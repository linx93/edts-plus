package com.phadata.etdsplus.entity.vo;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

/**
 * @description: 数据提供者的授权凭证vo
 * @author: linx
 * @create: 2021-12-06 15:11
 */
@Data
public class DataProvideAuthVO {
    @ApiModelProperty(value = "主键id")
    private Integer id;

    @ApiModelProperty(value = "数据授权方数字身份")
    private String authDtid;

    @ApiModelProperty(value = "数据请求方数字身份")
    private String applyDtid;

    @ApiModelProperty(value = "用于标记tdaas控制此授权凭证的状态（0:正常 1:暂停）]")
    private Integer authDtcState;

    @ApiModelProperty(value = "授权凭证的id")
    private String authDtcId;

    @ApiModelProperty(value = "授权详情/描述")
    private String desc;

    @ApiModelProperty(value = "创建时间")
    private Long createTime;

}
