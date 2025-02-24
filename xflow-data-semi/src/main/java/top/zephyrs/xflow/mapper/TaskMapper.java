package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.*;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.data.TaskDAO;
import top.zephyrs.xflow.entity.flow.FlowTask;

import java.util.List;

@Mapper
public interface TaskMapper extends BaseMapper<FlowTask>, TaskDAO {

    @Override
    @Select("select t.* from x_flow_task t where current_id=#{currentId}")
    List<FlowTask> selectByCurrentId(Long currentId);

    @Override
    @Select("select t.* from x_flow_task t where flow_id=#{flowId}")
    List<FlowTask> selectByFlowId(Long flowId);

    @Override
    @Select("select t.* from x_flow_task t where current_id=#{currentId} and user_id=#{userId}")
    List<FlowTask> selectByCurrentIdAndUserId(@Param("currentId") Long currentId,
                                              @Param("userId") String userId);

    @Override
    @Insert("<script>" +
            "insert into x_flow_task (task_id, flow_id, current_id, user_id, user_name, action, remark, candidates, data, prev_id, type) values " +
            "<foreach item=\"item\" collection=\"tasks\" separator=\",\">" +
            "(#{item.taskId}, #{item.flowId}, #{item.currentId}, #{item.userId}, #{item.userName}, #{item.action}, #{item.remark}, #{item.candidates, typeHandler=top.zephyrs.mybatis.semi.plugins.typehandlers.JsonTypeHandler}, #{item.data, typeHandler=top.zephyrs.mybatis.semi.plugins.typehandlers.JsonTypeHandler}, #{item.prevId}, #{item.type})" +
            "</foreach>" +
            "</script>")
    int insertBatch(@Param("tasks") List<FlowTask> tasks);

    @Override
    @Delete("delete from x_flow_task where current_id=#{currentId}")
    int deleteByCurrentId(Long currentId);


}
