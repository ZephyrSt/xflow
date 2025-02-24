package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import org.apache.ibatis.annotations.Update;
import top.zephyrs.xflow.data.NodeCurrentDAO;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.enums.NodeStatusEnum;

import java.util.List;

@Mapper
public interface NodeCurrentMapper extends BaseMapper<FlowNodeCurrent>, NodeCurrentDAO {

    @Override
    @Select("select t.* from x_flow_node_current t where flow_id=#{flowId}")
    List<FlowNodeCurrent> selectByFlowId(@Param("flowId") Long flowId);

    @Override
    @Select("select t.* from x_flow_node_current t where flow_id=#{flowId} and node_id=#{nodeId}")
    FlowNodeCurrent selectByFlowIdAndNodeId(@Param("flowId") Long flowId, @Param("nodeId") String nodeId);

    @Override
    @Update("update x_flow_node_current set status=#{newStatus} where current_id=#{currentId} and status=#{oldStatus}")
    int updateStatusByCurrentId(@Param("currentId") Long currentId, @Param("newStatus") NodeStatusEnum newStatus, @Param("oldStatus") NodeStatusEnum oldStatus);

}
