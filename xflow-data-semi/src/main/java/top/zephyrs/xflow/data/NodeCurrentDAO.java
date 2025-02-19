package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.enums.NodeStatusEnum;

import java.util.List;

public interface NodeCurrentDAO {

    int insert(FlowNodeCurrent current);

    int deleteById(Long currentId);

    FlowNodeCurrent selectById(Long currentId);
    List<FlowNodeCurrent> selectByFlowId(Long flowId);

    FlowNodeCurrent selectByFlowIdAndNodeId(Long flowId, String nodeId);

    int updateStatusByCurrentId(Long currentId, NodeStatusEnum newStatus, NodeStatusEnum oldStatus);

}
