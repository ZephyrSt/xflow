package top.zephyrs.xflow.enums;

/**
 * 拒绝策略
 */
public enum RejectStrategyEnum {

    /**
     * 退回到申请人
     */
    start,
    /**
     * 上一节点
     */
    prev,
    /**
     * 指定的
     */
    designated,
}
