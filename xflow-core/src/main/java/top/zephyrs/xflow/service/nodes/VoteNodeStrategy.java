package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigNodeData;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.enums.VoteTypeEnum;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.util.List;
import java.util.Map;

public class VoteNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {
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
        int checkResult = this.checkVoteResult(publish, current);
        if(checkResult==1) {
            super.onApproval(publish, flow, current, operator, candidates, data);
        }else if(checkResult == -1) {
            super.onReject(publish, flow, current, operator, candidates, data);
        }
    }

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
        Integer agree = 0;
        Integer disagree = 0;
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
            }else if(new BigDecimal(disagree).compareTo(voteWeight)>0) {
                return -1;//不通过
            }else {
                return 0;//票数未达到
            }
        }else {
            if(new BigDecimal(agree).divide(total, RoundingMode.DOWN).compareTo(voteWeight) >= 0) {
                return 1;//通过
            }else if(new BigDecimal(disagree).divide(total, RoundingMode.DOWN).compareTo(voteWeight) >= 0) {
                return -1;
            }else {
                return 0;//票数未达到
            }
        }
    }
}
