package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowTask;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.exceptions.FlowConfigurationIncorrectException;
import top.zephyrs.xflow.service.ConfigService;
import top.zephyrs.xflow.service.FlowDataService;

import java.util.Collections;
import java.util.List;
import java.util.Map;

public class ClaimNodeStrategy extends DefaultNodeStrategy implements NodeStrategy {

    protected ClaimNodeStrategy(ConfigService configService, FlowDataService flowDataService, NodeStrategyWrapper wrapper) {
        super(configService, flowDataService, wrapper);
    }

    @Override
    public List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node,
                                                User operator, List<User> candidates, Map<String, Object> data) {
        if(candidates == null || candidates.isEmpty()) {
            throw new FlowConfigurationIncorrectException("Not found next approval user !!!");
        }
        //任务是否需要进入候选人表，然后等待领取
        //创建待办节点
        Long flowId = flow.getFlowId();
        FlowNodeCurrent current = flowDataService.createCurrent(flowId, node, true);
        Long currentId = current.getCurrentId();
        //创建一般任务, 任务所属用户为空
        FlowTask task = flowDataService.createTask(flowId, currentId, null, candidates, TaskTypeEnum.Approval, null);
        return Collections.singletonList(new FlowNodeCurrentInfo(current, Collections.singletonList(task)));
    }

}
