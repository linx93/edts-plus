package com.phadata.etdsplus.localcache;

/**
 * @description: 缓存名
 * @author: linx
 * @create: 2021-11-17 11:17
 */
public enum CacheEnum {

    ETDS_STATUS("etds-status", "etds受tdaas管理的状态"),
    ETDS("etds", "etds的唯一码");


    private final String code;
    private final String status;

    CacheEnum(String code, String status) {
        this.code = code;
        this.status = status;
    }

    public String getCode() {
        return code;
    }

    public String getStatus() {
        return status;
    }
}
