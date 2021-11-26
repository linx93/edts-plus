package com.phadata.etdsplus.enums;

/**
 * @description: 授权类型
 * @author: linx
 * @create: 2021-11-26 15:45
 */
public enum AuthType {
    REQUEST(0, "授权请求"),
    RESPONSE(1, "授权响应");
    private final int code;
    private final String remark;

    AuthType(int code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public int getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
