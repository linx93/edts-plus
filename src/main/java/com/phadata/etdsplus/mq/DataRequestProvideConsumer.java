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
            /*ClaimInfoData claimInfoData = JSON.parseObject(new String(message.getBody()), ClaimInfoData.class);
            for (int i = 0; i < claimInfoData.getClaims().size(); i++) {
                ClaimInfoData.Claim claim = claimInfoData.getClaims().get(i);
                VerifiableClaim verifiableClaim = claim.getClaim();
                String holder = verifiableClaim.getCredentialSubject().getId();
                //解决幂等性问题
                QueryWrapper<DtcDocument> dtcDocumentQueryWrapper = new QueryWrapper<>();
                dtcDocumentQueryWrapper.eq("issuer", verifiableClaim.getIssuer());
                dtcDocumentQueryWrapper.eq("holder", holder);
                dtcDocumentQueryWrapper.eq("dtcid", verifiableClaim.getId());
                DtcDocument selectOne = dtcDocumentMapper.selectOne(dtcDocumentQueryWrapper);
                if (selectOne != null) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return;
                }
                String scene = verifiableClaim.getCredentialSubject().getBizData().getOrDefault("scene","数据流通").toString();
                if (scene.equals(SystemConstant.DATASCENE01)) {
                    receiveDTCI.executePush(verifiableClaim);
                }
                //保存日志信息
                rabbitService.saveAllByAccept(verifiableClaim, claim, holder, claimInfoData,dtcLogMapper);
                if (i == claimInfoData.getClaims().size() - 1) {
                    channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                    return;
                }
            }*/
            //手动签收
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
