package com.phadata.etdsplus.mq;

import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.EtdsUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.*;
import org.springframework.amqp.rabbit.connection.ConnectionFactory;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.amqp.rabbit.listener.SimpleMessageListenerContainer;
import org.springframework.amqp.rabbit.listener.api.ChannelAwareMessageListener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;

/**
 * 动态初始化MQ的queue和binding，exchange在TDaaS上初始化
 * @author: linx
 * @since 2021-11-22 15:24
 */
@Slf4j
@Component
public class InitMQInfo {

    @Autowired
    private RabbitAdmin rabbitAdmin;
    @Autowired
    private ConnectionFactory connectionFactory;
    @Autowired
    private EtdsUtil etdsUtil;
    @Autowired
    private AuthResultApplyConsumer authResultApplyConsumer;
    @Autowired
    private AuthResultProvideConsumer authResultProvideConsumer;
    @Autowired
    private DataRequestProvideConsumer dataRequestProvideConsumer;
    @Autowired
    private DataResultApplyConsumer dataResultApplyConsumer;
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
     * 初始化监听
     *
     * @param etdsService
     */
    public void executeListener(EtdsService etdsService) {
        Etds etdsInfo = null;
        try {
            etdsInfo = etdsUtil.EtdsInfo(etdsService);
        } catch (BussinessException bussinessException) {
            //log.info("初始化时获取etds信息不存在:{}", bussinessException.getMessage());
        }
        if (etdsInfo == null) {
            return;
        }
        //("4", "6", "9", "11")
        log.info("初始化MQ的consumer，注册消费监听逻辑到SimpleMessageListenerContainer监听容器中开始：------------------------------------");
        registerConsumerToContainer(etdsInfo, authResultApplyConsumer, "4");
        registerConsumerToContainer(etdsInfo, authResultProvideConsumer, "6");
        registerConsumerToContainer(etdsInfo, dataRequestProvideConsumer, "9");
        registerConsumerToContainer(etdsInfo, dataResultApplyConsumer, "11");
        log.info("初始化MQ的consumer，注册消费监听逻辑到SimpleMessageListenerContainer监听容器中结束：------------------------------------");
    }

    /**
     * 初始化MQ的consumer，注册消费监听逻辑到SimpleMessageListenerContainer监听容器中
     *
     * @param value 需要初始化的枚举code值，对应MessageConsumerEnum枚举中的code("4", "6", "9", "11")
     */
    private void registerConsumerToContainer(Etds etdsInfo, ChannelAwareMessageListener messageListener, String value) {
        String companyDtid = etdsInfo.getCompanyDtid();
        String etdsCode = etdsInfo.getEtdsCode();
        String queueName = buildQueueName(companyDtid, etdsCode, value);
        log.info("开始监听队列：{}", queueName);
        SimpleMessageListenerContainer container = new SimpleMessageListenerContainer(connectionFactory);
        container.setQueueNames(queueName);
        container.setExposeListenerChannel(true);
        //设置每个消费者获取的最大的消息数量
        container.setPrefetchCount(2);
        //消费者个数
        container.setConcurrentConsumers(1);
        //设置确认模式为手工确认
        container.setAcknowledgeMode(AcknowledgeMode.MANUAL);
        container.setMessageListener(messageListener);
        container.start();
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
    public static String buildRoutingKey(String companyDtid, String etdsCode, String code) {
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
    public static String buildQueueName(String companyDtid, String etdsCode, String code) {
        return new StringBuilder(companyDtid).append("_").append(etdsCode).append("_").append(code).toString();
    }

}
