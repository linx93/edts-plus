package com.phadata.etdsplus.exception.handler;


import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.utils.jwt.JwtVerifyException;
import com.phadata.etdsplus.utils.result.Result;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.converter.HttpMessageNotReadableException;
import org.springframework.validation.FieldError;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import java.io.PrintWriter;
import java.io.StringWriter;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * 异常统一处理
 *
 * @author linx
 */
@Slf4j
@RestControllerAdvice
public class CustomExceptionHandler {

    @ExceptionHandler(value = BussinessException.class)
    public Result exceptionHandler(BussinessException bussinessException) {
        log.error(getTrace(bussinessException));
        return Result.failed(bussinessException.getMessage());
    }

    @ExceptionHandler(value = JwtVerifyException.class)
    private Result<Map<String, Object>> exceptionHandler(JwtVerifyException exception) {
        log.error(getTrace(exception));
        return Result.failed(exception.getMessage());
    }


    @ExceptionHandler(value = MethodArgumentNotValidException.class)
    private Result<Map<String, Object>> exceptionHandler(MethodArgumentNotValidException exception) {
        List<FieldError> fieldErrors = exception.getBindingResult().getFieldErrors();
        Map<String, Object> map = new HashMap<>(16);
        for (FieldError fieldError : fieldErrors) {
            map.put(fieldError.getField(), fieldError.getDefaultMessage());
        }
        log.error(" 参数异常: \n{}", JSON.toJSONString(map));
        log.error(getTrace(exception));
        return Result.validateFailed(JSON.toJSONString(map));
    }

    @ExceptionHandler(value = Exception.class)
    public Result exceptionHandler(Exception exception) {
        if (exception instanceof HttpMessageNotReadableException) {
            log.error("参数不正确：{} ", exception.getMessage());
            return Result.failed("json格式不正确");
        }
        if (exception instanceof HttpRequestMethodNotSupportedException) {
            log.error("请求方式错误：{} ", exception.getMessage());
            return Result.failed("请求方式错误");
        }
        log.error(getTrace(exception));
        return Result.failed("系统内部错误");
    }

    public static String getTrace(Throwable t) {
        StringWriter stringWriter = new StringWriter();
        PrintWriter writer = new PrintWriter(stringWriter);
        t.printStackTrace(writer);
        StringBuffer buffer = stringWriter.getBuffer();
        return buffer.toString();
    }
}
