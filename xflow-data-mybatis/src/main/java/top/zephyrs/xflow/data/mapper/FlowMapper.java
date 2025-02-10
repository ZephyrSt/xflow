package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.*;
import top.zephyrs.xflow.data.FlowDAO;
import top.zephyrs.xflow.entity.flow.Flow;

@Mapper
public interface FlowMapper extends FlowDAO {


    @Insert("insert into x_flow(flow_id, biz_id, publish_id, config_id, status) values (#{flowId}, #{bizId}, #{publishId}, #{configId}, #{status})")
    @Override
    int insert(Flow flow);

    @Update("update x_flow set status=#{status}, finish_time=#{finishTime} where flow_id=#{flowId}")
    @Override
    int updateById(Flow flow);

    @Select("select * from x_flow t where flow_id=#{flowId}")
    @Override
    Flow selectById(Long flowId);

    @Override
    @Select("select * from x_flow t where config_id=#{configId} and biz_id=#{bizId}")
    Flow selectByConfigIdAndBizId(@Param("configId") Long configId, @Param("bizId") String bizId);

    @Override
    @Select("select t.* from x_flow t inner join x_flow_config c on t.config_id=c.config_id where c.config_code=#{configCode} and biz_id=#{bizId}")
    Flow selectByConfigCodeAndBizId(@Param("configCode") String configCode, @Param("bizId") String bizId);
}
