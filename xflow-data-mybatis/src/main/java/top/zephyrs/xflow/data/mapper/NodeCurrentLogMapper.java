package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.Insert;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.NodeCurrentLogDAO;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrent;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;

import java.util.List;

@Mapper
public interface NodeCurrentLogMapper extends NodeCurrentLogDAO {

    @Insert("insert into x_flow_node_current_log (current_id, flow_id, node_id, node_title, node_type, status, ticket_total, finish_time, create_time) VALUES " +
            "(#{currentId}, #{flowId}, #{nodeId}, #{nodeTitle}, #{nodeType}, #{status}, #{ticketTotal}, #{finishTime}, #{createTime})")
    @Override
    int insert(FlowNodeCurrentLog log);

    @Select("select t.* from x_flow_node_current t left join x_flow xf on t.flow_id = xf.flow_id where t.flow_id=#{flowId} and xf.biz_id=#{bizId}")
    @Override
    List<FlowNodeCurrent> selectByFlowIdAndBizId(@Param("flowId") String flowId, @Param("bizId") String bizId);

}
