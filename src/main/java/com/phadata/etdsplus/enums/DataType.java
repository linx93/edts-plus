package com.phadata.etdsplus.enums;

/**
 * @description: 授权类型
 * @author: linx
 * @create: 2021-11-26 15:45
 */
public enum DataType {
    /**
     * 请求
     */
    REQUEST(0, "数据请求"),
    /**
     * 响应
     */
    RESPONSE(1, "数据响应");


    private final int code;


    private final String remark;

    DataType(int code, String remark) {
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
