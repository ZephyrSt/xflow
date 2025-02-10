package top.zephyrs.xflow.entity.flow;

import lombok.Data;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskActionEnum;
import top.zephyrs.xflow.enums.TaskTypeEnum;

import java.util.Date;
import java.util.List;
import java.util.Map;

/**
 * 业务流程处理日志
 */
@Data
public class FlowTaskLog {

    /**
     * ID
     */
    private Long taskId;
    /**
     * 业务流程ID
     */
    private Long flowId;
    /**
     * 在办流程节点
     */
    private Long currentId;
    /**
     * 操作人ID
     */
    private String userId;
    /**
     * 操作人姓名
     */
    private String userName;
    /**
     * 处理动作
     */
    private TaskActionEnum action;
    /**
     * 备注
     */
    private String remark;

    /**
     * 候选人
     */
    private List<User> candidates;
    /**
     * 附加数据
     */
    private Map<String, Object> data;
    /**
     * 任务类型
     */
    private TaskTypeEnum type;

    /**
     * 上一任务ID(转办，移送时使用)
     */
    private Long prevId;

    /**
     * （任务）接收时间
     */
    private Date receiveTime;

    /**
     * （任务）完成时间
     */
    private Date finishTime;
}
