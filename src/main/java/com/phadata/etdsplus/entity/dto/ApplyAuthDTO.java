package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 申请授权的dto
 * @author: linx
 * @create: 2021-11-25 09:38
 */
@Data
@ApiModel(value = "申请授权的dto", description = "申请授权的dto")
public class ApplyAuthDTO {
    @ApiModelProperty(value = "", required = true)
    @NotBlank(message = "")
    private String xx;
    //TODO 具体需要的属性

}
