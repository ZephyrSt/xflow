package top.zephyrs.xflow.exceptions;

public class FlowException extends RuntimeException{

    public FlowException(Throwable cause) {
        super(cause);
    }

    public FlowException(String message) {
        super(message);
    }

    public FlowException(String message, Throwable cause) {
        super(message, cause);
    }
}
