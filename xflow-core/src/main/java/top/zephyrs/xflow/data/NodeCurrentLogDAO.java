package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;

import java.util.List;

public interface NodeCurrentLogDAO {

    int insert(FlowNodeCurrentLog log);

    List<FlowNodeCurrent> selectByFlowIdAndBizId(String flowId, String bizId);

}
