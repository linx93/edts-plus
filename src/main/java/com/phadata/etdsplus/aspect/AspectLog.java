package com.phadata.etdsplus.aspect;

import com.alibaba.fastjson.JSONObject;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.phadata.etdsplus.utils.IpUtil;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.aspectj.lang.JoinPoint;
import org.aspectj.lang.ProceedingJoinPoint;
import org.aspectj.lang.annotation.AfterThrowing;
import org.aspectj.lang.annotation.Around;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Pointcut;
import org.aspectj.lang.reflect.MethodSignature;
import org.springframework.stereotype.Component;
import org.springframework.util.Assert;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;
import org.springframework.web.multipart.MultipartFile;

import javax.servlet.http.HttpServletRequest;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @author top
 */
@Slf4j
@Aspect
@Component
public class AspectLog {


    @Pointcut("execution(public * com.phadata.etdsplus.controller..*.*(..))")
    public void point() {

    }

    @Around("point()")
    public Object doAround(ProceedingJoinPoint point) throws Throwable {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        Assert.notNull(requestAttributes, "request can not null");
        HttpServletRequest request = requestAttributes.getRequest();
        long start = System.currentTimeMillis();
        Object result = point.proceed();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(IpUtil.getIpAddr(request));
        requestInfo.setUri(request.getRequestURL().toString());
        requestInfo.setHttpMethod(request.getMethod());
        requestInfo.setClassMethod(String.format("%s.%s", point.getSignature().getDeclaringTypeName(), point.getSignature().getName()));
        if (log.isDebugEnabled()) {
            requestInfo.setRequestParams(getRequestParamsByProceedingJoinPoint(point));
            requestInfo.setResult(result);
        }
        requestInfo.setSuccess("成功");
        requestInfo.setExecuteTime(System.currentTimeMillis() - start);
        requestInfo.setMessage("");
        requestInfo.setExceptionStacks(null);
        log.info("请求信息 ----------------------------------  \n {} \n-----------------------------------------------", JSONObject.toJSONString(requestInfo, true));
        return result;
    }


    @AfterThrowing(pointcut = "point()", throwing = "e")
    public void doAfterThrow(JoinPoint joinPoint, RuntimeException e) {
        ServletRequestAttributes requestAttributes = (ServletRequestAttributes) RequestContextHolder
                .getRequestAttributes();
        Assert.notNull(requestAttributes, "request can not null");
        HttpServletRequest request = requestAttributes.getRequest();
        RequestInfo requestInfo = new RequestInfo();
        requestInfo.setIp(IpUtil.getIpAddr(request));
        requestInfo.setUri(request.getRequestURL().toString());
        requestInfo.setHttpMethod(request.getMethod());
        requestInfo.setClassMethod(String.format("%s.%s", joinPoint.getSignature().getDeclaringTypeName(), joinPoint.getSignature().getName()));
        if (log.isDebugEnabled()) {
            requestInfo.setRequestParams(getRequestParamsByJoinPoint(joinPoint));
        }
        requestInfo.setMessage(getExceptionType(e).toString());
        requestInfo.setSuccess("失败");
        requestInfo.setExecuteTime(0L);
        List<String> exceptions = new ArrayList<>();
        for (StackTraceElement traceElement : e.getStackTrace()) {
            String className = traceElement.getClassName();
            if (className.contains("net.phadata.center")) {
                String exception = className + "." + traceElement.getMethodName() + "(" + className.substring(className.lastIndexOf(".")).replace(".", "") + ".java:" + traceElement.getLineNumber() + ")";
                if (!exception.contains("$$")) {
                    exceptions.add(exception);
                }
            }
        }
        requestInfo.setExceptionStacks(exceptions);
        log.error("请求失败 ----------------------------------  \n {} \n-----------------------------------------------", JSONObject.toJSONString(requestInfo, true));
    }

    public static Throwable getExceptionType(Exception e) {
        return e;
    }

    private Map<String, Object> getRequestParamsByProceedingJoinPoint(ProceedingJoinPoint point) {
        String[] paramNames = ((MethodSignature) point.getSignature()).getParameterNames();
        Object[] paramValues = point.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> getRequestParamsByJoinPoint(JoinPoint joinPoint) {
        String[] paramNames = ((MethodSignature) joinPoint.getSignature()).getParameterNames();
        Object[] paramValues = joinPoint.getArgs();
        return buildRequestParam(paramNames, paramValues);
    }

    private Map<String, Object> buildRequestParam(String[] paramNames, Object[] paramValues) {
        Map<String, Object> requestParams = new HashMap<>(16);
        for (int i = 0; i < paramNames.length; i++) {
            Object value = paramValues[i];
            if (value instanceof MultipartFile) {
                MultipartFile file = (MultipartFile) value;
                value = file.getOriginalFilename();
            }
            requestParams.put(paramNames[i], value);
        }
        return requestParams;
    }

    @Data
    private static class RequestInfo {
        @JsonProperty(value = "用户ID", index = 0)
        private String userAccount;
        @JsonProperty(value = "用户名", index = 1)
        private String username;
        @JsonProperty(value = "用户IP", index = 2)
        private String ip;
        @JsonProperty(value = "URL", index = 3)
        private String uri;
        @JsonProperty(value = "请求方式", index = 4)
        private String httpMethod;
        @JsonProperty(value = "请求方法", index = 5)
        private String classMethod;
        @JsonProperty(value = "请求参数", index = 6)
        private Object requestParams;
        @JsonProperty(value = "是否成功", index = 7)
        private String success;
        @JsonProperty(value = "执行时间", index = 8)
        private Long executeTime;
        @JsonProperty(value = "异常消息", index = 9)
        private String message;
        @JsonProperty(value = "异常栈", index = 10)
        private List<String> exceptionStacks;
        @JsonProperty(value = "返回结果", index = 11)
        private Object result;
    }
}
