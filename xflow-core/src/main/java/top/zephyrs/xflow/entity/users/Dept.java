package top.zephyrs.xflow.entity.users;

import lombok.Data;

/**
 * 部门
 */
@Data
public class Dept {

    /**
     * ID
     */
    private String deptId;
    /**部门名称*/
    private String deptName;
    /**说明*/
    private String remark;

    /**父级ID*/
    private String parentId;

    /**父级名称*/
    private String parentName;
}
