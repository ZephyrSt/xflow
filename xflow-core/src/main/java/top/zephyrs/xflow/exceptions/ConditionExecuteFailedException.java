package top.zephyrs.xflow.exceptions;

/**
 * 条件执行失败异常
 */
public class ConditionExecuteFailedException extends FlowException{

    public ConditionExecuteFailedException(Throwable cause) {
        super(cause);
    }

    public ConditionExecuteFailedException(String message) {
        super(message);
    }

    public ConditionExecuteFailedException(String message, Throwable cause) {
        super(message, cause);
    }
}
