package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.GroupDAO;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.entity.config.Group;

import java.util.List;

@Mapper
public interface GroupMapper extends BaseMapper<Group>, GroupDAO {

    @Override
    @Select("select t.*, p.group_name as parent_name " +
            "from x_flow_group t " +
            "left join x_flow_group p on t.parent_id = p.group_id " +
            "where t.group_id = #{id}")
    Group selectById(@Param("id") Long id);

    @Override
    @Select("select t.*, p.group_name as parent_name " +
            "from x_flow_group t " +
            "left join x_flow_group p on t.parent_id = p.group_id ")
    List<Group> selectAll();

    @Override
    @Select("select t.*, p.group_name as parent_name " +
            "from x_flow_group t " +
            "left join x_flow_group p on t.parent_id = p.group_id " +
            "where t.parent_id =#{parentId}")
    List<Group> selectByParentId(Long parentId);

}
