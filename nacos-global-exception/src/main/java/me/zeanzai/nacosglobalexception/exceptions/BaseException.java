package me.zeanzai.nacosglobalexception.exceptions;

import java.io.Serializable;

/**
 * @author shawnwang
 * @version 1.0
 * @describe
 * @date 2023/4/7
 */
public class BaseException extends RuntimeException implements Serializable {
    public BaseException() {
    }

    public BaseException(String message) {
        super(message);
    }

    public BaseException(Throwable cause) {
        super(cause);
    }

    public BaseException(String message, Throwable cause) {
        super(message, cause);
    }
}

