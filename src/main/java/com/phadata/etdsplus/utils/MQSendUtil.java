package com.phadata.etdsplus.utils;

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
 * @description: mq的发送工具
 * @author: xionglin
 * @create: 2021-11-25 17:05
 */
@Slf4j
@Component
public class MQSendUtil {
    @Value("${mq.send}")
    private String sendMQ;

    public void sendToETDS(String dtid, String machineId, String title, String content, String etdsCode, MessageConsumerEnum messageConsumerEnum) {
        BizMessage bizMessage = new BizMessage()
                .setDtid(dtid)
                .setMachineId(machineId)
                .setTitle(title)
                .setBaseMqInfo(new BizMessage.BaseMqInfo()
                        .setContent(content)
                        .setExchange(ExchangeEnum.AUTH_DATA_EXCHANGE.getCode())
                        .setExchangeType("DIRECT")
                        .setQueue(InitMQInfo.buildQueueName(dtid, etdsCode, String.valueOf(messageConsumerEnum.getCode())))
                        .setRoutingKey(InitMQInfo.buildQueueName(dtid, etdsCode, String.valueOf(messageConsumerEnum.getCode()))));
        HttpResponse execute = HttpRequest.post(sendMQ).body(JSON.toJSONString(bizMessage)).execute();
        log.info("【发给etds消费的】发送mq的请求返回:{}", execute.body());
    }

    public void sendToTDaaS(String dtid, String machineId, String title, String content, MessageConsumerEnum messageConsumerEnum) {
        BizMessage bizMessage = new BizMessage()
                .setDtid(dtid)
                .setMachineId(machineId)
                .setTitle(title)
                .setBaseMqInfo(new BizMessage.BaseMqInfo()
                        .setContent(content)
                        .setExchange(ExchangeEnum.AUTH_DATA_EXCHANGE.getCode())
                        .setExchangeType("DIRECT")
                        .setQueue(dtid + "_" + messageConsumerEnum.getCode())
                        .setRoutingKey(dtid + "_" + messageConsumerEnum.getCode()));
        HttpResponse execute = HttpRequest.post(sendMQ).body(JSON.toJSONString(bizMessage)).execute();
        log.info("【发给tdaas消费的】发送mq的请求返回:{}", execute.body());
    }
}
