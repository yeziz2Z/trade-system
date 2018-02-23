package com.liuk.trade.common.exception;

/**
 * Created by kl on 2018/2/17.
 */
public class LiukMQException extends Exception{


    private static final long serialVersionUID = 2874098092853513310L;

    public LiukMQException() {
        super();
    }

    public LiukMQException(String message) {
        super(message);
    }

    public LiukMQException(String message, Throwable cause) {
        super(message, cause);
    }

    public LiukMQException(Throwable cause) {
        super(cause);
    }
}
