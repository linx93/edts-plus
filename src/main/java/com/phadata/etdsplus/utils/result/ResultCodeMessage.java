package com.phadata.etdsplus.utils.result;

/**
 * @Author linx
 * @Classname ResultCodeMessage
 * * @Description 枚举了一些常用API操作码
 * * @Date 2019/12/24 08:58
 */

public enum ResultCodeMessage implements ICodeMessage {
    SUCCESS("200000", "操作成功!"),
    FAILED("500000", "操作失败!"),
    VALIDATE_FAILED("400002", "参数检验失败!"),
    UNAUTHORIZED("400001", "暂未登录或token已经过期!"),
    FORBIDDEN("400003", "没有相关权限!");
    /**
     *
     */
    private String code;
    /**
     *
     */
    private String message;

    /**
     * 枚举类的构造器默认就是私有的
     *
     * @param code
     * @param message
     */
    ResultCodeMessage(String code, String message) {
        this.code = code;
        this.message = message;
    }


    @Override
    public String getCode() {
        return code;
    }


    @Override
    public String getMessage() {
        return message;
    }

    @Override
    public String toString() {
        return "ResultCodeMessage{" +
                "code=" + code +
                ", message='" + message + '\'' +
                '}';
    }
}
