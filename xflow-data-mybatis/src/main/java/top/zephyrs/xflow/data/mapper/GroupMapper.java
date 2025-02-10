package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.*;
import top.zephyrs.xflow.data.GroupDAO;
import top.zephyrs.xflow.entity.config.Group;

import java.util.List;

@Mapper
public interface GroupMapper extends GroupDAO {

    @Override
    @Insert("insert into x_flow_group(group_id, parent_id, group_name, remark) values (#{groupId}, #{parentId}, #{groupName}, #{remark})")
    int insert(Group group);

    @Update("update x_flow_group set group_name = #{groupName}, parent_id=#{parentId}, remark=#{remark} where group_id=#{groupId}")
    @Override
    int updateById(Group group);

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
