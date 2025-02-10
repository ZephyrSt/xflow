package top.zephyrs.xflow.exceptions;

/**
 * 无效的业务流程
 */
public class InvalidFlowException extends FlowException{

    public InvalidFlowException(Throwable cause) {
        super(cause);
    }

    public InvalidFlowException(String message) {
        super(message);
    }

    public InvalidFlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
