package top.zephyrs.xflow.entity.flow;

import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.fasterxml.jackson.databind.ser.std.ToStringSerializer;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Data;
import top.zephyrs.mybatis.semi.annotations.Column;
import top.zephyrs.mybatis.semi.annotations.Primary;
import top.zephyrs.mybatis.semi.annotations.Table;
import top.zephyrs.mybatis.semi.plugins.typehandlers.JsonTypeHandler;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;
import top.zephyrs.xflow.handlers.UserListTypeHandler;

import java.util.Date;
import java.util.List;
import java.util.Map;

@Schema(name = "业务流程待办任务")
@Data
@Table("x_flow_task")
public class FlowTask {

    @Schema(description = "ID")
    @JsonSerialize(using = ToStringSerializer.class)
    @Primary
    private Long taskId;
    @Schema(description = "业务流程ID")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long flowId;
    @Schema(description = "在办流程节点")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long currentId;

    @Schema(description = "操作人ID")
    private String userId;
    @Schema(description = "操作人姓名")
    private String userName;
    @Schema(description = "处理动作/状态")
    private TaskActionEnum action;
    @Schema(description = "备注")
    private String remark;

    @Schema(description = "候选人")
    @Column(typeHandler = UserListTypeHandler.class)
    private List<User> candidates;

    @Schema(description = "附加数据")
    @Column(typeHandler = JsonTypeHandler.class)
    private Map<String, Object> data;

    @Schema(description = "任务类型")
    private TaskTypeEnum type;

    @Schema(description = "上一任务ID(转办，移送时使用)")
    @JsonSerialize(using = ToStringSerializer.class)
    private Long prevId;

    @Schema(description = "（任务）接收时间")
    private Date receiveTime;
    @Schema(description = "（任务）完成时间")
    private Date finishTime;
}
