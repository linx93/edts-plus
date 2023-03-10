package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 登陆DTO
 * @author: linx
 * @since 2021-11-15 15:36
 */
@Data
@ApiModel(value="登陆/修改密码的DTO对象", description="登陆/修改密码的DTO对象")
public class LoginDTO {
    @ApiModelProperty(value = "账户",required = true)
    @NotBlank(message = "账户不能为空")
    private String account;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;

}
