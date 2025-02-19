package top.zephyrs.xflow.enums;

/**
 * 投票判定方式
 */
public enum VoteTypeEnum implements TypeEnum<String> {

    rate("rate", "百分比"),
    ticket("ticket", "票数"),
    ;

    private final String key;
    private final String label;

    VoteTypeEnum(String key, String label) {
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
