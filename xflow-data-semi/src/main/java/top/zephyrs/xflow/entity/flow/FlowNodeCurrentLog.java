package top.zephyrs.xflow.entity.flow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.mybatis.semi.annotations.Primary;
import top.zephyrs.mybatis.semi.annotations.Table;
import top.zephyrs.xflow.enums.NodeStatusEnum;
import top.zephyrs.xflow.enums.NodeTypeEnum;

import java.util.Date;

@Schema(name = "业务流程节点记录")
@Data
@Table("x_flow_node_current_log")
public class FlowNodeCurrentLog {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @Primary
    private Long currentId;

    @Schema(description = "上一节点ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prevId;

    @Schema(description = "业务流程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long flowId;

    @Schema(description = "节点标识")
    private String nodeId;
    @Schema(description = "节点标题")
    private String nodeTitle;

    @Schema(description = "节点类型")
    private NodeTypeEnum nodeType;
    @Schema(description = "节点状态")
    private NodeStatusEnum status;

    @Schema(description = "（节点）完成时间")
    private Date finishTime;

    @Schema(description = "创建时间")
    private Date createTime;
}

