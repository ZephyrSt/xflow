package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.TaskLogDAO;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;

import java.util.List;

@Mapper
public interface TaskLogMapper extends BaseMapper<FlowTaskLog>, TaskLogDAO {

    @Override
    @Select("select t.* from x_flow_task_log t where flow_id=#{flowId}")
    List<FlowTaskLog> selectByFlowId(Long flowId);

    @Override
    @Select("select t.* from x_flow_task_log t where current_id=#{currentId}")
    List<FlowTaskLog> selectByCurrentId(Long currentId);

}
