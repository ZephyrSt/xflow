package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.FlowDAO;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.entity.flow.Flow;

@Mapper
public interface FlowMapper extends BaseMapper<Flow>, FlowDAO {

    @Override
    @Select("select * from x_flow t where config_id=#{configId} and biz_id=#{bizId}")
    Flow selectByConfigIdAndBizId(@Param("configId") Long configId, @Param("bizId") String bizId);

    @Override
    @Select("select t.* from x_flow t inner join x_flow_config c on t.config_id=c.config_id where c.config_code=#{configCode} and biz_id=#{bizId}")
    Flow selectByConfigCodeAndBizId(@Param("configCode") String configCode, @Param("bizId") String bizId);
}
