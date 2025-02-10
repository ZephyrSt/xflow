package top.zephyrs.xflow.exceptions;

/**
 * 错误的流程配置异常
 */
public class FlowConfigurationIncorrectException extends FlowException {

    public FlowConfigurationIncorrectException(Throwable cause) {
        super(cause);
    }

    public FlowConfigurationIncorrectException(String message) {
        super(message);
    }

    public FlowConfigurationIncorrectException(String message, Throwable cause) {
        super(message, cause);
    }
}
