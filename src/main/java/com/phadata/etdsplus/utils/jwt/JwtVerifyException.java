package com.phadata.etdsplus.utils.jwt;


/**
 * jwt验证业务异常
 * @author linx
 */
public class JwtVerifyException extends RuntimeException {


    public JwtVerifyException() {
    }


    public JwtVerifyException(String message, int code) {
        super(message);
    }


    public JwtVerifyException(String message) {
        super(message);
    }


    public JwtVerifyException(String message, Throwable cause) {
        super(message, cause);
    }
}

