package com.phadata.etdsplus.utils.result;

import lombok.Getter;
import lombok.Setter;

/**
 * @Author linx
 * @Classname ResultVO
 * @Description 统一返回数据的VO
 * @Date 2019/12/20 18:03
 */
@Setter
@Getter
public class Result<T> {
    private String code;
    private String message;
    private T payload;

    protected Result() {

    }

    protected Result(String code, String message, T payload) {
        this.code = code;
        this.message = message;
        this.payload = payload;
    }


    /**
     * 成功返回结果
     *
     * @param payload 获取的数据
     */
    public static <T> Result<T> success(T payload) {
        return success(ResultCodeMessage.SUCCESS, payload);
    }

    /**
     * 成功返回结果
     */
    public static <T> Result<T> success() {
        return new Result(ResultCodeMessage.SUCCESS.getCode(), ResultCodeMessage.SUCCESS.getMessage(), null);
    }

    /**
     * 成功返回结果
     *
     * @param payload    获取的数据
     * @param message 提示信息
     */
    public static <T> Result<T> success(T payload, String message) {
        return new Result(ResultCodeMessage.SUCCESS.getCode(), message, payload);
    }

    /**
     * 失败返回结果
     *
     * @param codeMessage 错误码
     */
    public static <T> Result<T> success(ICodeMessage codeMessage, T payload) {
        return new Result(codeMessage.getCode(), codeMessage.getMessage(), payload);
    }

    /**
     * 失败返回结果
     *
     * @param codeMessage 错误码
     */
    public static <T> Result<T> failed(ICodeMessage codeMessage) {
        return new Result(codeMessage.getCode(), codeMessage.getMessage(), null);
    }

    /**
     * 失败返回结果
     *
     * @param codeMessage 错误码
     */
    public static <T> Result<T> failed(ICodeMessage codeMessage,T payload) {
        return new Result(codeMessage.getCode(), codeMessage.getMessage(), payload);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message) {
        return new Result(ResultCodeMessage.FAILED.getCode(), message, null);
    }

    /**
     * 失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> failed(String message, T payload) {
        return new Result(ResultCodeMessage.FAILED.getCode(), message, payload);
    }

    /**
     * 参数验证失败返回结果
     *
     * @param message 提示信息
     */
    public static <T> Result<T> validateFailed(String message) {
        return new Result(ResultCodeMessage.VALIDATE_FAILED.getCode(), message, null);
    }

    /**
     * 未登录返回结果
     */
    public static <T> Result<T> unauthorized(T payload) {
        return new Result(ResultCodeMessage.UNAUTHORIZED.getCode(), ResultCodeMessage.UNAUTHORIZED.getMessage(), payload);
    }

    /**
     * 未授权返回结果
     */
    public static <T> Result<T> forbidden(T payload) {
        return new Result(ResultCodeMessage.FORBIDDEN.getCode(), ResultCodeMessage.FORBIDDEN.getMessage(), payload);
    }

    /**
     * 失败返回结果
     */
    public static <T> Result<T> failed() {
        return failed(ResultCodeMessage.FAILED);
    }

    /**
     * 参数验证失败返回结果
     */
    public static <T> Result<T> validateFailed() {
        return failed(ResultCodeMessage.VALIDATE_FAILED);
    }

    @Override
    public String toString() {
        return "ResultVO{" +
                "code=" + code +
                ", message='" + message + '\'' +
                ", payload=" + payload +
                '}';
    }

}
