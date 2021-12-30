package com.phadata.etdsplus.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 分页返回数据格式
 * @author: linx
 * @since 2021-12-06 18:27
 */
@Data
@Accessors(chain = true)
public class PageInfo<T> {
    private int current;
    private int size;
    private long total;
    private T records;
}
