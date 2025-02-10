package top.zephyrs.xflow.entity.users;

import lombok.Data;

import java.util.List;

/**
 * 人员筛选信息
 */
@Data
public class UserFilter {
    //类型
    private String type;
    //筛选条件
    private String filter;
    //指定的人员
    private List<User> users;
    //指定的角色
    private List<Role> roles;
    //指定的部门
    private List<Dept> depts;

}
