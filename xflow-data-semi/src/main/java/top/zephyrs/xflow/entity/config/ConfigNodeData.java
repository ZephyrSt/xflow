package top.zephyrs.xflow.entity.config;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.xflow.enums.RejectStrategyEnum;
import top.zephyrs.xflow.enums.VoteTypeEnum;

import java.math.BigDecimal;
import java.util.Map;

@Schema(name = "流程节点附加配置信息", description = "包含条件判断等")
@Data
public class ConfigNodeData {

    @Schema(description = "节点名称")
    private String title;

    @Schema(description = "人员筛选信息")

    private Map<String, Object> filter;

    @Schema(description = "条件分支表达式")
    private String conditional;

    @Schema(description = "拒绝策略")
    private RejectStrategyEnum reject;

    @Schema(description = "拒绝后到达节点ID")
    private String rejectNodeId;

    @Schema(description = "投票判定方式")
    private VoteTypeEnum vote;
    @Schema(description = "投票判定权重")
    private BigDecimal weight;

}
