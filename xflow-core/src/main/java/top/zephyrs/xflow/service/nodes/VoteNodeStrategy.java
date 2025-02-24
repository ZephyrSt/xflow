package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;
import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigNodeData;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.enums.VoteTypeEnum;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.Collections;
import java.util.List;
import java.util.Map;

public class VoteNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {

    @Override
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node, FlowNodeCurrentLog prevCurrent,
                                                User operator, List<User> candidates, Map<String, Object> data) {

        if(candidates == null || candidates.isEmpty()) {
            throw new FlowConfigurationIncorrectException("Not found next approval user !!!");
        }
        //创建待办节点
        Long flowId = flow.getFlowId();
        FlowNodeCurrent current = flowDataService.createCurrent(flowId, prevCurrent == null? null: prevCurrent.getCurrentId(), node, candidates.size(), false);
        Long currentId = current.getCurrentId();
        //创建一般任务
        List<FlowTask> taskList = flowDataService.createTasks(flowId, currentId, candidates, null, TaskTypeEnum.Approval, null);
        return Collections.singletonList(new FlowNodeCurrentInfo(current, taskList));
    }


    protected VoteNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        super(configService, flowDataService, wrapper);
    }

    @Override
    public void onApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current, User operator, List<User> candidates, Map<String, Object> data) {
        this.doCheckAndExecuteFinish(publish, flow, current, operator, candidates, data);
    }

    @Override
    public void onReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current, User operator, List<User> candidates, Map<String, Object> data) {
        this.doCheckAndExecuteFinish(publish, flow, current, operator, candidates, data);
    }

    private void doCheckAndExecuteFinish(ConfigPublish publish, Flow flow, FlowNodeCurrent current, User operator, List<User> candidates, Map<String, Object> data) {
        //判断投票结果是否通过
        int checkResult = this.checkVoteResult(publish, current);
        if(checkResult==1) {
            super.onApproval(publish, flow, current, operator, candidates, data);
        }else if(checkResult == -1) {
            super.onReject(publish, flow, current, operator, candidates, data);
        }
    }

    /**
     * 判断节点是否办结
     * @return -1: 节点不通过 1：节点通过 0：票数未达到判断标准
     */
    private int checkVoteResult(ConfigPublish publish, FlowNodeCurrent current) {
        ConfigNode configNode = publish.getConfig().getNodes().stream().filter(node -> node.getId().equals(current.getNodeId())).findFirst().orElse(null);
        if (configNode == null || configNode.getData() == null) {
            throw new FlowConfigurationIncorrectException("flow node not found! publishId=" + publish.getPublishId() + ", currentId=" + current.getCurrentId());
        }
        //默认模式为百分比, 权重为 50%
        VoteTypeEnum voteType = VoteTypeEnum.rate;
        BigDecimal voteWeight = new BigDecimal("0.5");
        ConfigNodeData nodeData = configNode.getData();
        if (VoteTypeEnum.ticket == nodeData.getVote()) {
            voteType = VoteTypeEnum.ticket;
        }
        if (nodeData.getWeight() != null) {
            voteWeight = nodeData.getWeight();
        }
        //统计全部的票数
        BigDecimal total = new BigDecimal(current.getTicketTotal());
        int agree = 0;
        int disagree = 0;
        List<FlowTaskLog> logList = flowDataService.getTaskLogsByCurrentId(current.getCurrentId());
        for(FlowTaskLog log: logList) {
            if(log.getType() != TaskTypeEnum.Entrust) {
                if (log.getAction() == TaskActionEnum.Approved) {
                    agree +=1;
                }else if(log.getAction() == TaskActionEnum.Reject) {
                    disagree += 1;
                }
            }
        }
        //票数决定
        if(VoteTypeEnum.ticket == voteType) {
            if(new BigDecimal(agree).compareTo(voteWeight) >= 0) {
                return 1; //通过
            }else if(new BigDecimal(disagree).compareTo(total.subtract(voteWeight))>0) {
                return -1;//不通过
            }else {
                return 0;//票数未达到
            }
        }else {
            BigDecimal weight = new BigDecimal(agree).divide(total, 2,RoundingMode.DOWN);
            if(weight.compareTo(voteWeight) >= 0) {
                return 1;//通过
            }else if(new BigDecimal(disagree).divide(total, RoundingMode.DOWN).compareTo(new BigDecimal(1).subtract(voteWeight)) >= 0) {
                return -1;
            }else {
                return 0;//票数未达到
            }
        }
    }
}
