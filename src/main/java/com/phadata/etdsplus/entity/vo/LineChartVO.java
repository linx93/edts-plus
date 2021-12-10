package com.phadata.etdsplus.entity.vo;

import lombok.Data;
import lombok.experimental.Accessors;

import java.util.List;


/**
 * @description: 折线图
 * @author: linx
 * @create: 2021-12-07 09:24
 */
@Data
@Accessors(chain = true)
public class LineChartVO {
    /**
     * {
     * "code": "200000",
     * "message": "操作成功",
     * "payload": {
     * "sjltj": {
     * "size": 0,//总共的MB大小，是浮点数
     * "totals": 0//总共的data_amount大小，是整数
     * },
     * "tj": {
     * "lineChart": [
     * {
     * "size": 0,//总共的MB大小，是浮点数
     * "time": "20211122",
     * "totals": 0//总共的data_amount大小，是整数
     * }
     * ],
     * "total": 0,//所有的total相加的数量，是整数
     * "totalSize": 0//所有的size相加的数量，是浮点数
     * }
     * }
     * }
     */
    private Sjltj sjltj;
    private Tj tj;



    @Data
    @Accessors(chain = true)
    public static class Tj {
        private List<Tj15>  lineChart;

        /**
         * totals的和[15的和]
         */
        private Integer total;

        /**
         * size的和[15的和]
         */
        private Long totalSize;
    }
}
