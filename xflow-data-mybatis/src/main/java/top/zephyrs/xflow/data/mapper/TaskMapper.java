package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.*;
import top.zephyrs.xflow.data.TaskDAO;
import top.zephyrs.xflow.entity.flow.FlowTask;

import java.util.List;

@Mapper
public interface TaskMapper extends TaskDAO {

    @Insert("insert into x_flow_task (task_id, flow_id, current_id, user_id, user_name, action, remark, candidates, data, prev_id, type) values " +
            "(#{taskId}, #{flowId}, #{currentId}, #{userId}, #{userName}, #{action}, #{remark}, #{candidates}, #{data}, #{prevId}, #{type})")
    @Override
    int insert(FlowTask task);
    @Insert("<script>" +
            "insert into x_flow_task (task_id, flow_id, current_id, user_id, user_name, action, remark, candidates, data, prev_id, type) values " +
            "<foreach item=\"item\" collection=\"tasks\" separator=\",\" open=\"(\" close=\")\">" +
            "#{item.taskId}, #{item.flowId}, #{item.currentId}, #{item.userId}, #{item.userName}, #{item.action}, #{item.remark}, #{item.candidates}, #{item.data}, #{item.prevId}, #{item.type}" +
            "</foreach>" +
            "</script>")
    @Override
    public int insertBatch(@Param("tasks") List<FlowTask> tasks);

    @Update("update x_flow_task set user_id=#{userId}, user_name=#{userName}, action=#{action} where task_id=#{taskId}")
    @Override
    public int updateById(FlowTask task);

    @Delete("delete from x_flow_task where task_id=#{taskId}")
    @Override
    public int deleteById(Long taskId);

    @Delete("delete from x_flow_task where current_id=#{currentId}")
    @Override
    int deleteByCurrentId(Long currentId);

    @Select("select t.* from x_flow_task t where task_id=#{taskId}")
    @Override
    public FlowTask selectById(Long taskId);

    @Select("select t.* from x_flow_task t where current_id=#{currentId}")
    @Override
    List<FlowTask> selectByCurrentId(Long currentId);

    @Select("select t.* from x_flow_task t where flow_id=#{flowId}")
    @Override
    List<FlowTask> selectByFlowId(Long flowId);

    @Select("select t.* from x_flow_task t where current_id=#{currentId} and user_id=#{userId}")
    @Override
    List<FlowTask> selectByCurrentIdAndUserId(@Param("currentId") Long currentId,
                                              @Param("userId") String userId);


}
