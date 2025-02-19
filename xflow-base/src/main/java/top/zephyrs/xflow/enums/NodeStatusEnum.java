package top.zephyrs.xflow.enums;

/**
 * 节点状态（）
 */
public enum NodeStatusEnum implements TypeEnum<Integer> {

    unclaimed(-1, "待领取"),
    flowing(0, "办理中"),
    finished(1, "已办结"),
    rejected(2, "已驳回"),
    ;

    private final Integer key;
    private final String label;

    NodeStatusEnum(Integer key, String label) {
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
