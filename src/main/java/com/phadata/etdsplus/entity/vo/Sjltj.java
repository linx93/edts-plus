package com.phadata.etdsplus.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

/**
 * @description: 用于mybatis返回
 * @author: linx
 * @create: 2021-12-07 17:38
 */
@Data
@Accessors(chain = true)
public  class Sjltj {

    /**
     * 所有和
     */
    private Long size;

    /**
     * 所有和
     */
    private Integer totals;
}
