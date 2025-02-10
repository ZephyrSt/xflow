package top.zephyrs.xflow.exceptions;

/**
 * 流程状态不支持操作
 */
public class FlowStatusNotSupportsException extends FlowException{

    public FlowStatusNotSupportsException(Throwable cause) {
        super(cause);
    }

    public FlowStatusNotSupportsException(String message) {
        super(message);
    }

    public FlowStatusNotSupportsException(String message, Throwable cause) {
        super(message, cause);
    }
}
