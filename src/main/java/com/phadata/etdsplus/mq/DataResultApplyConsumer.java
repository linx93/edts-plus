package com.phadata.etdsplus.mq;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.constant.SystemConstant;
import com.phadata.etdsplus.entity.dto.ReportDTO;
import com.phadata.etdsplus.entity.po.DataResultApply11;
import com.phadata.etdsplus.entity.po.ReportApply11;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DataResultApply11Service;
import com.phadata.etdsplus.service.ReportApply11Service;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.phadata.identity.common.DTCType;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Date;
import java.util.List;
import java.util.Map;


/**
 * 数据响应结果的消费者【流程中对应11】
 *
 * @author linx
 */
@Component
@Slf4j
public class DataResultApplyConsumer implements ChannelAwareMessageListener {

    @Value("${custom.data-push:}")
    private String dataPush;
    private final DataResultApply11Service dataResultApply11Service;
    private final ReportApply11Service reportApply11Service;

    public DataResultApplyConsumer(DataResultApply11Service dataResultApply11Service, ReportApply11Service reportApply11Service) {
        this.dataResultApply11Service = dataResultApply11Service;
        this.reportApply11Service = reportApply11Service;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            log.info("数据请求的消费者【流程中对应9】消费消息：{}", msg);
            //1. 获取凭证   这个vc就是凭证
            VerifiableClaim vc = JSON.parseObject(msg, VerifiableClaim.class);
            if (vc == null) {
                throw new BussinessException("凭证为空");
            }
            List<String> type = vc.getType();
            if (type.contains(DTCType.DATA.getType())) {
                //处理的是数据凭证
                //2. 调用定制层接口：将携带数据的凭证推给定制层
                HttpResponse execute = HttpRequest.post(dataPush).body(JSON.toJSONString(vc)).execute();
                log.info("调用定制层接口：将携带数据的凭证推给定制层:", execute);
                //3. 本地存储业务  表中的凭证id字段加了唯一索引，防止重复消费
                long epochSecond = Instant.now().getEpochSecond();
                dataResultApply11Service.save(new DataResultApply11()
                        .setClaimId(vc.getId())
                        .setCreatedTime(epochSecond)
                        .setOperatedTime(epochSecond)
                        .setSerialNumber(vc.getCredentialSubject().getBizData().getOrDefault(SystemConstant.SERIAL_NUMBER, "").toString())
                        .setDataDocument(JSON.toJSONString(vc)));

            }

            if (type.contains(DTCType.OTHER.getType())) {
                Map<String, Object> bizData = vc.getCredentialSubject().getBizData();
                ReportDTO reportDTO = JSON.parseObject(JSON.toJSONString(bizData), ReportDTO.class);
                //处理的是统计数据的凭证
                reportApply11Service.save(
                        new ReportApply11()
                                .setAuthDtc(reportDTO.getAuthDtc())
                                .setAuthStatus(reportDTO.getAuthStatus())
                                .setAuthStatusDesc(reportDTO.getAuthStatusDesc())
                                .setChunkLength(reportDTO.getChunkLength())
                                .setChunkSize(reportDTO.getChunkSize())
                                .setClaimId(vc.getId())
                                .setFirstResponse(reportDTO.getFirstResponse())
                                .setFromEtds(reportDTO.getFrom() == null ? "" : reportDTO.getFrom().getEtds())
                                .setFromTdaas(reportDTO.getFrom() == null ? "" : reportDTO.getFrom().getTdaas())
                                .setLastResponse(reportDTO.getLastResponse())
                                .setRequestedAt(reportDTO.getRequestedAt())
                                .setRequestHttpMetaBody(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getBody())
                                .setRequestHttpMetaHeader(reportDTO.getRequestHttpMeta() == null ? "" : JSON.toJSONString(reportDTO.getRequestHttpMeta().getHeader()))
                                .setRequestHttpMetaMethod(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getMethod())
                                .setRequestHttpMetaPath(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getPath())
                                .setRequestHttpMetaQuery(reportDTO.getRequestHttpMeta() == null ? "" : reportDTO.getRequestHttpMeta().getQuery())
                                .setResponseHttpMetaContentlength(reportDTO.getResponseHttpMeta() == null ? 0L : reportDTO.getResponseHttpMeta().getContentLength())
                                .setResponseHttpMetaHeader(reportDTO.getResponseHttpMeta() == null ? "" : JSON.toJSONString(reportDTO.getResponseHttpMeta().getHeader()))
                                .setResponseHttpMetaStatus(reportDTO.getResponseHttpMeta() == null ? -1 : reportDTO.getResponseHttpMeta().getStatus())
                                .setRetries(reportDTO.getRetries())
                                .setSerialNumber(reportDTO.getSerialNumber())
                                .setToEtds(reportDTO.getTo() == null ? "" : reportDTO.getTo().getEtds())
                                .setToTdaas(reportDTO.getTo() == null ? "" : reportDTO.getTo().getTdaas())
                                .setCreateTime(new Date()));

            }
            //手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据响应结果的消费者【流程中对应11】消费数据异常:{}", e);
            log.error("数据响应结果的消费者【流程中对应11】消费数据提交确认异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                log.error("消费发生异常后，手动签收消息，MessageId: [{}]", message.getMessageProperties().getMessageId());
            } catch (IOException ioException) {
                log.error("手动单条签收异常消息出现问题");
                ioException.printStackTrace();
            }
        }
    }

}
