package com.phadata.etdsplus.service;


import com.phadata.etdsplus.entity.dto.ClaimReqBizPackage;
import com.phadata.etdsplus.entity.dto.DTCResponse;

import java.util.Map;

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


    /**
     * 通过创建凭证响应结果，获取出其中的凭证
     *
     * @param dtcResponse
     * @return
     */
    Map<String, Object> parse(DTCResponse dtcResponse);


    /**
     * 注册成为发行方
     *
     * @param etdsService
     * @return
     * @throws Exception
     */
    boolean registerIssuer(EtdsService etdsService);
}
