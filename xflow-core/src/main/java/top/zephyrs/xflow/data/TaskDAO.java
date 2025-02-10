package top.zephyrs.xflow.data;

import top.zephyrs.xflow.entity.flow.FlowTask;

import java.util.List;

public interface TaskDAO {

    int insert(FlowTask task);

    int insertBatch(List<FlowTask> tasks);

    int updateById(FlowTask task);

    int deleteById(Long taskId);

    int deleteByCurrentId(Long currentId);

    FlowTask selectById(Long taskId);

    List<FlowTask> selectByCurrentId(Long currentId);

    List<FlowTask> selectByFlowId(Long flowId);

    List<FlowTask> selectByCurrentIdAndUserId(Long currentId, String userId);


}
