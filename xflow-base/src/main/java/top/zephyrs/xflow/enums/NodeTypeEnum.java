package top.zephyrs.xflow.enums;

/**
 * 节点类型（start, 申请人节点（提交节点），审批节点，或签节点，会签节点，分支节点, end）
 */
public enum NodeTypeEnum implements TypeEnum<Integer> {

    /**发起人节点*/
    start(0, "发起人节点"),
    /**流程结束节点*/
    end(-1, "结束"),
    /** 或签节点 */
    approval(1, "或签"),
    /** 会签节点，需要全部人员通过后才可通过 */
    jointly(2, "会签"),
    /** 票签节点， 需要达到指定人数或百分比才通过*/
    vote(3, "票签"),
    /** 条件判断节点(连线) */
    condition(4, "条件分支"),
    //    /** 公共任务节点，需要认领任务才能确定待办 */
    claim(5, "认领"),
//    /**自动化节点*/
    robot(9, "自动化")
    ;

    private final Integer key;
    private final String label;

    NodeTypeEnum(Integer key, String label) {
        this.key = key;
        this.label = label;
    }

    @Override
    public Integer getKey() {
        return this.key;
    }

    @Override
    public String getLabel() {
        return this.label;
    }
}
