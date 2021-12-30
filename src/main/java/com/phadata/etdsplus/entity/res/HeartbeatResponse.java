package com.phadata.etdsplus.entity.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 心跳响应对象
 * @author: linx
 * @since 2021-11-17 10:37
 */
@Data
public class HeartbeatResponse {


    @ApiModelProperty(value = "tdaas管控etds的状态[0:正常  1:暂停]")
    private String status;

    @ApiModelProperty(value = "etds的唯一码")
    private String EtdsCode;
}
