package com.phadata.etdsplus.service.impl;

import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.ApplyAuthDTO;
import com.phadata.etdsplus.entity.dto.ApplyDataDTO;
import com.phadata.etdsplus.mapper.TdaasPrivateKeyMapper;
import com.phadata.etdsplus.service.CustomService;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.utils.result.Result;
import org.springframework.stereotype.Service;

/**
 * 提供接口给定制层的
 *
 * @author linx
 * @since 2021-11-15
 */
@Service
public class CustomServiceImpl implements CustomService {
    private final TdaasPrivateKeyMapper tdaasPrivateKeyMapper;
    private final DTCServerConfig dtcServerConfig;
    private final DTCComponent dtcComponent;

    public CustomServiceImpl(TdaasPrivateKeyMapper tdaasPrivateKeyMapper, DTCServerConfig dtcServerConfig, DTCComponent dtcComponent) {
        this.tdaasPrivateKeyMapper = tdaasPrivateKeyMapper;
        this.dtcServerConfig = dtcServerConfig;
        this.dtcComponent = dtcComponent;
    }

    @Override
    public Result<Boolean> applyAuth(ApplyAuthDTO applyAuth) {
        //TODO 申请授权的逻辑

        return null;
    }

    @Override
    public Result applyData(ApplyDataDTO applyData) {
        //TODO 申请数据的逻辑
        return null;
    }
}
