package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 暂停或恢复某个授权凭证的dto
 * @author: linx
 * @since 2021-11-17 14:36
 */
@Data
public class AuthDtcDTO {
    @ApiModelProperty(value = "凭证id", required = true)
    @NotBlank(message = "凭证id不能为空")
    private String claimId;
}
