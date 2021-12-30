package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * 修改密码DTO
 * @author: linx
 * @since 2021-11-15 15:36
 */
@Data
@ApiModel(value="登陆/修改密码的DTO对象", description="登陆/修改密码的DTO对象")
public class UpdatePasswordDTO {
    @ApiModelProperty(value = "账户",required = true)
    @NotBlank(message = "账户不能为空")
    private String account;

    @ApiModelProperty(value = "密码",required = true)
    @NotBlank(message = "密码不能为空")
    private String password;


    @ApiModelProperty(value = "老密码",required = true)
    private String oldPassword;
}
