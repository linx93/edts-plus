package com.phadata.etdsplus.mq;

import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.EtdsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @description: 动态初始化MQ的queue和binding，exchange在TDaaS上初始化
 * @author: linx
 * @create: 2021-11-22 15:24
 */
@Slf4j
@Component
public class InitMQInfo {

    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private EtdsUtil etdsUtil;

    /**
     * 全局交换机名称
     */
    private String exchangeName = ExchangeEnum.AUTH_DATA_EXCHANGE.getCode();


    /**
     * 需要初始化的枚举code值，对应MessageConsumerEnum枚举中的code
     */
    private List<String> codeList = Arrays.asList("4", "6", "9", "11");

    /**
     * 初始化MQ和bind
     */
    public void initMQInfo(EtdsService etdsService) {
        Etds etds = etdsUtil.EtdsInfo(etdsService);
        String companyDtid = etds.getCompanyDtid();
        String etdsCode = etds.getEtdsCode();
        log.info("初始化MQ和bind关系开始：------------------------------------");
        log.info("etds的数字身份：{}", companyDtid);
        log.info("etds的唯一码：{}", etdsCode);
        for (String value : codeList) {
            Queue queue = new Queue(buildQueueName(companyDtid, etdsCode, value));
            log.info("创建队列：{},  详细信息：{}", queue.getName(), queue.toString());
            rabbitAdmin.declareQueue(queue);
            Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchangeName, buildRoutingKey(companyDtid, etdsCode, value), null);
            log.info("创建绑定关系详细信息：{}", binding.toString());
            rabbitAdmin.declareBinding(binding);
        }
        log.info("初始化MQ和bind关系结束：------------------------------------");
    }

    /**
     * 移除所有绑定关系
     */
    public void removeBinding(EtdsService etdsService) {
        log.info("移除所有绑定关系开始：------------------------------------");
        Etds etds = etdsUtil.EtdsInfo(etdsService);
        for (String value : codeList) {
            Queue queue = new Queue(buildQueueName(etds.getCompanyDtid(), etds.getEtdsCode(), value));
            rabbitAdmin.declareQueue(queue);
            Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, exchangeName, buildRoutingKey(etds.getCompanyDtid(), etds.getEtdsCode(), value), null);
            log.info("移除绑定关系：{}", binding.toString());
            rabbitAdmin.removeBinding(binding);
        }
        log.info("移除所有绑定关系结束：------------------------------------");

    }

    /**
     * 构建队列全局唯一名
     *
     * @param companyDtid 公司的dtid
     * @param etdsCode    etds的唯一码
     * @param code        枚举code，标记了流程的唯一，对应一直存储表
     * @return
     */
    private String buildRoutingKey(String companyDtid, String etdsCode, String code) {
        return new StringBuilder(companyDtid).append("_").append(etdsCode).append("_").append(code).toString();
    }

    /**
     * 构建队列全局唯一名
     *
     * @param companyDtid 公司的dtid
     * @param etdsCode    etds的唯一码
     * @param code        枚举code，标记了流程的唯一，对应一直存储表
     * @return
     */
    private String buildQueueName(String companyDtid, String etdsCode, String code) {
        return new StringBuilder(companyDtid).append("_").append(etdsCode).append("_").append(code).toString();
    }

}
