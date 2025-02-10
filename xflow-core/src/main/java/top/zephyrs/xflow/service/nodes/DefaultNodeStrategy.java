package top.zephyrs.xflow.service.nodes;

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
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node,
                                                User operator, List<User> candidates, Map<String, Object> data) {

        if(candidates == null || candidates.isEmpty()) {
            throw new FlowConfigurationIncorrectException("Not found next approval user !!!");
        }
        //创建待办节点
        Long flowId = flow.getFlowId();
        FlowNodeCurrent current = flowDataService.createCurrent(flowId, node, false);
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
     * 拒绝后处理
     */
    protected void doFinishCurrentOnReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                                           User operator, List<User> candidates, Map<String, Object> data) {
        FlowNodeCurrentLog currentLog = flowDataService.finishCurrent(current, NodeStatusEnum.rejected);
        //退回申请人，并结束流程
        flowDataService.finishFlow(flow.getFlowId(), FlowStatusEnum.rejected);
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
                List<FlowNodeCurrentInfo> nextCurrents = wrapper.createNode(publish, flow, next, operator, candidates, data);
                currentInfoList.addAll(nextCurrents);
            }
        }
        return currentInfoList;
    }

}
