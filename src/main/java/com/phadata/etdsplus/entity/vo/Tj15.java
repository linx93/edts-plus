package com.phadata.etdsplus.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 用于接收mybatis返回
 * @author: linx
 * @create: 2021-12-07 17:17
 */
@Data
@Accessors(chain = true)
public class Tj15 {
    /**
     * 数据分片加起来的大小
     */
    private Long size;

    /**
     * yyyy-MM-dd格式的时间字符串
     */
    private String time;

    /**
     * 分片数量，如果size=0的话totals就为0
     */
    private Integer totals;
}
