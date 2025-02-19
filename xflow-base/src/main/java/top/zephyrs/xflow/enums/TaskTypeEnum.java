package top.zephyrs.xflow.enums;

/**
 * 待办类型/待办来源类型
 */
public enum TaskTypeEnum{

    /**委托， 将任务分给新的审批人处理，此时委派人不能审核，等新的审批人处理后，该任务还是会回到委派人手上*/
    Entrust,
    /**委托返还， 被委托人办结后回到委托人手中的任务类型*/
    EntrustBack,
    /**转办， 将任务转办给新的审批人审核，任务的分配人更改成新的审批人，新的审批人审核后，该任务不会回到原来的转办人上*/
    Transfer,
    /**条件判断，*/
    Condition,
    /**用户审批， 一般的审核审批节点，来自上一节点提交*/
    Approval,
    /** 提交 */
    Submit,
    /**结束*/
    End,
    ;
}
