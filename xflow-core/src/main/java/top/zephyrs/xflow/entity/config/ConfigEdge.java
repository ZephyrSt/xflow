package top.zephyrs.xflow.entity.config;

import lombok.Data;
import top.zephyrs.xflow.enums.EdgeTypeEnum;

import java.util.Map;

/**
 * 节点连接接信息
 */
@Data
public class ConfigEdge {

    /**
     * 下一节点ID
     */
    private String id;
    /**
     * 类型
     */
    private EdgeTypeEnum type;
    /**
     * 起始节点ID
     */
    private String source;
    /**
     * 目标定义ID
     */
    private String target;
    /**
     * 扩展信息
     */
    private Map<String, Object> data;
}
