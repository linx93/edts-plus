package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.ReportApply11;
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
public interface ReportApply11Service extends IService<ReportApply11> {
    /**
     * 最近15天授权凭证折线图（数据请求方）
     *
     * @param days
     * @param authDtcId
     * @return
     */
    LineChartVO dataApplyLine(Integer days, String authDtcId);

    /**
     * 流转日志（数据请求方）
     *
     * @param page
     * @param size
     * @param authDtcId 授权凭证id
     * @return
     */
    PageInfo<List<MoveLogsVO>> applyMoveLogs(Integer page, Integer size, String authDtcId);
}
