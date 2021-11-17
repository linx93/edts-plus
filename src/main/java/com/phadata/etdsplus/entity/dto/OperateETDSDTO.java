package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 操作etds的dto
 * @author: xionglin
 * @create: 2021-11-16 14:12
 */
@Data
@ApiModel(value="操作etds的dto", description="操作etds的dto")
public class OperateETDSDTO {
    @ApiModelProperty(value = "公司数字身份")
    @NotBlank(message = "公司数字身份不能为空")
    private String companyDTID;

    @ApiModelProperty(value = "公司名称")
    @NotBlank(message = "公司名称不能为空")
    private String companyName;

    @ApiModelProperty(value = "etds的唯一code")
    @NotBlank(message = "etds的唯一code不能为空")
    private String etdsCode;
}
