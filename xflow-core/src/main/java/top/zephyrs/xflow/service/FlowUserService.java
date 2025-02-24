package top.zephyrs.xflow.service;

import top.zephyrs.xflow.entity.query.Query;
import top.zephyrs.xflow.entity.users.DeptTree;
import top.zephyrs.xflow.entity.users.Role;
import top.zephyrs.xflow.entity.users.User;

import java.util.List;

/**
 * 根据用户筛选信息获取用户
 */
public interface FlowUserService {

    /**
     * 根据操作人和人员筛选条件查询后续用户
     * @param filterJson 人员筛选信息
     * @param operator
     * @return
     */
    List<User> getUsers(String filterJson, User operator, List<User> candidates);

    List<User> getUserByQuery(Query query);

    List<Role> getRoleByQuery(Query query);

    List<DeptTree> getDeptTree(String parentId);
}
