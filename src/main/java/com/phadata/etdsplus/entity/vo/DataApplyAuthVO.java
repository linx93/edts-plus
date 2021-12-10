package com.phadata.etdsplus.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据请求者的授权凭证vo
 * @author: linx
 * @create: 2021-12-06 15:11
 */
@Data
public class DataApplyAuthVO {
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "数据授权方数字身份")
    private String authDtid;

    @ApiModelProperty(value = "数据授权方名称")
    private String authName;

    @ApiModelProperty(value = "数据提供方数字身份")
    private String provideDtid;

    @ApiModelProperty(value = "数据提供方企业名称")
    private String provideName;

    @ApiModelProperty(value = "数据提供方的ETDS的唯一码")
    private String provideEtdsCode;

    /**
     * 这个是go中定义的授权枚举
     * PENDING   uint8 = iota // 请求中
     * ACCEPTED               // 已授权
     * REJECTED               // 已拒绝
     * COMPLETED              // 已完成
     * REVOKED                // 已撤销
     * PAUSED                 // 已暂停
     */
    @ApiModelProperty(value = "授权凭证的状态[1: 已同意  2:已拒绝 4:已撤销]")
    private Integer authDtcState;

    @ApiModelProperty(value = "授权凭证的id")
    private String authDtcId;

    @ApiModelProperty(value = "授权详情/描述")
    private String desc;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

}
