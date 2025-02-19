package top.zephyrs.xflow.entity.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import top.zephyrs.xflow.enums.EdgeTypeEnum;

import java.util.Map;

@Schema(name = "节点连接接信息")
@Data
@AllArgsConstructor
@NoArgsConstructor
public class ConfigEdge {

    @Schema(description = "下一节点ID")
    private String id;
    @Schema(description = "类型")
    private EdgeTypeEnum type;
    @Schema(description = "起始节点ID")
    private String source;
    @Schema(description = "目标定义ID")
    private String target;
    @Schema(description = "扩展信息")
    private Map<String, Object> data;
}
