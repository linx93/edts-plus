package com.phadata.etdsplus.mq;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.mq.BizMessage;
import com.phadata.etdsplus.mq.ExchangeEnum;
import com.phadata.etdsplus.mq.InitMQInfo;
import com.phadata.etdsplus.mq.MessageConsumerEnum;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

/**
 * mq的发送工具
 *
 * @author: linx
 * @since 2021-11-25 17:05
 */
@Slf4j
@Component
public class MQSendUtil {
    @Value("${mq.send}")
    private String sendMQ;

    public void sendToETDS(String errorLogInfo, String dtid, String machineId, String title, String content, String etdsCode, MessageConsumerEnum messageConsumerEnum) {
        if (dtid == null || dtid.equals("")) {
            log.error("发送mq失败,dtid为空");
            return;
        }
        String queueName = InitMQInfo.buildQueueName(dtid, etdsCode, String.valueOf(messageConsumerEnum.getCode()));
        String routingKey = InitMQInfo.buildRoutingKey(dtid, etdsCode, String.valueOf(messageConsumerEnum.getCode()));
        BizMessage bizMessage = new BizMessage()
                .setDtid(dtid)
                .setMachineId(machineId)
                .setTitle(title)
                .setBaseMqInfo(new BizMessage.BaseMqInfo()
                        .setContent(content)
                        .setExchange(ExchangeEnum.AUTH_DATA_EXCHANGE.getCode())
                        .setExchangeType("DIRECT")
                        .setQueue(queueName)
                        .setRoutingKey(routingKey));
        HttpResponse execute = HttpRequest.post(sendMQ).body(JSON.toJSONString(bizMessage)).execute();
        log.info(errorLogInfo);
        log.info("调用MQ的入参:{}", JSON.toJSONString(bizMessage, true));
        log.info("【SEND-MQ-ETDS】发送mq的请求返回:{}", execute.body());
    }

    public void sendToTDaaS(String errorLogInfo, String dtid, String machineId, String title, String content, MessageConsumerEnum messageConsumerEnum) {
        if (dtid == null || dtid.equals("")) {
            log.error("发送mq失败,dtid为空");
            return;
        }
        String queueName = dtid + "_" + messageConsumerEnum.getCode();
        String routingKey = dtid + "_" + messageConsumerEnum.getCode();
        BizMessage bizMessage = new BizMessage()
                .setDtid(dtid)
                .setMachineId(machineId)
                .setTitle(title)
                .setBaseMqInfo(new BizMessage.BaseMqInfo()
                        .setContent(content)
                        .setExchange(ExchangeEnum.AUTH_DATA_EXCHANGE.getCode())
                        .setExchangeType("DIRECT")
                        .setQueue(queueName)
                        .setRoutingKey(routingKey));

        HttpResponse execute = HttpRequest.post(sendMQ).body(JSON.toJSONString(bizMessage)).execute();
        log.info(errorLogInfo);
        log.info("调用MQ的入参:{}", JSON.toJSONString(bizMessage, true));
        log.info("【SEND-MQ-TDaaS】发送mq的请求返回:{}", execute.body());
    }
}
