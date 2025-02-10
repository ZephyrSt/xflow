package top.zephyrs.xflow.service;

import top.zephyrs.xflow.data.*;
import top.zephyrs.xflow.data.keys.XFlowKeySnowflake;
import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.*;
import top.zephyrs.xflow.entity.flow.dto.FlowInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.FlowStatusEnum;
import top.zephyrs.xflow.enums.NodeStatusEnum;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.exceptions.FlowStatusNotSupportsException;
import top.zephyrs.xflow.exceptions.InvalidFlowException;
import top.zephyrs.xflow.exceptions.InvalidUserException;
import top.zephyrs.xflow.utils.BeanUtils;

import java.util.*;

public class FlowDataService {

    private final FlowDAO flowMapper;
    private final NodeCurrentDAO nodeCurrentMapper;
    private final NodeCurrentLogDAO nodeLogMapper;
    private final TaskDAO taskMapper;
    private final TaskLogDAO taskLogMapper;

    private final FlowEventService flowEventService;

    private final XFlowKeySnowflake snowflake;

    public FlowDataService(FlowDAO flowMapper,
                           NodeCurrentDAO nodeCurrentMapper,
                           NodeCurrentLogDAO nodeLogMapper,
                           TaskDAO taskMapper,
                           TaskLogDAO taskLogMapper,
                           FlowEventService flowEventService, XFlowKeySnowflake snowflake) {
        this.flowMapper = flowMapper;
        this.nodeCurrentMapper = nodeCurrentMapper;
        this.nodeLogMapper = nodeLogMapper;
        this.taskMapper = taskMapper;
        this.taskLogMapper = taskLogMapper;
        this.flowEventService = flowEventService;
        this.snowflake = snowflake;
    }

    /**
     * 获取流程当前全部待办节点信息
     *
     * @param flowId
     * @return 流程待办信息
     */
    public FlowInfo getFlowCurrent(Long flowId) {
        Flow flow = flowMapper.selectById(flowId);
        List<FlowNodeCurrent> currents = nodeCurrentMapper.selectByFlowId(flowId);
        List<FlowTask> tasks = taskMapper.selectByFlowId(flowId);
        FlowInfo info = new FlowInfo();
        info.setFlow(flow);
        info.setCurrents(currents);
        info.setTasks(tasks);
        return info;
    }

    /**
     * 获取流程当前全部待办节点信息
     *
     * @param flowCode 流程编码
     * @param bizId    业务唯一标识
     * @return 流程待办信息
     */
    public FlowInfo getFlowCurrent(String flowCode, String bizId) {
        Flow flow = flowMapper.selectByConfigCodeAndBizId(flowCode, bizId);
        Long flowId = flow.getFlowId();
        List<FlowNodeCurrent> currents = nodeCurrentMapper.selectByFlowId(flowId);
        List<FlowTask> tasks = taskMapper.selectByFlowId(flowId);
        FlowInfo info = new FlowInfo();
        info.setFlow(flow);
        info.setCurrents(currents);
        info.setTasks(tasks);
        return info;
    }

    public Flow getFlowById(Long flowId) {
        return flowMapper.selectById(flowId);
    }

    public Flow getFlowByConfigIdAndBizId(String configCode, String bizId) {
        return flowMapper.selectByConfigCodeAndBizId(configCode, bizId);
    }

    /**
     * 获取待办节点
     *
     * @param currentId 待办节点ID
     * @return 待办节点
     */
    public FlowNodeCurrent getCurrentById(Long currentId) {
        return nodeCurrentMapper.selectById(currentId);
    }


    /**
     * 查询待办任务
     *
     * @param taskId ID
     * @return 待办记录
     */
    public FlowTask getTaskById(Long taskId) {
        return taskMapper.selectById(taskId);
    }

    /**
     * 查询未办结节点的待办
     *
     * @param currentId 未办结节点的ID
     * @return 待办任务列表
     */
    public List<FlowTask> getTasksByCurrentId(Long currentId) {
        return taskMapper.selectByCurrentId(currentId);
    }

    /**
     * 查询已完成待办记录
     *
     * @param taskId
     * @return
     */
    public FlowTaskLog getTaskLogById(Long taskId) {
        return taskLogMapper.selectById(taskId);
    }

    /**
     * 查询节点的已办记录
     *
     * @param currentId
     * @return
     */
    public List<FlowTaskLog> getTaskLogsByCurrentId(Long currentId) {
        return taskLogMapper.selectByCurrentId(currentId);
    }

    /**
     * 查询流程的全部待办任务
     *
     * @param flowId
     * @return
     */
    public List<FlowTask> getTasksByFlowId(Long flowId) {
        return taskMapper.selectByFlowId(flowId);
    }

