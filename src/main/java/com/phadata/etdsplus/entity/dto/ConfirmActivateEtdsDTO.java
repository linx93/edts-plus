package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiParam;
import lombok.Data;

import javax.validation.constraints.NotBlank;

/**
 * @description: 确认激活etds的dto
 * @author: linx
 * @create: 2021-11-17 17:43
 */
@Data
public class ConfirmActivateEtdsDTO {
    @NotBlank(message = "etdsCode不能为空")
    @ApiParam(value = "etds唯一吗", name = "etdsCode", required = true)
    private String etdsCode;
}
