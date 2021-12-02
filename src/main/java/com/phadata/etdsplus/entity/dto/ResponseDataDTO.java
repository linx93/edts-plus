package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.constraints.NotBlank;

/**
 * @description: 响应数据
 * @author: linx
 * @create: 2021-11-30 14:37
 */
@Data
@ApiModel(value = "数据响应DTC的bizData/数据响应DTC的bizData", description = "数据响应DTC的bizData")
@Accessors(chain = true)
public class ResponseDataDTO {
    @ApiModelProperty(value = "授权DTC的ID", required = true)
    private AuthState dtc;

    @ApiModelProperty(value = "请求序列号", required = true)
    private String serialNumber;

    @ApiModelProperty(value = "整个请求的响应头", required = true)
    private BizHeader bizHeader;

    @ApiModelProperty(value = "响应的数据:这个数据包一定是Chunk，使用Chunk.Wrapper()获得", required = true)
    private String chunk;

    @ApiModelProperty(value = "向谁响应数据")
    private Address to;

    @ApiModelProperty(value = "响应数据者")
    private Address from;

}
