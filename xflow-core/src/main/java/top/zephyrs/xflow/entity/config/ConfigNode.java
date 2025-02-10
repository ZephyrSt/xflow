package top.zephyrs.xflow.entity.config;

import lombok.Data;
import top.zephyrs.xflow.enums.NodeTypeEnum;

/**
 * 流程节点定义
 */
@Data
public class ConfigNode {
    /**
     * 节点标识
     */
    private String id;
    /**
     * 节点类型
     */
    private NodeTypeEnum type;

    /**
     * 节点坐标
     */
    private Position position;

    /**
     * 节点数据
     */
    private ConfigNodeData data;
}
