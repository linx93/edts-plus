package com.phadata.etdsplus.exception;


/**
 * 业务异常
 * @author linx
 */
public class BussinessException extends RuntimeException {


    public BussinessException() {
    }


    public BussinessException(String message, int code) {
        super(message);
    }


    public BussinessException(String message) {
        super(message);
    }


    public BussinessException(String message, Throwable cause) {
        super(message, cause);
    }
}

