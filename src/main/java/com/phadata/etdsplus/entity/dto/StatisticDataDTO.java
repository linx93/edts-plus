package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @description: 响应的统计数据
 * @author: linx
 * @create: 2021-11-30 14:37
 */
@Data
@ApiModel(value = "响应的统计数据", description = "响应的统计数据")
@Accessors(chain = true)
public class StatisticDataDTO {
    @ApiModelProperty(value = "授权凭证的ID", required = true)
    @NotBlank(message = "授权凭证的ID不能为空")
    private String dtc;

    @ApiModelProperty(value = "请求序列号", required = true)
    @NotBlank(message = "授权凭证的ID不能为空")
    private String serialNumber;

    @ApiModelProperty(value = "对应的请求信息", required = true)
    private HttpMeta httpMeta;

    @ApiModelProperty(value = "数据的大小/单位是byte", required = true)
    private Long dataSize;

    @ApiModelProperty(value = "向谁响应数据", required = true)
    private Address to;

    @ApiModelProperty(value = "响应者", required = true)
    private Address from;

}
