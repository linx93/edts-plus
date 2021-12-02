package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.constant.SystemConstant;
import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.GrantResultApply4;
import com.phadata.etdsplus.entity.po.ReAuthNoticeApply1;
import com.phadata.etdsplus.entity.po.ReAuthNoticeApply2;
import com.phadata.etdsplus.enums.AuthType;
import com.phadata.etdsplus.enums.DataType;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.TdaasPrivateKeyMapper;
import com.phadata.etdsplus.mq.MessageConsumerEnum;
import com.phadata.etdsplus.service.*;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.mq.MQSendUtil;
import com.phadata.etdsplus.utils.result.Result;
import lombok.extern.slf4j.Slf4j;
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
@Slf4j
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
    private final GrantResultApply4Service grantResultApply4Service;

    public CustomServiceImpl(TdaasPrivateKeyMapper tdaasPrivateKeyMapper, DTCServerConfig dtcServerConfig, DTCComponent dtcComponent, EtdsUtil etdsUtil, EtdsService etdsService, MQSendUtil mqSendUtil, ReAuthNoticeApply1Service reAuthNoticeApply1Service, ReAuthNoticeApply2Service reAuthNoticeApply2Service, GrantResultApply4Service grantResultApply4Service) {
        this.tdaasPrivateKeyMapper = tdaasPrivateKeyMapper;
        this.dtcServerConfig = dtcServerConfig;
        this.dtcComponent = dtcComponent;
        this.etdsUtil = etdsUtil;
        this.etdsService = etdsService;
        this.mqSendUtil = mqSendUtil;
        this.reAuthNoticeApply1Service = reAuthNoticeApply1Service;
        this.reAuthNoticeApply2Service = reAuthNoticeApply2Service;
        this.grantResultApply4Service = grantResultApply4Service;
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
    public Result<Boolean> applyData(ApplyDataDTO applyData) {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        //获取授权凭证具体信息
        String dtcId = applyData.getDtc();
        ResponseAuthDTO responseAuthDTO = getResponseAuthDTOByDtcId(dtcId);
        //检验此凭证是否被tdaas关闭
        //构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.DATA.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(SystemConstant.EXPIRED)
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());

        //1. 给自己的tdaas发送MQ消息【流程7】
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            // 构建设置bizData7的内容
            Map<String, Object> bizData7 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData7);
            //创建凭证
            DTCResponse dtcResponse7 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim7 = dtcComponent.parse(dtcResponse7);
            //发送mq
            mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim7), MessageConsumerEnum.re_etds_to_re_tdaas_data);
        } catch (Exception e) {
            throw new BussinessException("【流程7】创建凭证失败:" + e.getMessage());
        }

        //2. 给数据供应方TDaaS发送MQ消息【流程8】
        try {
            claimReqBizPackage.setHolder(responseAuthDTO.getCc().get(0).getTdaas());
            // 构建设置bizData8的内容
            Map<String, Object> bizData8 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData8);
            //创建凭证
            DTCResponse dtcResponse8 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim8 = dtcComponent.parse(dtcResponse8);
            //发送mq
            mqSendUtil.sendToTDaaS(responseAuthDTO.getCc().get(0).getTdaas(), responseAuthDTO.getCc().get(0).getEtds(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim8), MessageConsumerEnum.re_etds_to_pr_tdaas_data);
        } catch (Exception e) {
            throw new BussinessException("【流程8】创建凭证失败:" + e.getMessage());
        }
        //3. 给数据供应方ETDS发送MQ消息【流程9】
        try {
            claimReqBizPackage.setHolder(responseAuthDTO.getCc().get(0).getTdaas());
            // 构建设置bizData9的内容
            Map<String, Object> bizData9 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData9);
            //创建凭证
            DTCResponse dtcResponse9 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim9 = dtcComponent.parse(dtcResponse9);
            //发送mq
            mqSendUtil.sendToETDS(responseAuthDTO.getCc().get(0).getTdaas(), responseAuthDTO.getCc().get(0).getEtds(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim9), responseAuthDTO.getCc().get(0).getEtds(), MessageConsumerEnum.re_etds_to_pr_etds_data);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }
        //todo 4. 本地存储业务

        return Result.success(true);
    }

    @Override
    public Result<Boolean> receiveData(ResponseDataDTO responseData) {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        //获取授权凭证具体信息
        String dtcId = responseData.getDtc().getDtc();
        ResponseAuthDTO responseAuthDTO = getResponseAuthDTOByDtcId(dtcId);
        //TODO 响应数据的逻辑
        //构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.DATA.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(SystemConstant.EXPIRED)
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());
        //构建bizData部分
        //1. 给自己的tdaas发送MQ消息【流程13】
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            // 构建设置bizData13的内容
            //todo 根据具体的需求填入bizData数据
            Map<String, Object> bizData13 = JSON.parseObject(JSON.toJSONString(responseData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData13);
            //创建凭证
            DTCResponse dtcResponse13 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim13 = dtcComponent.parse(dtcResponse13);
            //发送mq
            mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim13), MessageConsumerEnum.pr_etds_to_pr_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }

        //2. 给数据授权方的tdaas发送MQ消息【流程12】
        try {
            claimReqBizPackage.setHolder(responseAuthDTO.getCc().get(0).getTdaas());
            // 构建设置bizData12的内容
            //todo 根据具体的需求填入bizData数据
            Map<String, Object> bizData12 = JSON.parseObject(JSON.toJSONString(responseData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData12);
            //创建凭证
            DTCResponse dtcResponse12 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim12 = dtcComponent.parse(dtcResponse12);
            //发送mq
            mqSendUtil.sendToTDaaS(responseAuthDTO.getCc().get(0).getTdaas(), responseAuthDTO.getCc().get(0).getEtds(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim12), MessageConsumerEnum.pr_etds_to_gr_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }

        //3. 给数据请求方的tdaas发送MQ消息【流程10】
        try {
            claimReqBizPackage.setHolder(responseData.getTo().getTdaas());
            // 构建设置bizData10的内容
            //todo 根据具体的需求填入bizData数据
            Map<String, Object> bizData10 = JSON.parseObject(JSON.toJSONString(responseData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData10);
            //创建凭证
            DTCResponse dtcResponse10 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim10 = dtcComponent.parse(dtcResponse10);
            //发送mq
            mqSendUtil.sendToTDaaS(responseData.getTo().getTdaas(), etdsInfo.getEtdsCode(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim10), MessageConsumerEnum.pr_etds_to_re_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }

        //4. 给数据请求方的ETDS发送MQ消息【流程11】
        try {
            claimReqBizPackage.setHolder(responseAuthDTO.getFrom().getTdaas());
            // 构建设置bizData11的内容
            //todo 根据具体的需求填入bizData数据
            Map<String, Object> bizData11 = JSON.parseObject(JSON.toJSONString(responseData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData11);
            //创建凭证
            DTCResponse dtcResponse11 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim11 = dtcComponent.parse(dtcResponse11);
            //发送mq
            mqSendUtil.sendToETDS(responseData.getTo().getTdaas(), etdsInfo.getEtdsCode(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim11), responseData.getTo().getEtds(), MessageConsumerEnum.pr_etds_to_re_etds_data);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }
        //todo 5. 本地存储业务

        return Result.success(true);
    }

    @Override
    public Result findEtdsInfo() {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        Map<String, String> result = new HashMap<>(8);
        result.put("etdsCode",etdsInfo.getEtdsCode());
        result.put("dtid",etdsInfo.getCompanyDtid());
        return Result.success(result);
    }


    /**
     * 根据凭证id查询授权凭证内容
     *
     * @param dtcId
     * @return
     */
    private ResponseAuthDTO getResponseAuthDTOByDtcId(String dtcId) {
        GrantResultApply4 one = grantResultApply4Service.getOne(new QueryWrapper<GrantResultApply4>().lambda().eq(GrantResultApply4::getClaimId, dtcId));
        if (one == null) {
            throw new BussinessException("没有查询到对应的授权凭证   " + "  [dtc=" + dtcId + "]");
        }
        //获取授权凭证
        String grantDocument = one.getGrantDocument();
        log.info("授权凭证:{}", grantDocument);
        //授权凭证
        VerifiableClaim vc = JSON.parseObject(grantDocument, VerifiableClaim.class);
        //授权凭证红中bizData的内容
        ResponseAuthDTO responseAuthDTO = JSON.parseObject(JSON.toJSONString(vc.getCredentialSubject().getBizData()), ResponseAuthDTO.class);

        return responseAuthDTO;
    }

}
