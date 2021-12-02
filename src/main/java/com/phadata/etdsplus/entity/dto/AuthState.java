package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 响应的授权信息
 * @author: linx
 * @create: 2021-12-01 10:18
 */
@Data
@Accessors(chain = true)
public class AuthState {

    @ApiModelProperty(value = "授权凭证的ID", required = true)
    private String dtc;

    @ApiModelProperty(value = "授权状态码", required = true)
    private int code;

    @ApiModelProperty(value = "授权状态描述", required = true)
    private String desc;
}
