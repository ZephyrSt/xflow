package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.NodeTypeEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;
import top.zephyrs.xflow.service.FlowUserService;
import top.zephyrs.xflow.utils.JSONUtils;

import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class NodeStrategyWrapper implements NodeStrategy {

    protected final ConfigService configService;
    protected final FlowDataService flowDataService;
    protected final FlowUserService flowUserService;

    private final Map<NodeTypeEnum, NodeStrategy> nodeMap = new HashMap<>();

    public NodeStrategyWrapper(ConfigService configService, FlowDataService flowDataService, FlowUserService flowUserService) {
        this.configService = configService;
        this.flowDataService = flowDataService;
        this.flowUserService = flowUserService;

        nodeMap.put(NodeTypeEnum.start, new StartNodeStrategy(configService, flowDataService, this));
        nodeMap.put(NodeTypeEnum.end, new EndNodeStrategy(configService, flowDataService, this));

        nodeMap.put(NodeTypeEnum.claim, new ClaimNodeStrategy(configService, flowDataService, this));
        nodeMap.put(NodeTypeEnum.approval, new ApproverNodeStrategy(configService, flowDataService, this));
        nodeMap.put(NodeTypeEnum.jointly, new JointlyNodeStrategy(configService, flowDataService, this));
        nodeMap.put(NodeTypeEnum.vote, new VoteNodeStrategy(configService, flowDataService, this));
        nodeMap.put(NodeTypeEnum.condition, new ConditionNodeStrategy(configService, flowDataService, this));
    }

    @Override
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node,
                                                User operator, List<User> candidates, Map<String, Object> data) {
        NodeStrategy target = nodeMap.get(node.getType());
        if(target == null) {
            throw new FlowConfigurationIncorrectException("node type: "+node.getType()+" is invalid!");
        }
        //根据节点配置获取任务承办人， 开始节点不需要查询承办人，为提交人
        List<User> actualCandidates = null;
        if(node.getType() == NodeTypeEnum.start) {
            actualCandidates = Collections.singletonList(operator);
        } else {
            String filter = JSONUtils.toJson(node.getData().getFilter());
            actualCandidates = flowUserService.getUsers(filter, operator, candidates);
        }
        return target.createNode(publish, flow, node, operator, actualCandidates, data);
    }

    public void doApproval(ConfigPublish publish, Long flowId, Long currentId, User operator, List<User> candidates, Map<String, Object> data, FlowTaskLog taskLog) {
        //如果是委派任务，为委派人创建待办(委派任务完成后需要委派人审核确认)
        if (TaskTypeEnum.Entrust == taskLog.getType()) {
            FlowTask entrust = flowDataService.getTaskById(taskLog.getPrevId());
            flowDataService.createTask(taskLog.getFlowId(), taskLog.getCurrentId(), new User(entrust.getUserId(), entrust.getUserName()), null, TaskTypeEnum.EntrustBack, taskLog.getTaskId());
            //委派任务返还委派人，不会办结节点
        } else {
            Flow flow = flowDataService.getFlowById(flowId);
            FlowNodeCurrent current = flowDataService.getCurrentById(currentId);
            this.onApproval(publish, flow, current, operator, candidates, data);
        }
    }

    public void doReject(ConfigPublish publish, Long flowId, Long currentId, User operator, List<User> candidates, Map<String, Object> data, FlowTaskLog taskLog) {
        //如果是委派任务，为委派人创建待办(委派任务完成后需要委派人审核确认)
        if (TaskTypeEnum.Entrust == taskLog.getType()) {
            FlowTask entrust = flowDataService.getTaskById(taskLog.getPrevId());
            flowDataService.createTask(taskLog.getFlowId(), taskLog.getCurrentId(), new User(entrust.getUserId(), entrust.getUserName()), null, TaskTypeEnum.EntrustBack, taskLog.getTaskId());
            //委派任务返还委派人，不会办结节点
        } else {
            Flow flow = flowDataService.getFlowById(flowId);
            FlowNodeCurrent current = flowDataService.getCurrentById(currentId);
            this.onReject(publish, flow, current, operator, candidates, data);
        }
    }

    @Override
    public void onApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                                                User operator, List<User> candidates, Map<String, Object> data) {
        NodeStrategy target = nodeMap.get(current.getNodeType());
        if(target == null) {
            throw new FlowConfigurationIncorrectException("node type: "+current.getNodeType()+" is invalid!");
        }
        target.onApproval(publish, flow, current, operator, candidates, data);
    }

    @Override
    public void onReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                         User operator, List<User> candidates, Map<String, Object> data) {
        NodeStrategy target = nodeMap.get(current.getNodeType());
        if(target == null) {
            throw new FlowConfigurationIncorrectException("node type: "+current.getNodeType()+" is invalid!");
        }
        target.onReject(publish, flow, current, operator, candidates, data);
    }

}
