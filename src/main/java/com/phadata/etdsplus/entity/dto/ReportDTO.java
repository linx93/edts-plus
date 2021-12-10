package com.phadata.etdsplus.entity.dto;

import lombok.Data;

import java.util.List;
import java.util.Map;

/**
 * @description: 接收定制层的统计数据dto
 * @author: linx
 * @create: 2021-12-07 10:48
 */
@Data
public class ReportDTO {
    /**
     * 请求时间
     */
    private Long requestedAt;

    /**
     * 首次响应时间
     */
    private Long firstResponse;

    /**
     * 最后响应时间
     */
    private Long lastResponse;

    /**
     * 请求序列号
     */
    private String serialNumber;

    /**
     * 数据请求方
     */
    private Address from;

    /**
     * 数据提供方
     */
    private Address to;

    /**
     * 请求HTTP要素
     */
    private HttpMeta requestHttpMeta;

    /**
     * 授权凭证id
     */
    private String authDtc;


    /**
     * 重试次数
     */
    private Integer retries;

    /**
     * 授权状态码
     */
    private Integer authStatus;

    /**
     * 授权状态描述
     */
    private String authStatusDesc;

    /**
     * 响应HTTP要素
     */
    private ResponseHttpMeta responseHttpMeta;

    /**
     * 分片大小,每片的大小单位b
     */

    private Long chunkSize;
    /**
     * 分片数量
     */
    private Integer chunkLength;


    @Data
    public static class ResponseHttpMeta {
        /**
         * 响应头
         */
        private Map<String, List<String>> header;

        /**
         * 状态码
         */
        private Integer status;
        /**
         * 数据大小
         */
        private Long contentLength;
    }
}
