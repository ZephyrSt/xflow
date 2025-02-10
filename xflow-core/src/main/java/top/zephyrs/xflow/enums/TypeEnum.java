package top.zephyrs.xflow.enums;

public interface TypeEnum<T> {

    T getKey();

    String getLabel();

    default boolean isMe(T value) {
        return value!= null && this.getKey().equals(value);
    }

}
