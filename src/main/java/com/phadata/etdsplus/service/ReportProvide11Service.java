package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.ReportProvide11;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phadata.etdsplus.entity.vo.LineChartVO;
import com.phadata.etdsplus.entity.vo.MoveLogsVO;
import com.phadata.etdsplus.entity.vo.PageInfo;

import java.util.List;

/**
 * <p>
 * 服务类
 * </p>
 *
 * @author linx
 * @since 2021-12-07
 */
public interface ReportProvide11Service extends IService<ReportProvide11> {


    /**
     * 最近15天授权凭证折线图（数据提供方）
     *
     * @param days
     * @param authDtcId
     * @return
     */
    LineChartVO dataProvideLine(Integer days, String authDtcId);

    /**
     * 流转日志（数据供应方）
     *
     * @param page
     * @param size
     * @param authDtcId
     * @return
     */

    PageInfo<List<ReportProvide11>> provideMoveLogs(Integer page, Integer size, String authDtcId);


}
