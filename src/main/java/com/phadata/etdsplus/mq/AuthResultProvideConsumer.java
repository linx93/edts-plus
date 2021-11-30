package com.phadata.etdsplus.mq;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.po.GrantResultProvide6;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.GrantResultProvide6Service;
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
 * 数据授权的消费者【流程中对应6】
 *
 * @author linx
 */
@Component
@Slf4j
public class AuthResultProvideConsumer implements ChannelAwareMessageListener {
    @Autowired
    private GrantResultProvide6Service grantResultProvide6Service;

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            log.info("数据授权的消费者【流程中对应6】消费消息{}：", new String(message.getBody()));
            String msg = new String(message.getBody());
            //这个就是授权凭证
            VerifiableClaim vc = JSON.parseObject(msg, VerifiableClaim.class);
            log.info("数据授权的消费者【流程中对应4】消费消息：{}", JSON.toJSONString(vc, true));
            //bizData就是数据
            Map<String, Object> bizData = vc.getCredentialSubject().getBizData();
            //保存凭证
            long epochSecond = Instant.now().getEpochSecond();
            grantResultProvide6Service.save(new GrantResultProvide6()
                    .setClaimId(vc.getId())
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
                    .setToEtdsUuid(bizData.getOrDefault("", "").toString())
                    .setUseStatus(0));

            //手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据授权的消费者【流程中对应6】消费数据异常:{}", e);
            log.error("数据授权的消费者【流程中对应6】消费数据异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                log.error("手动单条签收异常消息出现问题");
                ioException.printStackTrace();
            }
        }
    }

}
