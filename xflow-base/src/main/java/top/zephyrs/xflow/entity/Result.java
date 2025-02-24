package top.zephyrs.xflow.entity;

import lombok.Getter;

/**
 * 接口返回数据的封装
 * @param <T>
 */
@Getter
public class Result<T> {

    /**
     * 操作执行结果的编码
     */
    private final String code;

    /**
     * 操作执行是否成功
     */
    private final boolean success;

    /**
     * 操作执行结果的说明
     */
    private final String msg;
    /**
     * 需要返回的数据，具体结构参见具体接口
     */
    private final T data;

    private Result(String code, boolean success, String msg, T data) {
        this.code = code;
        this.success = success;
        this.msg = msg;
        this.data = data;
    }

    public static <T> Result<T> success() {
        return new Result<>("200", true, "操作成功", null);
    }
    public static <T> Result<T> success(T data) {
        return new Result<>("200", true, "操作成功", data);
    }
    public static <T> Result<T> success(String msg, T data) {
        return new Result<>("200", true, msg, data);
    }

    public static <T> Result<T> paramError() {
        return new Result<>("101", false, "请求参数错误", null);
    }

    public static <T> Result<T> paramError(String msg) {
        return new Result<>("101", false, msg, null);
    }

    public static <T> Result<T> fail(String code, String msg, T data) {
        return new Result<T>(code, false, msg, data);
    }

    public static <T> Result<T> fail(String code, String msg) {
        return new Result<T>(code, false, msg, null);
    }

    @Override
    public String toString() {
        return "Result{" +
                "code='" + code + '\'' +
                ", success=" + success +
                ", msg='" + msg + '\'' +
                ", data=" + data +
                '}';
    }
}
