package top.zephyrs.xflow.entity.flow;

import lombok.Data;
import top.zephyrs.xflow.enums.FlowStatusEnum;

import java.util.Date;

/**
 * 业务流程记录
 */
@Data
public class Flow {

    /**
     * ID
     */
    private Long flowId;

    /**
     * 业务唯一标识
     */
    private String bizId;

    /**
     * 使用的已发布流程ID
     */
    private Long publishId;

    /**
     * 流程定义ID
     */
    private Long configId;

    /**
     * 状态
     */
    private FlowStatusEnum status;

    /**
     * 创建时间
     */
    private Date createTime;

    /**
     * 完成时间
     */
    private Date finishTime;
}
