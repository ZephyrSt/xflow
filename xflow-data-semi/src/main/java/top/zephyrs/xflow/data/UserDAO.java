package top.zephyrs.xflow.data;

import org.apache.ibatis.annotations.Param;
import top.zephyrs.xflow.entity.query.Query;
import top.zephyrs.xflow.entity.users.Dept;
import top.zephyrs.xflow.entity.users.Role;
import top.zephyrs.xflow.entity.users.User;

import java.io.Serializable;
import java.util.List;

public interface UserDAO {

    List<User> selectUserByIds(List<String> users);

    List<User> selectUserByRoles(List<String> roles);

    List<User> selectUserByRolesAndDepts(List<String> roles, List<String> depts);

    List<User> selectUserByQuery(Query query);

    List<Dept> selectDeptByParentId(Serializable parentId);

    List<Dept> selectDeptByUserId(Serializable userId);

    List<Role> selectRoleByQuery(@Param("query") Query query);
}
