package com.phadata.etdsplus.mq;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.po.GrantResultApply4;
import com.phadata.etdsplus.service.GrantResultApply4Service;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.io.IOException;
import java.time.Instant;
import java.util.Map;


/**
 * 数据授权的消费者【流程中对应4】
 *
 * @author linx
 */
@Component
@Slf4j
public class AuthResultApplyConsumer implements ChannelAwareMessageListener {
    @Autowired
    private GrantResultApply4Service grantResultApply4Service;

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            //这个就是授权凭证
            VerifiableClaim vc = JSON.parseObject(msg, VerifiableClaim.class);
            log.info("数据授权的消费者【流程中对应4】消费消息：{}", JSON.toJSONString(vc, true));
            //bizData就是数据
            Map<String, Object> bizData = vc.getCredentialSubject().getBizData();
            //1. 保存凭证   TODO 下面的bizData中取出的key，需要和柯博对接确定
            long epochSecond = Instant.now().getEpochSecond();
            grantResultApply4Service.save(new GrantResultApply4()
                    .setCreatedTime(epochSecond)
                    .setOperatedTime(epochSecond)
                    .setApplyEtdsUuid(bizData.getOrDefault("", "").toString())
                    .setGrantDetails(bizData.getOrDefault("", "").toString())
                    .setGrantDtid(bizData.getOrDefault("", "").toString())
                    .setGrantStatus(bizData.getOrDefault("", "").toString())
                    .setGrantDocument(JSON.toJSONString(vc))
                    .setNoticeId(Long.valueOf(bizData.getOrDefault("", -1).toString()))
                    .setSerialNumber(bizData.getOrDefault("serialNumber", "").toString())
                    .setToDtid(bizData.getOrDefault("", "").toString())
                    .setToEtdsUuid(bizData.getOrDefault("", "").toString()));
            //TODO 2. 调用定制层的回调接口，推送授权凭证给定制层
            //手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据授权的消费者【流程中对应4】消费消息数据异常:{}", e);
            log.error("数据授权的消费者【流程中对应4】消费消息数据异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                log.error("手动单条签收异常消息出现问题");
                ioException.printStackTrace();
            }
        }
    }

}
