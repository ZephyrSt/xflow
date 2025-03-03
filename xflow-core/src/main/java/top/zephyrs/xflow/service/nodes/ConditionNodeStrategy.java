package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.configs.XFlowConstants;
import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.NodeStatusEnum;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.util.List;
import java.util.Map;

/**
 * 会签节点
 */
public class ConditionNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {

    protected ConditionNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        super(configService, flowDataService, wrapper);
    }


    @Override
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node, FlowNodeCurrentLog prevCurrent,
                                                User operator, List<User> candidates, Map<String, Object> data) {
        String condition = node.getData().getConditional();
        //条件节点直接办结, 创建办结记录（获取后续节点时已经过滤掉不符合条件的节点）
        FlowNodeCurrentLog currentLog = flowDataService.createCurrentLog(flow.getFlowId(), prevCurrent == null? null: prevCurrent.getCurrentId(), node, NodeStatusEnum.finished);
        FlowTaskLog taskLog = flowDataService.createTaskLog(flow.getFlowId(), currentLog.getCurrentId(), XFlowConstants.SYSTEM_USER, TaskActionEnum.Approved,
                "条件判断："+condition, data, TaskTypeEnum.Condition, null);
        //创建后续节点
        return super.doCreateNextNodes(publish, flow, currentLog, operator, candidates, data);
    }
}
