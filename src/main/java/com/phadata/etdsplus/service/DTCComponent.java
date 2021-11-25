package com.phadata.etdsplus.service;


import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;

/**
 * 凭证相关服务
 *
 * @author linx
 * @since 2021-11-15
 */
public interface DTCComponent {

    /**
     * 获取rid
     *
     * @return
     */
    String applyRid();


    /**
     * 创建凭证
     *
     * @param claimReqBizPackage
     * @return
     * @throws Exception
     */
    DTCResponse createDtc(ClaimReqBizPackage claimReqBizPackage) throws Exception;
}
