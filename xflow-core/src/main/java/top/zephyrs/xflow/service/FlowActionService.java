package top.zephyrs.xflow.service;

import org.springframework.transaction.annotation.Transactional;
import top.zephyrs.xflow.configs.XFlowConfig;
import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.NodeStatusEnum;
import top.zephyrs.xflow.enums.NodeTypeEnum;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.exceptions.FlowStatusNotSupportsException;
import top.zephyrs.xflow.exceptions.InvalidFlowException;
import top.zephyrs.xflow.exceptions.InvalidUserException;
import top.zephyrs.xflow.service.nodes.NodeStrategyWrapper;

import java.util.List;
import java.util.Map;

public class FlowActionService {

    protected final ConfigService configService;
    private final FlowDataService flowDataService;
    private final FlowLock flowLock;
    private final NodeStrategyWrapper nodeStrategyWrapper;

    public FlowActionService(ConfigService configService,
                             FlowDataService flowDataService,
                             FlowUserService flowUserService,
                             FlowLock flowLock, XFlowConfig flowConfig) {
        this.configService = configService;
        this.flowDataService = flowDataService;
        this.flowLock = flowLock;
        this.nodeStrategyWrapper = new NodeStrategyWrapper(configService, flowDataService, flowUserService, flowConfig);
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public Flow start(ConfigPublish publish, String bizId,
                      User submitter, List<User> candidates, Map<String, Object> data) {
        //获取开始节点配置, 开始节点必须是提交人节点
        ConfigNode start = publish.getConfig().getNodes().stream()
                .filter(flowNode -> flowNode.getType() == NodeTypeEnum.start)
                .findFirst()
                .orElse(null);
        try {
            assert start != null;
        } catch (Exception e) {
            throw new FlowConfigurationIncorrectException("flow config ( publishId:" + publish.getPublishId() + ") not found start node!!!");
        }

        //创建流程记录
        Flow flow = flowDataService.createFlow(publish, bizId);
        //创建待办节点
        nodeStrategyWrapper.createNode(publish, flow, start, null, submitter, candidates, data);
        return flow;
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public FlowTaskLog approval(ConfigPublish publish, Long taskId,
                         User operator, List<User> candidates,
                         String remark, Map<String, Object> data) {
        FlowTask task = flowDataService.getTaskById(taskId);
        if(task == null) {
            throw new InvalidFlowException("flow task not found (taskId="+taskId+"), it may have been completed.");
        }
        try{
            flowLock.tryLock(task.getCurrentId());
            //完成待办
            FlowTaskLog taskLog = flowDataService.finishTask(task, operator, TaskActionEnum.Approved, remark);
            //后续处理
            Long flowId = taskLog.getFlowId();
            Long currentId = taskLog.getCurrentId();
            nodeStrategyWrapper.doApproval(publish, flowId, currentId, operator, candidates, data, taskLog);
            return taskLog;
        }finally {
            flowLock.unLock(task.getCurrentId());
        }
    }

    @Transactional(rollbackFor = RuntimeException.class)
    public FlowTaskLog reject(ConfigPublish publish, Long taskId,
                       User operator, List<User> candidates,
                       String remark, Map<String, Object> data) {
        FlowTask task = flowDataService.getTaskById(taskId);
        if(task == null || task.getTaskId() == null) {
            throw new InvalidFlowException("flow task not found (taskId="+taskId+"), it may have been completed.");
        }
        try{
            flowLock.tryLock(task.getCurrentId());
            //完成待办
            FlowTaskLog taskLog = flowDataService.finishTask(task, operator, TaskActionEnum.Reject, remark);
            //后续处理
            Long flowId = taskLog.getFlowId();
            Long currentId = taskLog.getCurrentId();
            nodeStrategyWrapper.doReject(publish, flowId, currentId, operator, candidates, data, taskLog);
            return taskLog;
        }finally {
            flowLock.unLock(task.getCurrentId());
        }
    }

    /**
     * (公共)任务领取
     *
     * @param taskId 待办任务ID
     * @param user   领取人
     * @return 待办信息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public FlowTask claim(Long taskId, User user) {
        FlowTask task = flowDataService.getTaskById(taskId);
        Long currentId = task.getCurrentId();
        FlowNodeCurrent current = flowDataService.getCurrentById(currentId);
        //任务不存在
        if (current == null) {
            throw new InvalidFlowException("current Node info not found! currentId:" + currentId);
        }
        //任务已被认领
        if (current.getStatus() != NodeStatusEnum.unclaimed) {
            throw new FlowStatusNotSupportsException("flow node is claimed !");
        }
        //校验领取人员是否在候选人名单中
        if (task.getCandidates() != null && !task.getCandidates().isEmpty()) {
            User exists = task.getCandidates().stream().filter(candidate -> candidate.getUserId().equals(user.getUserId())).findFirst().orElse(null);
            if (exists == null) {
                throw new InvalidUserException("The designated user is not a candidate :" + user);
            }
        }
        try{
            flowLock.tryLock(currentId);
            return flowDataService.claimCurrent(task, user);
        }finally {
            flowLock.unLock(currentId);
        }
    }

    /**
     * 委派.
     * 将任务分给其它审批人处理，此时委派人不能审核，等新的审批人处理后，该任务还是会回到委派人手上,委派人审核通过后进入后续
     *
     * @param taskId   操作ID
     * @param operator 操作人
     * @param receiver 任务接收人
     * @param remark   备注
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public FlowTask entrust(Long taskId, User operator, User receiver, String remark) {
        FlowTask task = flowDataService.getTaskById(taskId);
        if(task == null) {
            throw new InvalidFlowException("flow task not found (taskId="+taskId+"), it may have been completed.");
        }
        //完成本次待办
        FlowTaskLog taskLog = flowDataService.finishTask(task, operator, TaskActionEnum.Entrust, remark);
        Long flowId = taskLog.getFlowId();
        Long currentId = taskLog.getCurrentId();
        //为接收人创建待办
        return flowDataService.createTask(flowId, currentId, receiver, null, TaskTypeEnum.Entrust, taskId);
    }

    /**
     * 转办.
     * 将任务转办给新的审批人审核，任务的分配人更改成新的审批人，新的审批人审核后，进入后续，该任务不会回到原来的转办人上
     * @param taskId 待办
     * @param operator 操作人
     * @param receiver 转办接收人
     * @param remark 备注
     * @return 待办任务信息
     */
    @Transactional(rollbackFor = RuntimeException.class)
    public FlowTask transfer(Long taskId, User operator, User receiver, String remark) {
        FlowTask task = flowDataService.getTaskById(taskId);
        if(task == null) {
            throw new InvalidFlowException("flow task not found (taskId="+taskId+"), it may have been completed.");
        }
        //完成本次待办
        FlowTaskLog taskLog = flowDataService.finishTask(task, operator, TaskActionEnum.Transfer, remark);
        Long flowId = taskLog.getFlowId();
        Long currentId = taskLog.getCurrentId();
        //为接收人创建待办
        return flowDataService.createTask(flowId, currentId, receiver, null, TaskTypeEnum.Transfer, taskId);
    }

}
