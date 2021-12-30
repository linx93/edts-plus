package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

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
@Accessors(chain = true)
@ApiModel(value = "申请授权的dto/etds通过mq传给授权方tdaas", description = "申请授权的dto/etds通过mq传给授权方tdaas")
public class ApplyAuthBizDataDTO {

    @ApiModelProperty(value = "数据请求方", required = true)
    @Valid
    @NotNull
    private Address from;

    @ApiModelProperty(value = "授权方", required = true)
    @Valid
    @NotNull
    private Address to;

    @ApiModelProperty(value = "授权类型（0:请求 1:响应）", required = true)
    @NotNull
    private Integer authType;

    @ApiModelProperty(value = "数据供应方的list", required = true)
    //@Size(min = 1,message = "cc数组不能为空")
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
