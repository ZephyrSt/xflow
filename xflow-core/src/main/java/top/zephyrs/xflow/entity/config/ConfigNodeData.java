package top.zephyrs.xflow.entity.config;

import lombok.Data;
import top.zephyrs.xflow.enums.RejectStrategyEnum;
import top.zephyrs.xflow.enums.VoteTypeEnum;

import java.math.BigDecimal;
import java.util.Map;

/**
 * 流程节点附加配置信息
 */
@Data
public class ConfigNodeData {

    /**
     * 节点名称
     */
    private String title;

    /**
     * 节点展示
     */
    private String text;

    /**
     * 人员筛选信息
     */
    private Map<String, Object> filter;

    /**
     * 条件分支表达式
     */
    private String conditional;

    /**
     * 拒绝策略
     */
    private RejectStrategyEnum reject;

    /**
     * 拒绝后到达节点ID
     */
    private String rejectNodeId;

    /**
     * 投票判定方式
     */
    private VoteTypeEnum vote;
    /**
     * 投票判定权重
     */
    private BigDecimal weight;

}
