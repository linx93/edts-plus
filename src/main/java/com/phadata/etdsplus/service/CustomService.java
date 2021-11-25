package com.phadata.etdsplus.service;


import com.phadata.etdsplus.entity.dto.ApplyAuthDTO;
import com.phadata.etdsplus.entity.dto.ApplyDataDTO;
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
    Result applyData(ApplyDataDTO applyData);
}
