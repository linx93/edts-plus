package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description: 整个请求的响应头, 在发送数据之前，先发送响应头
 * 有两个目的：
 * 一是把HTTP请求的响应头和数据隔离，方便数据分包
 * 二是传输数据分片的响应控制包，让收到这个包的客户端知道如何分配内存
 * @author: linx
 * @create: 2021-12-01 10:23
 */
@Data
public class BizHeader {
    private Map<String, List<String>> header;

    private int status;

    @ApiModelProperty(value = "// Fragment.Wraper() 序列化的数据包，// 这个包一定是数据包的头包，应为控制包没有HTTP请求", required = true)
    private String fragment;
}
