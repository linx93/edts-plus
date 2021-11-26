package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.enums.AuthType;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.TdaasPrivateKeyMapper;
import com.phadata.etdsplus.mq.MessageConsumerEnum;
import com.phadata.etdsplus.service.CustomService;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.utils.MQSendUtil;
import com.phadata.etdsplus.utils.result.Result;
import net.phadata.identity.common.DTCType;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Map;
import java.util.UUID;

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
    private final EtdsUtil etdsUtil;
    private final EtdsService etdsService;
    private final MQSendUtil mqSendUtil;


    public CustomServiceImpl(TdaasPrivateKeyMapper tdaasPrivateKeyMapper, DTCServerConfig dtcServerConfig, DTCComponent dtcComponent, EtdsUtil etdsUtil, EtdsService etdsService, MQSendUtil mqSendUtil) {
        this.tdaasPrivateKeyMapper = tdaasPrivateKeyMapper;
        this.dtcServerConfig = dtcServerConfig;
        this.dtcComponent = dtcComponent;
        this.etdsUtil = etdsUtil;
        this.etdsService = etdsService;
        this.mqSendUtil = mqSendUtil;
    }

    @Override
    public Result<Boolean> applyAuth(ApplyAuthDTO applyAuth) {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        List<Address> cc = applyAuth.getCc();
        Address to = applyAuth.getTo();
        //1. 本地存储业务

        //2. 构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.OAUTH.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(applyAuth.getExpiration())
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());
        //构建bizData部分
        ApplyAuthBizDataDTO applyAuthBizDataDTO = new ApplyAuthBizDataDTO()
                .setFrom(new Address().setTdaas(etdsInfo.getCompanyDtid()).setEtds(etdsInfo.getEtdsCode()))
                .setTo(to)
                .setCc(applyAuth.getCc())
                .setExpiration(applyAuth.getExpiration())
                .setSerializeNumber(applyAuth.getSerializeNumber())
                .setDesc(applyAuth.getDesc())
                .setAuthType(AuthType.REQUEST.getCode());
        claimReqBizPackage.setBizData(JSON.parseObject(JSON.toJSONString(applyAuthBizDataDTO), Map.class));

        //3. 给数据授权方的tdaas发送MQ消息【流程2】
        if (!cc.isEmpty()) {
            try {
                claimReqBizPackage.setHolder(to.getTdaas());
                //创建凭证
                DTCResponse dtcToAuthTdaas = dtcComponent.createDtc(claimReqBizPackage);
                //这就是凭证
                Map<String, Object> claim = dtcComponent.parse(dtcToAuthTdaas);
                //发送mq
                mqSendUtil.sendToTDaaS(to.getTdaas(), etdsInfo.getEtdsCode(), AuthType.REQUEST.getRemark(), JSON.toJSONString(claim), MessageConsumerEnum.re_etds_to_gr_tdaas_apply);
            } catch (Exception e) {
                throw new BussinessException("创建凭证失败:" + e.getMessage());
            }
        } else {
            throw new BussinessException("cc属性不能空");
        }
        //4. 给自己的tdaas发送MQ消息【流程1】
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            //创建凭证
            DTCResponse dtcToApplyTdaas = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim = dtcComponent.parse(dtcToApplyTdaas);
            //发送mq
            mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), AuthType.REQUEST.getRemark(), JSON.toJSONString(claim), MessageConsumerEnum.re_etds_to_re_tdaas_apply);
        } catch (Exception e) {
            throw new BussinessException("创建凭证失败:" + e.getMessage());
        }
        return Result.success(true);
    }

    @Override
    public Result applyData(ApplyDataDTO applyData) {
        //TODO 申请数据的逻辑
        return null;
    }

    public static void main(String[] args) {

    }
}
