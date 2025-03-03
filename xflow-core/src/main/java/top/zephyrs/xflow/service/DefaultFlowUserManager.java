package top.zephyrs.xflow.service;

import top.zephyrs.xflow.configs.XFlowConstants;
import top.zephyrs.xflow.data.UserDAO;
import top.zephyrs.xflow.entity.query.Query;
import top.zephyrs.xflow.entity.users.Dept;
import top.zephyrs.xflow.entity.users.DeptTree;
import top.zephyrs.xflow.entity.users.Role;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.utils.BeanUtils;
import top.zephyrs.xflow.utils.JSONUtils;

import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 根据用户筛选信息获取用户
 */
public class DefaultFlowUserManager implements FlowUserManager {


    private final UserDAO userMapper;

    public DefaultFlowUserManager(UserDAO userMapper) {
        this.userMapper = userMapper;
    }

    @Override
    public List<User> getUsers(String filter, top.zephyrs.xflow.entity.users.User operator, List<User> candidates) {
        top.zephyrs.xflow.entity.users.UserFilter userFilter = null;
        if(filter != null) {
            userFilter = JSONUtils.fromJson(filter, top.zephyrs.xflow.entity.users.UserFilter.class);
        }
        return this.defaultUserFilterProcess(userFilter, operator, candidates);
    }

    @Override
    public List<User> getUserByQuery(Query query) {
        return userMapper.selectUserByQuery(query);
    }

    @Override
    public List<Role> getRoleByQuery(Query query) {
        return userMapper.selectRoleByQuery(query);
    }

    @Override
    public List<DeptTree> getDeptTree(String parentId) {
        List<Dept> all = userMapper.selectDeptByParentId(parentId);
        List<DeptTree> treeList = all.parallelStream()
                .map(dict -> BeanUtils.convertBean(dict, top.zephyrs.xflow.entity.users.DeptTree.class))
                .collect(Collectors.toList());
        return streamToTree(treeList, parentId);
    }

    private List<DeptTree> streamToTree(List<DeptTree> treeList, String rootId) {
        return treeList.stream()
                // 过滤父节点
                .filter(parent -> parent.getParentId().equals(rootId))
                // 把父节点children递归赋值成为子节点
                .peek(child -> child.setChildren(streamToTree(treeList, child.getDeptId())))
                .collect(Collectors.toList());
    }

    protected List<User> defaultUserFilterProcess(top.zephyrs.xflow.entity.users.UserFilter filter, top.zephyrs.xflow.entity.users.User operator, List<User> candidates) {
        if(filter == null) {
            return Collections.emptyList();
        }
        //指定人员
        if(filter.getType().equals(XFlowConstants.USER_FILTER_TYPE_USER)) {
            if(filter.getUsers().isEmpty()) {
                return Collections.emptyList();
            }
//            List<String> userIdList = filter.getUsers().stream().map(User::getUserId).collect(Collectors.toList());
//            return userMapper.selectUserByIds(userIdList);
            return filter.getUsers();
        }
        //指定角色
        else if(filter.getType().equals(XFlowConstants.USER_FILTER_TYPE_ROLE)) {
            if(filter.getRoles().isEmpty()) {
                return Collections.emptyList();
            }
            //筛选条件：全部人员
            if(filter.getFilter().equals(XFlowConstants.USER_FILTER_FILTER_ALL)) {
                List<String> roleIdList = filter.getRoles().stream().map(top.zephyrs.xflow.entity.users.Role::getRoleId).collect(Collectors.toList());;
                return userMapper.selectUserByRoles(roleIdList);
            }
            //筛选条件：本部门
            if(filter.getFilter().equals(XFlowConstants.USER_FILTER_FILTER_DEPT)) {
                List<String> roleIdList = filter.getRoles().stream().map(top.zephyrs.xflow.entity.users.Role::getRoleId).collect(Collectors.toList());
                List<String> deptIdList = userMapper.selectDeptByUserId(operator.getUserId())
                        .stream().map(top.zephyrs.xflow.entity.users.Dept::getDeptId).collect(Collectors.toList());
                return userMapper.selectUserByRolesAndDepts(roleIdList, deptIdList);
            }
            //筛选条件：上级部门
            if(filter.getFilter().equals(XFlowConstants.USER_FILTER_FILTER_MANAGE_DEPT)) {
                List<String> roleIdList = filter.getRoles().stream().map(Role::getRoleId).collect(Collectors.toList());
                List<String> deptIdList = userMapper.selectDeptByUserId(operator.getUserId())
                        .stream().map(top.zephyrs.xflow.entity.users.Dept::getParentId).collect(Collectors.toList());
                return userMapper.selectUserByRolesAndDepts(roleIdList, deptIdList);
            }
        }
        else if(filter.getType().equals(XFlowConstants.USER_FILTER_TYPE_SELECT)) {
            return candidates;
        }
        return Collections.emptyList();
    }

}
