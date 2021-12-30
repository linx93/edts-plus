package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * 包含tdaas和etds的标识
 * @author: linx
 * @since 2021-11-26 10:43
 */
@Accessors(chain = true)
@Data
public class Address {
    @ApiModelProperty(value = "tdaas数字身份", required = true)
    @NotBlank(message = "tdaas数字身份不能为空")
    private String tdaas;

    @ApiModelProperty(value = "etds的唯一码", required = true)
    private String etds;
}
