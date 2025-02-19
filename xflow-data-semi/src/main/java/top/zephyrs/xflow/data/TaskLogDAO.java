package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.flow.FlowTaskLog;

import java.util.List;

public interface TaskLogDAO {

    int insert(FlowTaskLog taskLog);

    FlowTaskLog selectById(Long taskId);

    List<FlowTaskLog> selectByFlowId(Long flowId);

    List<FlowTaskLog> selectByCurrentId(Long currentId);

}
