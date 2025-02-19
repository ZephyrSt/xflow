package top.zephyrs.xflow.enums;

public enum FlowStatusEnum implements TypeEnum<Integer> {

    flowing(0, "办理中"),
    finished(1, "已办结"),
    rejected(2, "已驳回"),
    ;

    private final Integer key;
    private final String label;

    FlowStatusEnum(Integer key, String label) {
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
