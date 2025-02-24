package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.data.NodeCurrentLogDAO;
import top.zephyrs.xflow.entity.flow.FlowNodeCurrentLog;

@Mapper
public interface NodeCurrentLogMapper extends BaseMapper<FlowNodeCurrentLog>, NodeCurrentLogDAO {

}
