package top.zephyrs.xflow.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.mybatis.semi.base.BaseMapper;
import top.zephyrs.xflow.data.UserDAO;
import top.zephyrs.xflow.entity.users.Dept;
import top.zephyrs.xflow.entity.users.User;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface UserMapper extends BaseMapper<User>, UserDAO {

    @Override
    @Select("<script>" +
            "select t.user_id, t.user_name from x_flow_u_user_info t " +
            "where user_id in <foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    List<User> selectUserByIds(List<String> users);

    @Override
    @Select("<script>" +
            "select t.* from x_flow_u_user_info t inner join x_flow_u_user_role r on t.user_id = r.user_id " +
            "where r.role_id in <foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    List<User> selectUserByRoles(List<String> roles);

    @Override
    @Select("<script>" +
            "select t.* from x_flow_u_user_info t " +
            "inner join x_flow_u_user_role ur on t.user_id = ur.user_id " +
            "inner join x_flow_u_user_dept ud on t.user_id = ud.user_id " +
            "where ur.role_id in <foreach item=\"item\" collection=\"roles\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "  and ud.dept_id in <foreach item=\"item\" collection=\"depts\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    List<User> selectUserByRolesAndDepts(@Param("roles") List<String> roles, @Param("depts") List<String> depts);

    @Override
    @Select("select t.*, p.dept_name as parent_name from x_flow_u_dept t left join x_flow_u_dept p on t.parent_id = p.dept_id where t.parent_id =#{parentId}")
    List<Dept> selectDeptByParentId(Serializable parentId);

    @Override
    @Select("select t.* from x_flow_u_dept t inner join x_flow_u_user_dept xfuud on t.dept_id = xfuud.dept_id where user_id = #{userId}")
    List<Dept> selectDeptByUserId(Serializable userId);


}