    /**
     * 查询流程的全部已办任务
     *
     * @param flowId
     * @return
     */
    public List<FlowTaskLog> getTaskLogsByFlowId(Long flowId) {
        return taskLogMapper.selectByFlowId(flowId);
    }

    /**
     * 创建业务流程，
     * 根据 流程ID 和 bizId 确认记录是否存在。
     * 如果不存在，则新建
     * 如果已存在，且状态在办理中，则抛出 FlowStatusNotSupportsException
     * 否则修改状态为办理中
     *
     * @param publish 发布的流程配置信息
     * @param bizId   业务唯一标识
     * @return 业务流程定义
     * @throws FlowStatusNotSupportsException 业务流程已存在，且状态在办理中
     */
    public Flow createFlow(ConfigPublish publish, String bizId) {

        Flow flow = flowMapper.selectByConfigIdAndBizId(publish.getConfigId(), bizId);
        if (flow != null && flow.getStatus() == FlowStatusEnum.flowing) {
            throw new FlowStatusNotSupportsException("The business flow already exists!");
        } else if (flow == null) {
            flow = new Flow();
            flow.setStatus(FlowStatusEnum.flowing);
            flow.setBizId(bizId);
            flow.setConfigId(publish.getConfigId());
            flow.setPublishId(publish.getPublishId());
            flow.setFlowId(snowflake.nextId());
            flowMapper.insert(flow);
            flowEventService.flowCreated(flow);
            return flow;
        } else {
            flow.setStatus(FlowStatusEnum.flowing);
            flow.setConfigId(publish.getConfigId());
            flow.setPublishId(publish.getPublishId());
            flowMapper.updateById(flow);
            flowEventService.flowCreated(flow);
            return flow;
        }
    }

    /**
     * 结束业务流程
     *
     * @param flowId 业务流程ID
     * @return 业务流程对象
     */
    public Flow finishFlow(Long flowId, FlowStatusEnum status) {
        Flow flow = flowMapper.selectById(flowId);
        if (flow != null) {
            flow.setStatus(status);
            flow.setFinishTime(new Date());
            flowMapper.updateById(flow);
        }

        flowEventService.flowFinished(flow);
        return flow;
    }

    /**
     * 创建待办节点
     */
    public FlowNodeCurrent createCurrent(Long flowId, ConfigNode node, boolean needClaimed) {
        //校验是否存在
        FlowNodeCurrent exists = nodeCurrentMapper.selectByFlowIdAndNodeId(flowId, node.getId());
        if (exists != null) {
            throw new FlowStatusNotSupportsException("flow node is already exists!");
        }
        //创建待办节点信息
        FlowNodeCurrent current = new FlowNodeCurrent();
        current.setFlowId(flowId);
        current.setNodeId(node.getId());
        current.setNodeTitle(node.getData().getTitle());
        current.setNodeType(node.getType());
        current.setStatus(needClaimed ? NodeStatusEnum.unclaimed : NodeStatusEnum.flowing);
        current.setCurrentId(snowflake.nextId());
        nodeCurrentMapper.insert(current);
        flowEventService.nodeCreated(current);
        return current;
    }

    /**
     * 认领节点任务
     *
     * @param task 待办节点Id
     * @param user 认领人
     * @return 待办任务
     * @throws FlowStatusNotSupportsException 任务已被领取
     */
    public FlowTask claimCurrent(FlowTask task, User user) {
        int isClaimed = nodeCurrentMapper.updateStatusByCurrentId(task.getCurrentId(), NodeStatusEnum.flowing, NodeStatusEnum.unclaimed);
        if (isClaimed != 1) {
            throw new FlowStatusNotSupportsException("the flow maybe claimed!");
        }
        FlowTask flowTask = new FlowTask();
        flowTask.setTaskId(task.getTaskId());
        task.setUserId(user.getUserId());
        task.setUserName(user.getUserName());
        task.setAction(TaskActionEnum.Pending);
        taskMapper.updateById(task);
        flowEventService.taskClaimed(task);
        return task;
    }

    /**
     * 办结节点
     *
     * @throws FlowStatusNotSupportsException 节点已完成
     */
    public FlowNodeCurrentLog finishCurrent(FlowNodeCurrent current, NodeStatusEnum status) {
        //删除待办记录
        int isFinished = nodeCurrentMapper.deleteById(current.getCurrentId());
        if (isFinished != 1) {
            throw new FlowStatusNotSupportsException("flow node maybe finished!");
        }
        //创建历史记录
        FlowNodeCurrentLog currentLog = BeanUtils.convertBean(current, FlowNodeCurrentLog.class);
        currentLog.setStatus(status);
        currentLog.setFinishTime(new Date());
        nodeLogMapper.insert(currentLog);
        //删除全部待办节点
        taskMapper.deleteByCurrentId(current.getCurrentId());
        flowEventService.nodeFinished(currentLog);
        return currentLog;
    }

