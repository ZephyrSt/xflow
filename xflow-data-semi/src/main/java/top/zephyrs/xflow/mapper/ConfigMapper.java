package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.entity.config.Config;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.data.ConfigDAO;
import top.zephyrs.xflow.entity.query.Query;
import top.zephyrs.xflow.entity.config.ConfigVO;

import java.util.List;

@Mapper
public interface ConfigMapper extends BaseMapper<Config>, ConfigDAO {

    @Override
    @Select("select * from x_flow_config where config_code=#{configCode}")
    Config selectByCode(@Param("configCode") String configCode);

    @Override
    @Select("<script>" +
            "select c.config_id, c.config_name, c.config_code, c.group_id, c.remark, c.create_time, c.modify_time, " +
            "xfg.group_name," +
            "xfcp.version, xfcp.create_time as publish_time " +
            "from x_flow_config c " +
            "left join x_flow_group xfg on c.group_id = xfg.group_id " +
            "left join x_flow_config_publish xfcp on c.config_id = xfcp.config_id and xfcp.is_active = true " +
            "<where> " +
            " <if test=\"query.groupId!= null and query.groupId != ''\"> and c.group_id = #{query.groupId}</if>" +
            " <if test=\"query.search!= null and query.search != ''\"> and (concat(c.config_code,',' ,c.config_name) like concat('%', #{query.search},'%'))</if>" +
            "</where>" +
            "</script>")
    List<ConfigVO> selectVOByQuery(@Param("query") Query query);
}
