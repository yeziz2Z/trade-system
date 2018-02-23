package com.liuk.trade.common.exception;

/**
 * Created by kl on 2018/2/18.
 */
public class LiukOrderException extends RuntimeException {
    public LiukOrderException() {
    }

    public LiukOrderException(String message) {
        super(message);
    }

    public LiukOrderException(String message, Throwable cause) {
        super(message, cause);
    }
}
