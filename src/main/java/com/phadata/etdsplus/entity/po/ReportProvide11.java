package com.phadata.etdsplus.entity.po;

import com.baomidou.mybatisplus.annotation.IdType;
import com.baomidou.mybatisplus.annotation.TableId;
import com.baomidou.mybatisplus.annotation.TableField;

import java.io.Serializable;
import java.util.Date;

import com.baomidou.mybatisplus.annotation.TableName;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.experimental.Accessors;

/**
 * <p>
 *
 * </p>
 *
 * @author linx
 * @since 2021-12-07
 */
@Data
@EqualsAndHashCode(callSuper = false)
@ApiModel(value = "ReportProvide11对象", description = "")
@TableName("report_provide_11")
@Accessors(chain = true)
public class ReportProvide11 implements Serializable {

    private static final long serialVersionUID = 1L;

    @TableId(value = "id", type = IdType.AUTO)
    private Integer id;

    @ApiModelProperty(value = "凭证id")
    private String claimId;

    @ApiModelProperty(value = "请求时间")
    private Long requestedAt;

    @ApiModelProperty(value = "首次响应时间")
    private Long firstResponse;

    @ApiModelProperty(value = "最后响应时间")
    private Long lastResponse;

    @ApiModelProperty(value = "请求序列号")
    private String serialNumber;

    @ApiModelProperty(value = "请求方数字身份")
    private String fromTdaas;

    @ApiModelProperty(value = "请求方etds唯一码")
    private String fromEtds;

    @ApiModelProperty(value = "响应方数字身份")
    private String toTdaas;

    @ApiModelProperty(value = "响应方etds唯一码")
    private String toEtds;

    @ApiModelProperty(value = "请求Path")
    private String requestHttpMetaPath;

    @ApiModelProperty(value = "请求Query")
    private String requestHttpMetaQuery;

    @ApiModelProperty(value = "请求方法，只允许GET/POST")
    private String requestHttpMetaMethod;

    @ApiModelProperty(value = "请求头")
    private String requestHttpMetaHeader;

    @ApiModelProperty(value = "请求body的数据")
    private String requestHttpMetaBody;

    @ApiModelProperty(value = "授权凭证id")
    private String authDtc;

    @ApiModelProperty(value = "重试次数")
    private Integer retries;

    @ApiModelProperty(value = "授权状态码")
    private Integer authStatus;

    @ApiModelProperty(value = "授权状态描述")
    private String authStatusDesc;

    @ApiModelProperty(value = "响应头")
    private String responseHttpMetaHeader;

    @ApiModelProperty(value = "响应状态码")
    private Integer responseHttpMetaStatus;

    @ApiModelProperty(value = "响应数据大小,单位是byte")
    @TableField("response_http_meta_contentLength")
    private Long responseHttpMetaContentLength;

    @ApiModelProperty(value = "分片大小,每片的大小单位b")
    private Long chunkSize;

    @ApiModelProperty(value = "分片的数量")
    private Integer chunkLength;

    private Date createTime;


}
