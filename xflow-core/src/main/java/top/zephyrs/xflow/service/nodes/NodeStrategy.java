package top.zephyrs.xflow.service.nodes;

import top.zephyrs.xflow.entity.config.ConfigNode;
import top.zephyrs.xflow.entity.config.ConfigPublish;
import top.zephyrs.xflow.entity.flow.Flow;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;
import top.zephyrs.xflow.entity.flow.dto.FlowNodeCurrentInfo;
import top.zephyrs.xflow.entity.users.User;

import java.util.List;
import java.util.Map;

public interface NodeStrategy {

    /**
     * 创建待办节点
     *
     * @param node       节点配置
     * @param flow       业务记录
     * @param candidates 任务候选人/接收人
     * @param data       附加数据
     */
    List<FlowNodeCurrentInfo> createNode(ConfigPublish publish, Flow flow, ConfigNode node, FlowNodeCurrentLog prevCurrent,
                                         User operator, List<User> candidates, Map<String, Object> data);

    void onApproval(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                    User operator, List<User> candidates, Map<String, Object> data);

    void onReject(ConfigPublish publish, Flow flow, FlowNodeCurrent current,
                  User operator, List<User> candidates, Map<String, Object> data);

}
