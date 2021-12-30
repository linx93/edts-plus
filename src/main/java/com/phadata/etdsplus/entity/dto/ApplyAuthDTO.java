package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import javax.validation.constraints.NotNull;
import java.util.List;

/**
 * 申请授权的dto
 * @author: linx
 * @since 2021-11-25 09:38
 */
@Data
@ApiModel(value = "申请授权的dto/定制层传给etds的", description = "申请授权的dto/定制层传给etds的")
public class ApplyAuthDTO {

    @ApiModelProperty(value = "授权方", required = true)
    @Valid
    @NotNull
    private Address to;

    @ApiModelProperty(value = "数据供应方的list",required = true)
    private List<Address> cc;

    @ApiModelProperty(value = "描述信息", required = true)
    @NotBlank(message = "描述信息不能为空")
    private String desc;

    @ApiModelProperty(value = "过期时间", required = true)
    @NotNull(message = "过期时间不能为空")
    private Long expiration;

    @ApiModelProperty(value = "序列号", required = true)
    @NotBlank(message = "序列号不能为空")
    private String serialNumber;

}
