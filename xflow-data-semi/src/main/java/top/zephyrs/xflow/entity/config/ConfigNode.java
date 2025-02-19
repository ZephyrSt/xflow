package top.zephyrs.xflow.entity.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.xflow.enums.NodeTypeEnum;

@Schema(name = "流程节点定义", description = "流程节点定义")
@Data
public class ConfigNode {
    @Schema(description = "节点标识")
    private String id;
    @Schema(description = "节点类型")
    private NodeTypeEnum type;

    @Schema(description = "节点坐标")
    private Position position;

    @Schema(description = "节点数据")
    private ConfigNodeData data;
}
