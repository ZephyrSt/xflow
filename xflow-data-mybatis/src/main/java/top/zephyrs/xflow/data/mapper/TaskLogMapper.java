package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.TaskLogDAO;
import top.zephyrs.xflow.entity.flow.FlowTaskLog;

import java.util.List;

@Mapper
public interface TaskLogMapper extends TaskLogDAO {

    @Insert("insert into x_flow_task_log" +
            "(task_id, flow_id, current_id, user_id, user_name, action, remark, candidates, data, prev_id, type, receive_time, finish_time)" +
            "values(#{taskId}, #{flowId}, #{currentId}, #{userId}, #{userName}, #{action}, #{remark}, " +
            "#{candidates}, #{data}, #{prevId}, #{type}, #{receiveTime}, #{finishTime}) ")
    @Override
    int insert(FlowTaskLog taskLog);

    @Select("select t.* from x_flow_task_log t where task_id=#{taskId}")
    @Override
    FlowTaskLog selectById(Long taskId);

    @Select("select t.* from x_flow_task_log t where flow_id=#{flowId}")
    @Override
    List<FlowTaskLog> selectByFlowId(Long flowId);

    @Select("select t.* from x_flow_task_log t where current_id=#{currentId}")
    @Override
    List<FlowTaskLog> selectByCurrentId(Long currentId);


}
