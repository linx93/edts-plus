package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.config.DTCServerConfig;
import com.phadata.etdsplus.constant.SystemConstant;
import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.entity.po.*;
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
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Service;

import javax.validation.Valid;
import javax.validation.constraints.NotNull;
import java.time.Instant;
import java.util.*;

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
    private final DTIDComponent dtidComponent;
    private final EtdsUtil etdsUtil;
    private final EtdsService etdsService;
    private final MQSendUtil mqSendUtil;
    private final ReAuthNoticeApply1Service reAuthNoticeApply1Service;
    private final ReAuthNoticeApply2Service reAuthNoticeApply2Service;
    private final GrantResultApply4Service grantResultApply4Service;
    private final GrantResultProvide6Service grantResultProvide6Service;
    private final ReportProvide11Service reportProvide11Service;

    public CustomServiceImpl(TdaasPrivateKeyMapper tdaasPrivateKeyMapper, DTCServerConfig dtcServerConfig, DTCComponent dtcComponent, DTIDComponent dtidComponent, EtdsUtil etdsUtil, EtdsService etdsService, MQSendUtil mqSendUtil, ReAuthNoticeApply1Service reAuthNoticeApply1Service, ReAuthNoticeApply2Service reAuthNoticeApply2Service, GrantResultApply4Service grantResultApply4Service, GrantResultProvide6Service grantResultProvide6Service, ReportProvide11Service reportProvide11Service) {
        this.tdaasPrivateKeyMapper = tdaasPrivateKeyMapper;
        this.dtcServerConfig = dtcServerConfig;
        this.dtcComponent = dtcComponent;
        this.dtidComponent = dtidComponent;
        this.etdsUtil = etdsUtil;
        this.etdsService = etdsService;
        this.mqSendUtil = mqSendUtil;
        this.reAuthNoticeApply1Service = reAuthNoticeApply1Service;
        this.reAuthNoticeApply2Service = reAuthNoticeApply2Service;
        this.grantResultApply4Service = grantResultApply4Service;
        this.grantResultProvide6Service = grantResultProvide6Service;
        this.reportProvide11Service = reportProvide11Service;
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
        ResponseAuthDTO responseAuthDTO = getResponseAuthDTOByDtcIdApply(dtcId);
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
        Map<String, Object> claim7;
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            // 构建设置bizData7的内容
            Map<String, Object> bizData7 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData7);
            //创建凭证
            DTCResponse dtcResponse7 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            claim7 = dtcComponent.parse(dtcResponse7);
        } catch (Exception e) {
            throw new BussinessException("【流程7】创建凭证失败:" + e.getMessage());
        }

        //2. 给数据供应方TDaaS发送MQ消息【流程8】
        Map<String, Object> claim8;
        try {
            claimReqBizPackage.setHolder(applyData.getTo().getTdaas());
            // 构建设置bizData8的内容
            Map<String, Object> bizData8 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData8);
            //创建凭证
            DTCResponse dtcResponse8 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            claim8 = dtcComponent.parse(dtcResponse8);
        } catch (Exception e) {
            throw new BussinessException("【流程8】创建凭证失败:" + e.getMessage());
        }
        //3. 给数据供应方ETDS发送MQ消息【流程9】
        Map<String, Object> claim9;
        try {
            claimReqBizPackage.setHolder(applyData.getTo().getTdaas());
            // 构建设置bizData9的内容
            Map<String, Object> bizData9 = JSON.parseObject(JSON.toJSONString(applyData), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData9);
            //创建凭证
            DTCResponse dtcResponse9 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            claim9 = dtcComponent.parse(dtcResponse9);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }
        //发送mq
        mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim7), MessageConsumerEnum.re_etds_to_re_tdaas_data);
        //发送mq
        mqSendUtil.sendToTDaaS(applyData.getTo().getTdaas(), applyData.getTo().getEtds(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim8), MessageConsumerEnum.re_etds_to_pr_tdaas_data);
        //发送mq
        mqSendUtil.sendToETDS(applyData.getTo().getTdaas(), applyData.getTo().getEtds(), DataType.REQUEST.getRemark(), JSON.toJSONString(claim9), applyData.getTo().getEtds(), MessageConsumerEnum.re_etds_to_pr_etds_data);
        //todo 4. 本地存储业务

        return Result.success(true);
    }

    @Override
    public Result<Boolean> receiveData(ResponseDataDTO responseData) {
        log.info("定制层");
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        //获取授权凭证具体信息
        String dtcId = responseData.getDtc().getDtc();
        ResponseAuthDTO responseAuthDTO = getResponseAuthDTOByDtcIdProvide(dtcId);
        //构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.DATA.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(SystemConstant.EXPIRED)
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());

        //4. 给数据请求方的ETDS发送MQ消息【这里发送的数据体】【流程11】
        try {
            claimReqBizPackage.setHolder(responseData.getTo().getTdaas());
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

        //
        return Result.success(true);
    }

    @Override
    public Result findEtdsInfo() {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        Map<String, String> result = new HashMap<>(8);
        result.put("etdsCode", etdsInfo.getEtdsCode());
        result.put("dtid", etdsInfo.getCompanyDtid());
        return Result.success(result);
    }

    @Override
    public Result receiveStatisticData(ReportDTO reportDTO) {
        Etds etdsInfo = etdsUtil.EtdsInfo(etdsService);
        //获取授权凭证具体信息
        String dtcId = reportDTO.getAuthDtc();
        ResponseAuthDTO responseAuthDTO = getResponseAuthDTOByDtcIdProvide(dtcId);
        //数据授权方
        Address auth = responseAuthDTO.getFrom();
        //数据请求方
        Address request = reportDTO.getFrom();
        //数据提供方
        Address provide = reportDTO.getTo();
        //构建创建凭证的参数ClaimReqBizPackage
        ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                .setType(DTCType.OTHER.getType())
                .setTdrType("-1")
                .setTimes(0)
                .setExpire(SystemConstant.EXPIRED)
                .setUnionId(UUID.randomUUID().toString())
                .setIssuer(etdsInfo.getCompanyDtid());
        //构建bizData部分
        //1. 给自己的tdaas发送MQ消息【流程13】
        //这就是凭证
        Map<String, Object> claim13;
        try {
            claimReqBizPackage.setHolder(etdsInfo.getCompanyDtid());
            // 构建设置bizData13的内容
            Map<String, Object> bizData13 = JSON.parseObject(JSON.toJSONString(reportDTO), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData13);
            //创建凭证
            DTCResponse dtcResponse13 = dtcComponent.createDtc(claimReqBizPackage);
            claim13 = dtcComponent.parse(dtcResponse13);
            //发送mq
            mqSendUtil.sendToTDaaS(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim13), MessageConsumerEnum.pr_etds_to_pr_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }

        //2. 给数据授权方的tdaas发送MQ消息【流程12】
        try {
            claimReqBizPackage.setHolder(auth.getTdaas());
            // 构建设置bizData12的内容
            Map<String, Object> bizData12 = JSON.parseObject(JSON.toJSONString(reportDTO), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData12);
            //创建凭证
            DTCResponse dtcResponse12 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim12 = dtcComponent.parse(dtcResponse12);
            //发送mq
            mqSendUtil.sendToTDaaS(auth.getTdaas(), auth.getEtds(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim12), MessageConsumerEnum.pr_etds_to_gr_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }

        //3. 给数据请求方的tdaas发送MQ消息【流程10】
        try {
            claimReqBizPackage.setHolder(request.getTdaas());
            // 构建设置bizData10的内容
            Map<String, Object> bizData10 = JSON.parseObject(JSON.toJSONString(reportDTO), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData10);
            //创建凭证
            DTCResponse dtcResponse10 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim10 = dtcComponent.parse(dtcResponse10);
            //发送mq
            mqSendUtil.sendToTDaaS(request.getTdaas(), request.getEtds(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim10), MessageConsumerEnum.pr_etds_to_re_tdaas_tj);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }



        //4 给数据请求方方的ETDS发送MQ消息【这里发的是统计信息，不是数据体】【流程11】
        try {
            claimReqBizPackage.setHolder(provide.getTdaas());
            // 构建设置bizData11的内容
            Map<String, Object> bizData11 = JSON.parseObject(JSON.toJSONString(reportDTO), Map.class);
            //设置bizData
            claimReqBizPackage.setBizData(bizData11);
            //创建凭证
            DTCResponse dtcResponse11 = dtcComponent.createDtc(claimReqBizPackage);
            //这就是凭证
            Map<String, Object> claim11 = dtcComponent.parse(dtcResponse11);
            //发送mq
            mqSendUtil.sendToETDS(request.getTdaas(), responseAuthDTO.getTo().getEtds(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim11), request.getEtds(), MessageConsumerEnum.pr_etds_to_re_etds_data);
        } catch (Exception e) {
            throw new BussinessException("【流程9】创建凭证失败:" + e.getMessage());
        }
        //5. 给数据请求方的ETDS发送MQ消息【这里发的是统计信息，不是数据体】【流程11】、数据提供方etds本地存储由于做统计
        //4.1 数据提供方etds本地存储由于做统计
        String claim_id = claim13.getOrDefault("id", "").toString();
        //数据请求方的企业名称
        String fromTdaasName = "";
        if (reportDTO.getFrom() != null) {
            try {
                fromTdaasName = reportDTO.getFrom().getTdaas() == null ? "" : dtidComponent.getCompanyNameByDtid(reportDTO.getFrom().getTdaas());
            } catch (Exception e) {
                log.error("【流程9】解析数字身份异常:{}", e.getMessage());
                throw new BussinessException("【流程9】解析数字身份异常:" + e.getMessage());
            }
        }
        //数据提供方的企业名称
        String toTdaasName = "";
        if (reportDTO.getTo() != null) {
            try {
                toTdaasName = reportDTO.getTo().getTdaas() == null ? "" : dtidComponent.getCompanyNameByDtid(reportDTO.getTo().getTdaas());
            } catch (Exception e) {
                log.error("【流程9】解析数字身份异常:{}", e.getMessage());
                throw new BussinessException("【流程9】解析数字身份异常:" + e.getMessage());
            }
        }
        //数据授权方的企业名称
        String authTdaasName = "";
        try {
            authTdaasName = (StringUtils.isBlank(responseAuthDTO.getCc().get(0).getTdaas())) ? "" : dtidComponent.getCompanyNameByDtid(responseAuthDTO.getCc().get(0).getTdaas());
        } catch (Exception e) {
            log.error("【流程9】解析数字身份异常:{}", e.getMessage());
            throw new BussinessException("【流程9】解析数字身份异常:" + e.getMessage());
        }
        boolean save = reportProvide11Service.save(
                new ReportProvide11().setAuthDtc(reportDTO.getAuthDtc())
                        .setAuthStatus(reportDTO.getAuthStatus())
                        .setAuthTdaas(responseAuthDTO.getCc().get(0).getTdaas())
                        .setAuthTdaasName(authTdaasName)
                        .setAuthStatusDesc(reportDTO.getAuthStatusDesc())
                        .setChunkLength(reportDTO.getChunkLength())
                        .setChunkSize(reportDTO.getChunkSize())
                        .setClaimId(claim_id)
                        .setFirstResponse(reportDTO.getFirstResponse())
                        .setFromEtds(reportDTO.getFrom() == null ? "" : reportDTO.getFrom().getEtds())
                        .setFromTdaas(reportDTO.getFrom() == null ? "" : reportDTO.getFrom().getTdaas())
                        .setFromTdaasName(fromTdaasName)
                        .setLastResponse(reportDTO.getLastResponse())
                        .setRequestedAt(reportDTO.getRequestedAt())
                        .setRequestHttpMetaBody(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getBody())
                        .setRequestHttpMetaHeader(reportDTO.getRequestHttpMeta() == null ? "" : JSON.toJSONString(reportDTO.getRequestHttpMeta().getHeader()))
                        .setRequestHttpMetaMethod(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getMethod())
                        .setRequestHttpMetaPath(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getPath())
                        .setRequestHttpMetaQuery(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getQuery())
                        .setResponseHttpMetaContentLength(reportDTO.getResponseHttpMeta() == null ? 0L : reportDTO.getResponseHttpMeta().getContentLength())
                        .setResponseHttpMetaHeader(reportDTO.getResponseHttpMeta() == null ? "" : JSON.toJSONString(reportDTO.getResponseHttpMeta().getHeader()))
                        .setResponseHttpMetaStatus(reportDTO.getResponseHttpMeta() == null ? -1 : reportDTO.getResponseHttpMeta().getStatus())
                        .setRetries(reportDTO.getRetries())
                        .setSerialNumber(reportDTO.getSerialNumber())
                        .setToEtds(reportDTO.getTo() == null ? "" : reportDTO.getTo().getEtds())
                        .setToTdaas(reportDTO.getTo() == null ? "" : reportDTO.getTo().getTdaas())
                        .setToTdaasName(toTdaasName)
                        .setCreateTime(new Date())
        );

        return Result.success(true);
    }


    /**
     * 根据凭证id查询授权凭证内容【查询的是数据供应方中的授权凭证】
     *
     * @param dtcId
     * @return
     */
    private ResponseAuthDTO getResponseAuthDTOByDtcIdProvide(String dtcId) {
        GrantResultProvide6 one = grantResultProvide6Service.getOne(new QueryWrapper<GrantResultProvide6>().lambda().eq(GrantResultProvide6::getClaimId, dtcId));
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


    /**
     * 根据凭证id查询授权凭证内容【查询的是数据请求方中的授权凭证】
     *
     * @param dtcId
     * @return
     */
    private ResponseAuthDTO getResponseAuthDTOByDtcIdApply(String dtcId) {
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
