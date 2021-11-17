package com.phadata.etdsplus.entity.res;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * @description: 心跳响应对象
 * @author: linx
 * @create: 2021-11-17 10:37
 */
@Data
public class HeartbeatResponse {

    @ApiModelProperty(value = "etds的唯一吗")
    private String EtdsCode;

    @ApiModelProperty(value = "tdaas管控etds的状态[0:正常  1:暂停]")
    private String status;
}
