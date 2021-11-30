package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.ReAuthNoticeApply1;
import com.phadata.etdsplus.entity.po.ReAuthNoticeApply2;
import com.phadata.etdsplus.enums.AuthType;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.TdaasPrivateKeyMapper;
import com.phadata.etdsplus.mq.MessageConsumerEnum;
import com.phadata.etdsplus.service.*;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.utils.MQSendUtil;
import com.phadata.etdsplus.utils.result.Result;
import net.phadata.identity.common.DTCType;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.HashMap;
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
    private final ReAuthNoticeApply1Service reAuthNoticeApply1Service;
    private final ReAuthNoticeApply2Service reAuthNoticeApply2Service;

    public CustomServiceImpl(TdaasPrivateKeyMapper tdaasPrivateKeyMapper, DTCServerConfig dtcServerConfig, DTCComponent dtcComponent, EtdsUtil etdsUtil, EtdsService etdsService, MQSendUtil mqSendUtil, ReAuthNoticeApply1Service reAuthNoticeApply1Service, ReAuthNoticeApply2Service reAuthNoticeApply2Service) {
        this.tdaasPrivateKeyMapper = tdaasPrivateKeyMapper;
        this.dtcServerConfig = dtcServerConfig;
        this.dtcComponent = dtcComponent;
        this.etdsUtil = etdsUtil;
        this.etdsService = etdsService;
        this.mqSendUtil = mqSendUtil;
        this.reAuthNoticeApply1Service = reAuthNoticeApply1Service;
        this.reAuthNoticeApply2Service = reAuthNoticeApply2Service;
    }

    @Override
    public Result<Boolean> applyAuth(ApplyAuthDTO applyAuth) {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        //数据提供方list
        List<Address> cc = applyAuth.getCc();
        //授权方
        Address to = applyAuth.getTo();

        //1. 构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.OAUTH.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(applyAuth.getExpiration())
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());
        //2. 构建bizData部分
        ApplyAuthBizDataDTO applyAuthBizDataDTO = new ApplyAuthBizDataDTO()
                .setFrom(new Address().setTdaas(etdsInfo.getCompanyDtid()).setEtds(etdsInfo.getEtdsCode()))
                .setTo(to)
                .setCc(applyAuth.getCc())
                .setExpiration(applyAuth.getExpiration())
                .setSerialNumber(applyAuth.getSerialNumber())
                .setDesc(applyAuth.getDesc())
                .setAuthType(AuthType.REQUEST.getCode());
        claimReqBizPackage.setBizData(JSON.parseObject(JSON.toJSONString(applyAuthBizDataDTO), Map.class));
        Map<String, Object> claim2;
        //3. 给数据授权方的tdaas发送MQ消息【流程2】
        if (!cc.isEmpty()) {
            try {
                claimReqBizPackage.setHolder(to.getTdaas());
                //创建凭证
                DTCResponse dtcToAuthTdaas = dtcComponent.createDtc(claimReqBizPackage);
                //这就是凭证
                claim2 = dtcComponent.parse(dtcToAuthTdaas);
                //发送mq
                mqSendUtil.sendToTDaaS(to.getTdaas(), etdsInfo.getEtdsCode(), AuthType.REQUEST.getRemark(), JSON.toJSONString(claim2), MessageConsumerEnum.re_etds_to_gr_tdaas_apply);
            } catch (Exception e) {
                throw new BussinessException("创建凭证失败:" + e.getMessage());
            }
        } else {
            throw new BussinessException("cc[数据供应方的list]属性不能为空");
        }
        //4. 给自己的tdaas发送MQ消息【流程1】
        Map<String, Object> claim1;
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            //创建凭证
            DTCResponse dtcToApplyTdaas = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            claim1 = dtcComponent.parse(dtcToApplyTdaas);
            //发送mq
            mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), AuthType.REQUEST.getRemark(), JSON.toJSONString(claim1), MessageConsumerEnum.re_etds_to_re_tdaas_apply);
        } catch (Exception e) {
            throw new BussinessException("创建凭证失败:" + e.getMessage());
        }
        //5. 本地存储业务
        long epochSecond = Instant.now().getEpochSecond();
        // 保存发送给数据请求方TDaaS的数据
        reAuthNoticeApply1Service.save(new ReAuthNoticeApply1()
                .setApplyEtdsUuid(etdsInfo.getEtdsCode())
                .setApplyDtid(etdsInfo.getCompanyDtid())
                .setApplyTime(epochSecond)
                .setCreatedTime(epochSecond)
                .setGrantDtid(to.getTdaas())
                .setToDtid(cc.get(0).getTdaas())
                .setToEtdsUuid(cc.get(0).getEtds())
                .setSerialNumber(applyAuth.getSerialNumber())
                //TODO 这里的通知详情需要确认放什么
                .setNoticeDetails(String.format("数据请求方[%s]的ETDS服务[%s]向数据授权方[%s]发起了授权请求，数据供应方为:[%s]"
                        , etdsInfo.getCompanyDtid()
                        , etdsInfo.getEtdsCode(), to.getTdaas(), cc.get(0).getTdaas() + ":" + cc.get(0).getEtds()))
                .setDtcDocument(JSON.toJSONString(claim1))
                .setClaimId(JSON.parseObject(JSON.toJSONString(claim1), VerifiableClaim.class).getId()));
        // 保存发送给数据授权方TDaaS的数据
        reAuthNoticeApply2Service.save(new ReAuthNoticeApply2()
                .setApplyDtid(etdsInfo.getCompanyDtid())
                .setFromEtdsUuid(etdsInfo.getEtdsCode())
                .setCreatedTime(epochSecond)
                .setOperatedTime(epochSecond)
                .setSerialNumber(applyAuth.getSerialNumber())
                .setToDtid(cc.get(0).getTdaas())
                .setToEtdsUuid(cc.get(0).getEtds())
                //TODO 这里的通知详情需要确认放什么
                .setNoticeDetails(String.format("数据请求方[%s]的ETDS服务[%s]向数据授权方[%s]发起了授权请求，数据供应方为:[%s]"
                        , etdsInfo.getCompanyDtid()
                        , etdsInfo.getEtdsCode(), to.getTdaas(), cc.get(0).getTdaas() + ":" + cc.get(0).getEtds()))
                .setDtcDocument(JSON.toJSONString(claim2))
                .setClaimId(JSON.parseObject(JSON.toJSONString(claim2), VerifiableClaim.class).getId()));
        return Result.success(true);
    }

    @Override
    public Result applyData(ApplyDataDTO applyData) {
        //TODO 申请数据的逻辑
        //1. 给自己的tdaas发送MQ消息【流程7】
        //2. 给数据供应方TDaaS发送MQ消息【流程8】
        //3. 给数据供应方ETDS发送MQ消息【流程9】
        //4. 本地存储业务

        return null;
    }

    @Override
    public Result responseData(ResponseDataDTO responseData) {
        //TODO 响应数据的逻辑
        return null;
    }


}
