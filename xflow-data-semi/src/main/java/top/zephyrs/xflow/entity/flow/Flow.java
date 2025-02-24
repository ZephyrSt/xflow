package top.zephyrs.xflow.entity.flow;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.mybatis.semi.annotations.Primary;
import top.zephyrs.mybatis.semi.annotations.Table;
import top.zephyrs.xflow.enums.FlowStatusEnum;

import java.util.Date;

/**
 * 业务流程记录
 */

@Schema(name = "业务流程记录表")
@Data
@Table("x_flow")
public class Flow {

    @Schema(description = "ID")
    @Primary
    private Long flowId;
    @Schema(description = "业务唯一标识")
    private String bizId;
    @Schema(description = "使用的已发布流程ID")
    private Long publishId;
    @Schema(description = "流程定义ID")
    private Long configId;
    @Schema(description = "状态： 0：办理中 1：已办结")
    private FlowStatusEnum status;

    @Schema(description = "创建时间")
    private Date createTime;
    @Schema(description = "完成时间")
    private Date finishTime;
}
