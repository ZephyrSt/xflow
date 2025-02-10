package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.util.List;
import java.util.Map;

/**
 * 会签节点
 */
public class JointlyNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {

    protected JointlyNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        super(configService, flowDataService, wrapper);
    }

    @Override
    public void onApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current, User operator, List<User> candidates, Map<String, Object> data) {
        //没有待办即认为办结
        List<FlowTask> taskList = flowDataService.getTasksByCurrentId(current.getCurrentId());
        if(taskList.isEmpty()) {
            super.doFinishCurrentOnApproval(publish, flow, current, operator, candidates, data);
        }
    }

    @Override
    public void onReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current, User operator, List<User> candidates, Map<String, Object> data) {
        //会签有任意节点不通过即退回
        super.doFinishCurrentOnReject(publish, flow, current, operator, candidates, data);
    }
}
