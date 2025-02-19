package top.zephyrs.xflow.exceptions;

/**
 * 无效的操作用户/ 操作用户判定失败
 */
public class TaskNotClaimedException extends FlowException{

    public TaskNotClaimedException(String message) {
        super(message);
    }

    public TaskNotClaimedException(String message, Throwable cause) {
        super(message, cause);
    }
}
