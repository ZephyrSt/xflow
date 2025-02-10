package top.zephyrs.xflow.entity.flow;

import lombok.Data;
import top.zephyrs.xflow.enums.NodeStatusEnum;
import top.zephyrs.xflow.enums.NodeTypeEnum;

import java.util.Date;

/**
 * 业务流程当前在办节点
 */
@Data
public class FlowNodeCurrent {

    /**
     * ID
     */
    private Long currentId;

    /**
     * 业务流程ID
     */
    private Long flowId;

    /**
     * 节点标识
     */
    private String nodeId;

    /**
     * 节点标题
     */
    private String nodeTitle;

    /**
     * 节点类型
     */
    private NodeTypeEnum nodeType;

    /**
     * 节点状态
     */
    private NodeStatusEnum status;

    /**
     * 合计票数（票选节点）
     */
    private Integer ticketTotal;

    /**
     * （节点）完成时间
     */
    private Date finishTime;

    /**
     * 创建时间
     */
    private Date createTime;

}

