package com.phadata.etdsplus.localcache;

/**
 * @description: 缓存名
 * @author: linx
 * @create: 2021-11-17 11:17
 */
public enum CacheEnum {

    ETDS_STATUS("etdsStatus", "etds受tdaas管理的状态"),
    ETDS("etds", "etds的整体信息"),
    ETDS_CODE("etdsCode", "etds的唯一码");


    private final String code;
    private final String remark;

    CacheEnum(String code, String remark) {
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
