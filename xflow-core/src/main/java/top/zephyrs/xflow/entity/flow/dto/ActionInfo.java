package top.zephyrs.xflow.entity.flow.dto;

import lombok.Data;
import top.zephyrs.xflow.entity.users.User;
import top.zephyrs.xflow.enums.TaskActionEnum;

import java.util.List;
import java.util.Map;

/**
 * 流程操作信息
 */
@Data
public class ActionInfo {
    //流程编码
    private String flowCode;
    //业务唯一标识
    private String bizId;
    //任务唯一标识
    private Long taskId;
    //操作人
    private User user;
    private TaskActionEnum action;
    private String remark;
    //候选人，提交人自选后续处理人员时使用
    private List<User> candidates;
    //附加数据
    private Map<String, Object> data;
}
