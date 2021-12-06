package com.phadata.etdsplus.mq;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.constant.SystemConstant;
import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.GrantResultProvide6;
import com.phadata.etdsplus.entity.po.ReDataNoticeProvide9;
import com.phadata.etdsplus.enums.DataType;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.EtdsMapper;
import com.phadata.etdsplus.service.*;
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
import java.util.List;
import java.util.Map;
import java.util.UUID;


/**
 * 数据请求的消费者【流程中对应9】
 *
 * @author linx
 */
@Component
@Slf4j
public class DataRequestProvideConsumer implements ChannelAwareMessageListener {

    @Value("${custom.data-push:}")
    private String dataPush;

    private final ReDataNoticeProvide9Service reDataNoticeProvide9Service;
    private final GrantResultProvide6Service grantResultProvide6Service;
    private final EtdsMapper etdsMapper;
    private final MQSendUtil mqSendUtil;
    private final DTCComponent dtcComponent;

    public DataRequestProvideConsumer(ReDataNoticeProvide9Service reDataNoticeProvide9Service, GrantResultProvide6Service grantResultProvide6Service, EtdsMapper etdsMapper, MQSendUtil mqSendUtil, DTCComponent dtcComponent) {
        this.reDataNoticeProvide9Service = reDataNoticeProvide9Service;
        this.grantResultProvide6Service = grantResultProvide6Service;
        this.etdsMapper = etdsMapper;
        this.mqSendUtil = mqSendUtil;
        this.dtcComponent = dtcComponent;
    }

    @Override
    public void onMessage(Message message, Channel channel) {
        try {
            List<Etds> list = etdsMapper.selectList(new QueryWrapper<>());
            if (list.isEmpty()) {
                throw new BussinessException("etds暂未注册");
            }
            if (list.size() > 1) {
                throw new BussinessException("存在多个etds错误");
            }
            Etds etdsInfo = list.get(0);
            String msg = new String(message.getBody());
            log.info("数据请求的消费者【流程中对应9】消费消息：{}", msg);
            //1. 获取凭证   这个vc就是凭证
            VerifiableClaim vc = JSON.parseObject(msg, VerifiableClaim.class);
            ApplyDataDTO applyDataDTO = JSON.parseObject(JSON.toJSONString(vc.getCredentialSubject().getBizData()), ApplyDataDTO.class);
            //2. 获取授权凭证id，再到本地库中查询授权凭证，检查这个凭证的状态是否被TDaaS关闭使用
            String authDtcId = vc.getCredentialSubject().getBizData().getOrDefault("dtc", "-1").toString();
            GrantResultProvide6 one = grantResultProvide6Service.getOne(new QueryWrapper<GrantResultProvide6>().lambda().eq(GrantResultProvide6::getClaimId, authDtcId));
            if (one == null || one.getUseStatus() == 1) {
                //构建创建凭证的参数ClaimReqBizPackage
                ClaimReqBizPackage claimReqBizPackage = new ClaimReqBizPackage()
                        .setType(DTCType.DATA.getType())
                        .setTdrType("-1")
                        .setTimes(0)
                        .setExpire(SystemConstant.EXPIRED)
                        .setUnionId(UUID.randomUUID().toString())
                        .setIssuer(etdsInfo.getCompanyDtid());
                //推送凭证被停用的结果给请求方
                claimReqBizPackage.setHolder(applyDataDTO.getFrom().getTdaas());
                ResponseDataDTO responseData = new ResponseDataDTO();
                responseData.setBizHeader(null);
                responseData.setChunk("");
                responseData.setSerialNumber(vc.getCredentialSubject().getBizData().getOrDefault(SystemConstant.SERIAL_NUMBER, "").toString());
                String desc = "";
                if (one == null) {
                    desc = "此授权凭证不存在";
                    log.error("授权凭证【claimId={}】不存在", authDtcId);
                } else {
                    if (one.getUseStatus() == 1) {
                        desc = "此授权凭证被数据提供方TDaaS暂时关闭使用";
                        log.error("授权凭证【claimId={}】被数据提供方TDaaS暂时关闭使用", authDtcId);
                    }
                }
                responseData.setDtc(new AuthState().setDtc(authDtcId).setDesc(desc).setCode(0));
                responseData.setTo(applyDataDTO.getFrom());
                responseData.setFrom(new Address().setTdaas(etdsInfo.getCompanyDtid()).setEtds(etdsInfo.getEtdsCode()));
                // 构建设置bizData11的内容
                Map<String, Object> bizData11 = JSON.parseObject(JSON.toJSONString(responseData), Map.class);
                //设置bizData
                claimReqBizPackage.setBizData(bizData11);
                //创建凭证
                DTCResponse dtcResponse11 = dtcComponent.createDtc(claimReqBizPackage);
                //这就是凭证
                Map<String, Object> claim11 = dtcComponent.parse(dtcResponse11);
                //发送mq
                mqSendUtil.sendToETDS(responseData.getTo().getTdaas(), etdsInfo.getEtdsCode(), DataType.RESPONSE.getRemark(), JSON.toJSONString(claim11), responseData.getTo().getEtds(), MessageConsumerEnum.pr_etds_to_re_etds_data);
                //结束，签收消息，不调用定制层
                channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
                return;
            }
            //3. 调用定制层接口：将携带请求数据信息[HttpMate信息]的凭证推给定制层，有定制层去执行真正的请求数据
            HttpResponse execute = HttpRequest.post(dataPush).body(JSON.toJSONString(vc)).execute();
            log.info("调用定制层接口：将携带请求数据信息[HttpMate信息]的凭证推给定制层的响应:", execute);

            //4. 本地存储业务 表中的凭证id字段加了唯一索引，防止重复消费
            long epochSecond = Instant.now().getEpochSecond();
            reDataNoticeProvide9Service.save(
                    new ReDataNoticeProvide9().setClaimId(vc.getId())
                            .setCreatedTime(epochSecond)
                            .setOperatedTime(epochSecond)
                            .setSerialNumber(vc.getCredentialSubject().getBizData().getOrDefault(SystemConstant.SERIAL_NUMBER, "").toString())
                            .setDtcDocument(JSON.toJSONString(vc))
            );
            //. 手动签收
            channel.basicAck(message.getMessageProperties().getDeliveryTag(), false);
        } catch (Exception e) {
            log.error("数据请求的消费者【流程中对应9】消费数据异常:{}", e);
            log.error("数据请求的消费者【流程中对应9】消费数据异常，MessageId: [{}]", message.getMessageProperties().getMessageId());
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
