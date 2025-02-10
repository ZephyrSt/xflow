package top.zephyrs.xflow.data.mapper;

import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import org.apache.ibatis.annotations.Select;
import top.zephyrs.xflow.data.UserDAO;
import top.zephyrs.xflow.entity.Query;
import top.zephyrs.xflow.entity.users.Dept;
import top.zephyrs.xflow.entity.users.Role;
import top.zephyrs.xflow.entity.users.User;

import java.io.Serializable;
import java.util.List;

@Mapper
public interface UserMapper extends UserDAO {

    @Select("<script>" +
            "select t.user_id, t.user_name from x_flow_u_user_info t " +
            "where user_id in <foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    @Override
    List<User> selectUserByIds(List<String> users);

    @Select("<script>" +
            "select t.* from x_flow_u_user_info t inner join x_flow_u_user_role r on t.user_id = r.user_id " +
            "where r.role_id in <foreach item=\"item\" collection=\"list\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    @Override
    List<User> selectUserByRoles(List<String> roles);

    @Select("<script>" +
            "select t.* from x_flow_u_user_info t " +
            "inner join x_flow_u_user_role ur on t.user_id = ur.user_id " +
            "inner join x_flow_u_user_dept ud on t.user_id = ud.user_id " +
            "where ur.role_id in <foreach item=\"item\" collection=\"roles\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "  and ud.dept_id in <foreach item=\"item\" collection=\"depts\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            "</script>")
    @Override
    List<User> selectUserByRolesAndDepts(@Param("roles") List<String> roles, @Param("depts") List<String> depts);

    @Select("<script>" +
            "select t.* from x_flow_u_user_info t " +
            "left join x_flow_u_user_role ur on t.user_id = ur.user_id " +
            "left join x_flow_u_user_dept ud on t.user_id = ud.user_id " +
            "left join x_flow_u_dept d on d.dept_id = ud.dept_id " +
            "left join x_flow_u_role r on r.role_id = ur.role_id " +
            "<where>" +
            " <if test=\"query.userName != null and query.userName!=''\">and t.user_name like concat('%',#{query.userName},'%')</if>" +
            " <if test=\"query.roleId != null and query.roleId!=''\">and r.role_id=#{query.roleId}</if>" +
            " <if test=\"query.deptId != null and query.deptId!=''\">and d.dept_id=#{query.deptId}</if>" +
            " <if test=\"query.depts != null and query.depts.size() > 0\">" +
            "   and d.dept_id in <foreach item=\"item\" collection=\"query.depts\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            " </if>" +
            " <if test=\"query.roles != null and query.roles.size() > 0\">" +
            "   and r.role_id in <foreach item=\"item\" collection=\"query.roles\" separator=\",\" open=\"(\" close=\")\">#{item}</foreach>" +
            " </if>" +
            "</where>" +
            "</script>")
    List<User> selectUserByQuery(@Param("params") Query query);

    @Select("select t.*, p.dept_name as parent_name from x_flow_u_dept t left join x_flow_u_dept p on t.parent_id = p.dept_id where t.parent_id =#{parentId}")
    @Override
    List<Dept> selectDeptByParentId(Serializable parentId);

    @Select("select t.* from x_flow_u_dept t inner join x_flow_u_user_dept xfuud on t.dept_id = xfuud.dept_id where user_id = #{userId}")
    @Override
    List<Dept> selectDeptByUserId(Serializable userId);

    @Select("<script>" +
            "select t.* from x_flow_u_role t " +
            "<where>" +
            "<if test=\"query.roleName!=null and query.roleName !=''\"> and t.role_name like concat('%',#{roleName},'%') </if>" +
            "<if test=\"query.roleId!=null and query.roleId !=''\"> and t.role_id like concat('%',#{roleId},'%')</if>" +
            "</where>" +
            "</script>")
    List<Role> selectRoleByQuery(@Param("query") Query query);
}
