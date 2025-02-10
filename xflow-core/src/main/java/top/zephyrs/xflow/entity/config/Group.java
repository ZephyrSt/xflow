package top.zephyrs.xflow.entity.config;

import lombok.Data;

/**
 * 流程分组
 */
@Data
public class Group {

    private Long groupId;

    /**
     * 名称
     */
    private String groupName;

    /**
     * 备注
     */
    private String remark;

    /**
     * 父级ID
     */
    private Long parentId;

    /**
     * 父级名称
     */
    private String parentName;
}
