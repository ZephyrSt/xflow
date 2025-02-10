package top.zephyrs.xflow.entity.config;

import lombok.Data;

import java.util.Date;
import java.util.List;

/**
 * 流程定义
 */
@Data
public class Config {

    /**
     * ID
     */
    private Long configId;

    /**
     * 流程定义名称
     */
    private String configName;

    /**
     * 流程定义编码
     */
    private String configCode;

    /**
     * 所属分组Id
     */
    private String groupId;

    /**
     * 说明
     */
    private String remark;

    /**
     * 节点
     */
    private List<ConfigNode> nodes;

    /**
     * 连接线
     */
    private List<ConfigEdge> edges;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 最后修改时间
     */
    private Date modifyTime;

}

