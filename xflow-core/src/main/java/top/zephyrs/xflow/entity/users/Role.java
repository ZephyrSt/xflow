package top.zephyrs.xflow.entity.users;

import lombok.Data;

/**
 * 流程角色
 */
@Data
public class Role {

    /**ID*/
    private String roleId;
    /**角色名称*/
    private String roleName;
    /**说明*/
    private String remark;
}
