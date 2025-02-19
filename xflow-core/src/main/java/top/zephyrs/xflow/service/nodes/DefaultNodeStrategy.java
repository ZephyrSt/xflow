package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.xflow.entity.config.ConfigEdge;
import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.*;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.*;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

public class DefaultNodeStrategy implements NodeStrategy {

    protected final ConfigService configService;
    protected final FlowDataService flowDataService;

    protected final NodeStrategyWrapper wrapper;

    protected DefaultNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        this.configService = configService;
        this.flowDataService = flowDataService;
        this.wrapper = wrapper;
    }


    /**
     * 创建待办节点
     *
     * @param node       节点配置
     * @param flow       业务记录
     * @param data       附加数据
     * @param candidates 任务候选人/接收人
     */
    @Override
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node, FlowNodeCurrentLog prevCurrent,
                                                User operator, List<User> candidates, Map<String, Object> data) {

        if(candidates == null || candidates.isEmpty()) {
            throw new FlowConfigurationIncorrectException("Not found next approval user !!!");
        }
        //创建待办节点
        Long flowId = flow.getFlowId();
        FlowNodeCurrent current = flowDataService.createCurrent(flowId, prevCurrent == null? null: prevCurrent.getCurrentId(), node, 0,false);
        Long currentId = current.getCurrentId();
        //创建一般任务
        List<FlowTask> taskList = flowDataService.createTasks(flowId, currentId, candidates, null, TaskTypeEnum.Approval, null);
        return Collections.singletonList(new FlowNodeCurrentInfo(current, taskList));
    }

    @Override
    public void onApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                           User operator, List<User> candidates, Map<String, Object> data) {
        //默认或签，一人处理即办结节点
        this.doFinishCurrentOnApproval(publish, flow, current, operator, candidates, data);
    }

    @Override
    public void onReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                         User operator, List<User> candidates, Map<String, Object> data) {
        //默认或签，一人处理即办结节点
        this.doFinishCurrentOnReject(publish, flow, current, operator, candidates, data);
    }

    /**
     * 通过后创建后续节点
     */
    protected void doFinishCurrentOnApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                                   User operator, List<User> candidates, Map<String, Object> data){
        FlowNodeCurrentLog currentLog = flowDataService.finishCurrent(current, NodeStatusEnum.finished);
        //已办结节点为结束节点，办结流程
        if(currentLog.getNodeType() == NodeTypeEnum.end) {
            flowDataService.finishFlow(flow.getFlowId(), FlowStatusEnum.finished);
            return;
        }
        this.doCreateNextNodes(publish, flow, currentLog, operator, candidates, data);
    }

    /**
     * 节点驳回后处理
     */
    protected FlowNodeCurrentLog doFinishCurrentOnReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                                           User operator, List<User> candidates, Map<String, Object> data) {
        //获取驳回策略
        ConfigNode configNode = publish.getConfig().getNodes().stream().filter(node -> node.getId().equals(current.getNodeId())).findFirst().orElse(null);
        if (configNode == null || configNode.getData() == null) {
            throw new FlowConfigurationIncorrectException("flow node not found! publishId=" + publish.getPublishId() + ", currentId=" + current.getCurrentId());
        }

        FlowNodeCurrentLog currentLog = flowDataService.finishCurrent(current, NodeStatusEnum.rejected);

        ConfigNode reject2Node = this.getPrevRejectNode(current, publish.getConfig(), configNode);
        //退回申请人，并结束流程
        if(reject2Node == null || reject2Node.getType().equals(NodeTypeEnum.start)) {
            flowDataService.finishFlow(flow.getFlowId(), FlowStatusEnum.rejected);
        }
        else {
            wrapper.createNode(publish, flow, reject2Node, currentLog, operator, candidates, data);
        }
        return currentLog;
    }


    /**
     * 创建后续节点
     */
    protected List<FlowNodeCurrentInfo> doCreateNextNodes(ConfigPublish publish, Flow flow, FlowNodeCurrentLog currentLog,
                                     User operator, List<User> candidates, Map<String, Object> data) {
        List<ConfigNode> nextNodes = configService.getNextNodes(publish, currentLog.getNodeId(), EdgeTypeEnum.approved, data);;
        List<FlowNodeCurrentInfo> currentInfoList = new ArrayList<>();
        //有指定的后续节点，创建指定的后续节点
        if(nextNodes.isEmpty()) {
            throw new FlowConfigurationIncorrectException("not found next node from publish(publishId="+publish.getPublishId()+")");
        } else {
            for(ConfigNode next: nextNodes) {
                List<FlowNodeCurrentInfo> nextCurrents = wrapper.createNode(publish, flow, next, currentLog, operator, candidates, data);
                currentInfoList.addAll(nextCurrents);
            }
        }
        return currentInfoList;
    }


    /**
     * 获取退回到哪一个节点
     * @return null: 退回申请人或未找到对应节点
     */
    protected ConfigNode getPrevRejectNode(FlowNodeCurrent current, Config config, ConfigNode configNode) {

        RejectStrategyEnum rejectStrategy = configNode.getData().getReject();
        if(rejectStrategy == null) {
            rejectStrategy = wrapper.getDefaultRejectStrategy();
        }
        if(rejectStrategy.equals(RejectStrategyEnum.start)) {
            return null;
        }
        if(rejectStrategy.equals(RejectStrategyEnum.prev)) {
            List<String> sourceNodeIdList = config.getEdges().stream()
                    .filter(edge -> edge.getTarget().equalsIgnoreCase(current.getNodeId()))
                    .map(ConfigEdge::getSource)
                    .collect(Collectors.toList());
            List<FlowNodeCurrentLog> nodeLogList = flowDataService.getCurrentLogByFlowId(current.getFlowId());
            FlowNodeCurrentLog log = this.getPrevNode(current.getPrevId(), nodeLogList, sourceNodeIdList);
            if(log == null) {
                return null;
            }
            return config.getNodes().stream().filter(node -> node.getId().equalsIgnoreCase(log.getNodeId())).findFirst().orElse(null);
        }
        if(rejectStrategy.equals(RejectStrategyEnum.designated)) {
            String toNodeId = configNode.getData().getRejectNodeId();
            return config.getNodes().stream().filter(node-> node.getId().equalsIgnoreCase(toNodeId)).findFirst().orElse(null);
        }
        return null;
    }

    private FlowNodeCurrentLog getPrevNode(Long prevId, List<FlowNodeCurrentLog> nodeLogList, List<String> sourceNodeIdList) {
        FlowNodeCurrentLog log = nodeLogList.stream().filter(nodeLog -> nodeLog.getCurrentId().equals(prevId)).findFirst().orElse(null);
        if(log == null) {
            return null;
        }
        if(log.getNodeType() == NodeTypeEnum.condition || !sourceNodeIdList.contains(log.getNodeId())) {
            return getPrevNode(log.getPrevId(), nodeLogList, sourceNodeIdList);
        } else if(log.getNodeType() == NodeTypeEnum.end ||
                log.getNodeType() == NodeTypeEnum.robot
        ) {
            throw new FlowConfigurationIncorrectException("reject prev node failed! prev node is "+log.getNodeType()+" node!");
        }
        return log;
    }


}
