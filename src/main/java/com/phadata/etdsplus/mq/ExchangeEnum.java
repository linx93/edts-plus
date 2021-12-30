package com.phadata.etdsplus.mq;

/**
 * 交换机枚举
 * @author: linx
 * @since 2021-11-17 11:17
 */
public enum ExchangeEnum {

    /**
     * 全局交换机定义
     */
    AUTH_DATA_EXCHANGE("auth-data", "整个授权及数据交互的流程共用的交换机");


    private final String code;
    private final String remark;

    ExchangeEnum(String code, String remark) {
        this.code = code;
        this.remark = remark;
    }

    public String getCode() {
        return code;
    }

    public String getRemark() {
        return remark;
    }
}
