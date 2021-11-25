package com.phadata.etdsplus.mq;

import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.stereotype.Component;

import java.io.IOException;


/**
 * 数据请求的消费者【流程中对应9】
 *
 * @author linx
 */
@Component
@Slf4j
public class DataRequestProvideConsumer implements ChannelAwareMessageListener {


    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            log.info("数据请求的消费者【流程中对应9】消费消息：{}", new String(message.getBody()));
            //TODO 逻辑处理如下
            //1. 解决重复消费问题
            //2. 本地存储业务

            //3. 给数据提供方（自己）的tdaas发凭证 mq 【不含实际数据】
            //4. 给数据授权方       的TDaaS发凭证 mq 【不含实际数据】
            //5. 给数据请求方       的TDaaS发凭证 mq 【不含实际数据】
            //6. 给数据请求方       的ETDS发凭证  mq 【含实际数据】
            //7. 手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据请求的消费者【流程中对应9】消费数据异常:{}", e);
            log.error("数据请求的消费者【流程中对应9】消费数据异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                log.error("手动单条签收异常消息出现问题");
                ioException.printStackTrace();
            }
        }
    }

}
