package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.*;
import top.zephyrs.xflow.data.NodeCurrentDAO;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.enums.NodeStatusEnum;

import java.util.List;

@Mapper
public interface NodeCurrentMapper extends NodeCurrentDAO {

    @Insert("insert into x_flow_node_current(current_id, flow_id, node_id, node_title, node_type, status, ticket_total) " +
            "values (#{currentId}, #{flowId}, #{nodeId}, #{nodeTitle}, #{nodeType}, #{status}, #{ticketTotal})")
    @Override
    int insert(FlowNodeCurrent current);

    @Delete("delete from x_flow_node_current where current_id=#{currentId}")
    @Override
    int deleteById(Long currentId);

    @Select("select t.* from x_flow_node_current t where current_id=#{currentId}")
    @Override
    FlowNodeCurrent selectById(Long currentId);

    @Select("select t.* from x_flow_node_current t where flow_id=#{flowId}")
    @Override
    List<FlowNodeCurrent> selectByFlowId(@Param("flowId") Long flowId);

    @Select("select t.* from x_flow_node_current t where flow_id=#{flowId} and node_id=#{nodeId}")
    @Override
    FlowNodeCurrent selectByFlowIdAndNodeId(@Param("flowId") Long flowId, @Param("nodeId") String nodeId);

    @Update("update x_flow_node_current set status=#{newStatus} where current_id=#{currentId} and status=#{oldStatus}")
    @Override
    int updateStatusByCurrentId(@Param("currentId") Long currentId, @Param("newStatus") NodeStatusEnum newStatus, @Param("oldStatus") NodeStatusEnum oldStatus);

}
