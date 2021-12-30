package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * etds注册的dto
 * @author: linx
 * @since 2021-11-16 16:30
 */
@Data
public class ETDSRegisterDTO {
    @ApiModelProperty(value = "激活码", required = true)
    @NotBlank(message = "激活码不能为空")
    private String activationCode;


    @ApiModelProperty(value = "etds的地址/可以是域名也可以是http://ip:port[http://192.168.1.44:18001]", required = true)
    @NotBlank(message = "etds的地址不能为空")
    private String etdsUrl;
}
