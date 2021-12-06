package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.experimental.Accessors;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;

/**
 * @description: DataRequestBizData 数据请求或者控制的DTC BizData
 * @author: linx
 * @create: 2021-11-25 09:38
 */
@Data
@ApiModel(value = "DataRequestBizData 数据请求或者控制的DTC BizData", description = "DataRequestBizData 数据请求或者控制的DTC BizData")
@Accessors(chain = true)
public class ApplyDataDTO {
    @ApiModelProperty(value = "授权DTC的ID", required = true)
    private String dtc;

    @ApiModelProperty(value = "请求序列号", required = true)
    private String serialNumber;

    @ApiModelProperty(value = "// HTTP 请求元数据，如果HttpMeta不为nil，那么它是一个请求，不处理控制包", required = true)
    private HttpMeta httpMeta;

    @ApiModelProperty(value = "Fragment.Wraper() 序列化的数据包" +
            "数据包，这里的包一定是控制包，当HttpMeta为空时，才会认为需要处理数据的控制包" +
            "控制功能包括：部分重传、全部重传、传输完毕确认", required = true)
    private String fragment;

    @ApiModelProperty("向谁请求数据")
    private Address to;

    @ApiModelProperty("请求数据者")
    private Address from;

}