    /**
     * 直接创建已办结节点记录
     */
    public FlowNodeCurrentLog createCurrentLog(Long flowId, ConfigNode node, NodeStatusEnum status) {
        FlowNodeCurrentLog log = new FlowNodeCurrentLog();
        log.setFlowId(flowId);
        log.setNodeId(node.getId());
        log.setNodeTitle(node.getData().getTitle());
        log.setNodeType(node.getType());
        log.setStatus(status);
        log.setCreateTime(new Date());
        log.setFinishTime(new Date());
        log.setCurrentId(snowflake.nextId());
        nodeLogMapper.insert(log);
        return log;
    }

    public FlowTask createTask(Long flowId, Long currentId,
                               User user, List<User> candidates,
                               TaskTypeEnum type, Long lastId) {
        FlowTask task = new FlowTask();
        task.setFlowId(flowId);
        task.setCurrentId(currentId);
        if (user != null) {
            task.setUserId(user.getUserId());
            task.setUserName(user.getUserName());
            task.setAction(TaskActionEnum.Pending);
        } else {
            task.setAction(TaskActionEnum.UnClaim);
        }
        task.setCandidates(candidates);
        task.setType(type);
        task.setPrevId(lastId);
        task.setReceiveTime(new Date());
        task.setTaskId(snowflake.nextId());
        taskMapper.insert(task);
        flowEventService.taskCreated(Collections.singletonList(task));
        return task;
    }

    public List<FlowTask> createTasks(Long flowId, Long currentId,
                                      List<User> userList, List<User> candidates,
                                      TaskTypeEnum type, Long lastId) {
        List<FlowTask> dataList = new ArrayList<>();
        for (User receiver : userList) {
            FlowTask task = new FlowTask();
            task.setTaskId(snowflake.nextId());
            task.setFlowId(flowId);
            task.setCurrentId(currentId);
            task.setUserId(receiver.getUserId());
            task.setUserName(receiver.getUserName());
            task.setAction(TaskActionEnum.Pending);
            task.setCandidates(candidates);
            task.setType(type);
            task.setPrevId(lastId);
            task.setReceiveTime(new Date());
            dataList.add(task);
        }
        taskMapper.insertBatch(dataList);
        flowEventService.taskCreated(dataList);
        return dataList;
    }

    /**
     * 完成待办
     *
     * @return 流程操作日志
     * @throws InvalidUserException 当前操作人不是任务指定的人员
     */
    public FlowTaskLog finishTask(FlowTask task,
                                  User operator, TaskActionEnum action,
                                  String remark) {
        //完成待办
        if (!task.getUserId().equals(operator.getUserId())) {
            throw new InvalidUserException("Expected user:(userId=" + task.getUserId() + ", userName=" + task.getUserName() + "), but provided user:(userId=" + operator.getUserId() + ", userName=" + operator.getUserName() + ")");
        }
        int isFinished = taskMapper.deleteById(task.getTaskId());
        if (isFinished == 0) {
            throw new InvalidFlowException("flow task not found, it may have been completed.");
        }
        FlowTaskLog taskLog = BeanUtils.convertBean(task, FlowTaskLog.class);
        taskLog.setAction(action);
        taskLog.setRemark(remark);
        taskLog.setFinishTime(new Date());
        taskLogMapper.insert(taskLog);
        flowEventService.taskFinished(taskLog);
        return taskLog;
    }

    /**
     * 创建操作历史记录
     *
     * @return 流程操作日志
     */
    public FlowTaskLog createTaskLog(Long flowId, Long currentId,
                                     User user, TaskActionEnum action,
                                     String remark, Map<String, Object> data, TaskTypeEnum type, Long lastId) {
        FlowTaskLog taskLog = new FlowTaskLog();
        taskLog.setTaskId(snowflake.nextId());
        taskLog.setFlowId(flowId);
        taskLog.setCurrentId(currentId);

        taskLog.setUserId(user.getUserId());
        taskLog.setUserName(user.getUserName());
        taskLog.setAction(action);

        taskLog.setRemark(remark);
        taskLog.setData(data);
        taskLog.setType(type);
        taskLog.setPrevId(lastId);
        taskLog.setReceiveTime(new Date());
        taskLog.setFinishTime(new Date());

        taskLogMapper.insert(taskLog);
        return taskLog;
    }

}
