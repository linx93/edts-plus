package com.phadata.etdsplus.mq;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.rabbitmq.client.Channel;
import lombok.extern.slf4j.Slf4j;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.amqp.core.Message;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.io.IOException;


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

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            String msg = new String(message.getBody());
            log.info("数据请求的消费者【流程中对应9】消费消息：{}", msg);
            //1. 获取凭证   这个vc就是授权凭证
            VerifiableClaim vc = JSON.parseObject(msg, VerifiableClaim.class);
            //todo 2. 本地存储业务  表中的凭证id字段加了唯一索引，防止重复消费

            //3. 调用定制层接口：将携带数据的凭证推给定制层
            HttpResponse execute = HttpRequest.post(dataPush).body(JSON.toJSONString(vc)).execute();
            log.info("调用定制层接口：将携带数据的凭证推给定制层:", execute.body());
            //手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据响应结果的消费者【流程中对应11】消费数据异常:{}", e);
            log.error("数据响应结果的消费者【流程中对应11】消费数据提交确认异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
            try {
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
            } catch (IOException ioException) {
                log.error("手动单条签收异常消息出现问题");
                ioException.printStackTrace();
            }
        }
    }

}
