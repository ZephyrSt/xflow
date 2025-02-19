package top.zephyrs.xflow.enums;

/**
 * 流程节点处理动作枚举||或者任务状态
 *
 提交：
 审核：提交任务，交给下一个任务节点
 委托：
 转办：
 终止：

 */
public enum TaskActionEnum implements TypeEnum<String> {
    /**任务状态：任务未处理*/
    Pending("pending","待处理"),
    /**任务状态：未认领。认领任务是针对或签或者公共任务，领后变为认领人的任务*/
    UnClaim("unClaim","待认领"),
    /**(开始节点)提交任务，交给下一个任务节点*/
    Submit("submit","提交"),
    /**认领任务*/
    Claim("claim","认领"),
    /**审核通过，交给下一个任务节点*/
    Approved("approved","通过"),
    /**将任务分给新的审批人处理，此时委派人不能审核，等新的审批人处理后，该任务还是会回到委派人手上*/
    Entrust("entrust", "委托"),
    /**将任务转办给新的审批人审核，任务的分配人更改成新的审批人，新的审批人审核后，该任务不会回到原来的转办人上*/
    Transfer("transfer", "转办"),
    /**驳回，审核不通过，根据策略退回到申请人/上一步/指定节点*/
    Reject("reject", "驳回"),
    /**撤回任务到本节点，仅下一待办未处理时可撤回*/
    Withdraw("withdraw", "撤回"),
    /**终止任务，一般为后台手动终止任务*/
    Stop("stop", "终止"),
    ;

    private final String key;
    private final String label;

    TaskActionEnum(String key, String label) {
        this.key = key;
        this.label = label;
    }

    @Override
    public String getKey() {
        return this.key;
    }

    @Override
    public String getLabel() {
        return this.label;
    }



}
