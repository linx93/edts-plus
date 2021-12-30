package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 同步私钥的dto
 * @author: linx
 * @since 2021-11-17 14:36
 */
@Data
public class SyncPrivateKeyDTO {
    @ApiModelProperty(value = "私钥", required = true)
    @NotBlank(message = "私钥不能为空")
    private String privateKey;

    @ApiModelProperty(value = "公钥", required = false)
    //@NotBlank(message = "公司数字身份不能为空")
    private String publicKey;

    @ApiModelProperty(value = "公司数字身份", required = true)
    @NotBlank(message = "公司数字身份不能为空")
    private String companyDtid;

    @ApiModelProperty(value = "安全码", required = true)
    @NotBlank(message = "安全码不能为空")
    private String safeCode;
}
