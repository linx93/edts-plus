package com.phadata.etdsplus.entity.dto;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotBlank;
import java.util.List;
import java.util.Map;

/**
 * 请求的元数据
 * @author: linx
 * @since 2021-12-01 10:00
 */
@Data
public class HttpMeta {

    @ApiModelProperty(value = "请求Path，要结合适配层定义好的应用层地址，一定是'/'开头", required = true)
    private String path;

    @ApiModelProperty(value = "请求Query", required = true)
    private String query;

    @ApiModelProperty(value = "请求方法，只允许GET/POST", required = true)
    private String method;

    @ApiModelProperty(value = "请求头", required = true)
    private Map<String, List<String>> header;

    @ApiModelProperty(value = "请求数据", required = true)
    private String body;
}
