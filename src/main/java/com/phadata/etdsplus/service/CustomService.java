package com.phadata.etdsplus.service;


import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.utils.result.Result;

/**
 * 提供接口给定制层的
 *
 * @author linx
 * @since 2021-11-15
 */
public interface CustomService {

    /**
     * 申请授权
     *
     * @param applyAuth
     * @return
     */
    Result<Boolean> applyAuth(ApplyAuthDTO applyAuth);


    /**
     * 申请数据
     *
     * @param applyData
     * @return
     */
    Result<Boolean> applyData(ApplyDataDTO applyData);


    /**
     * 接收数据
     *
     * @param responseData
     * @return
     */
    Result<Boolean> receiveData(ResponseDataDTO responseData);

    /**
     * 提供给定制层获取etds的唯一码和数字身份
     *
     * @return
     */
    Result findEtdsInfo();

    /**
     * 接收统计数据
     * @param reportDTO
     * @return result true false
     */
    Result<Boolean> receiveStatisticData(ReportDTO reportDTO);
}
