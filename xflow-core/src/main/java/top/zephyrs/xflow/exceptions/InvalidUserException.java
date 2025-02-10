package top.zephyrs.xflow.exceptions;

/**
 * 无效的操作用户/ 操作用户判定失败
 */
public class InvalidUserException extends RuntimeException{

    public InvalidUserException(String message) {
        super(message);
    }

    public InvalidUserException(String message, Throwable cause) {
        super(message, cause);
    }
}
