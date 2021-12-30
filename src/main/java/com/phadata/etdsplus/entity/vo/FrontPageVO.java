package com.phadata.etdsplus.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;

/**
 * 首页vo
 * @author: xionglin
 * @since 2021-12-08 19:14
 */
@Data
@Accessors(chain = true)
public class FrontPageVO {
    private Tj receiving;

    private Tj transmission;

    @Data
    @Accessors(chain = true)
    public static class Tj {
        private List<Tj15>  lineChart;

        /**
         * 总条数
         */
        private Integer total;

        /**
         * 总内存大小
         */
        private Long totalSize;
    }
}
