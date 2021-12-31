package com.phadata.etdsplus.config;

import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mq.InitMQInfo;
import lombok.Data;
import lombok.extern.slf4j.Slf4j;
import org.springframework.amqp.core.Binding;
import org.springframework.amqp.core.Queue;
import org.springframework.amqp.rabbit.core.RabbitAdmin;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Configuration;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.SchedulingConfigurer;
import org.springframework.scheduling.config.ScheduledTaskRegistrar;
import org.springframework.scheduling.support.CronTrigger;

import java.time.Instant;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Map;

/**
 * 检查etds过期的定时任务 // @EnableScheduling注解是开启定时任务
 *
 * @author linx
 * @since 2021-12-31 10:52
 */
@Slf4j
@Configuration
@EnableScheduling
public class EtdsCheckScheduledTask implements SchedulingConfigurer {

    @Autowired
    private ScheduledTaskConfig scheduledTaskConfig;

    @Autowired
    private RabbitAdmin rabbitAdmin;

    @Autowired
    private JdbcTemplate jdbcTemplate;

    /**
     * 执行定时任务
     *
     * @param taskRegistrar 任务注册器
     */
    @Override
    public void configureTasks(ScheduledTaskRegistrar taskRegistrar) {
        taskRegistrar.addTriggerTask(
                //1.添加任务内容(Runnable)
                () -> {
                    log.info("执行动态定时任务处理etds的过期检查: {}", LocalDateTime.now().toLocalTime());
                    EtdsInfo etdsInfo = queryEtds();
                    Long expirationTime = etdsInfo.getExpirationTime();
                    log.info("ETDS的过期时间");
                    if (Instant.now().getEpochSecond() >= expirationTime) {
                        //etds过期了
                        log.error("ETDS过期了，ETDS的信息:{}", etdsInfo);
                        //处理MQ逻辑
                        removeBinding(etdsInfo);
                    }
                },
                //2.设置执行周期(Trigger)
                triggerContext -> {
                    //2.1 从数据库获取执行周期
                    String checkEtds = scheduledTaskConfig.getCron().getCheckEtds();
                    //2.2 合法性校验.
                    if (checkEtds == null || "".equals(checkEtds.trim())) {
                        checkEtds = "1 0 0 * * ?";
                    }
                    log.info("checkEtds={}", checkEtds);
                    //2.3 返回执行周期(Date)
                    return new CronTrigger(checkEtds).nextExecutionTime(triggerContext);
                }
        );
    }


    /**
     * 移除所有绑定关系
     */
    public void removeBinding(EtdsInfo etdsInfo) {
        log.info("定任务触发器，ETDS过期,移除所有绑定关系开始：------------------------------------");
        for (String value : InitMQInfo.CODE_LIST) {
            Queue queue = new Queue(InitMQInfo.buildQueueName(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), value));
            rabbitAdmin.declareQueue(queue);
            Binding binding = new Binding(queue.getName(), Binding.DestinationType.QUEUE, InitMQInfo.EXCHANGE_NAME, InitMQInfo.buildRoutingKey(etdsInfo.getCompanyDtid(), etdsInfo.getEtdsCode(), value), null);
            log.info("移除绑定关系：{}", binding.toString());
            rabbitAdmin.removeBinding(binding);
        }
        log.info("定任务触发器，ETDS过期,移除所有绑定关系结束：------------------------------------");
    }

    /**
     * 查询etds的数字身份和etds唯一吗
     *
     * @return etds的数字身份和etds唯一吗
     */
    private EtdsInfo queryEtds() {
        String sql = "select license_expiration_time as expirationTime,etds_code as etdsCode,company_dtid as companyDtid from etds";
        List<Map<String, Object>> maps = jdbcTemplate.queryForList(sql);
        if (maps.isEmpty()) {
            throw new BussinessException("etds暂未注册");
        }
        if (maps.size() > 1) {
            throw new BussinessException("存在多个etds错误");
        }
        Map<String, Object> etdsMap = maps.get(0);
        String etdsCode = etdsMap.getOrDefault("etdsCode", "").toString();
        String companyDtid = etdsMap.getOrDefault("companyDtid", "").toString();
        EtdsInfo etdsInfo = new EtdsInfo();
        etdsInfo.setCompanyDtid(companyDtid);
        etdsInfo.setEtdsCode(etdsCode);
        etdsInfo.setExpirationTime(Long.valueOf(etdsMap.getOrDefault("expirationTime", "2640935233").toString()));
        return etdsInfo;
    }

    @Data
    private static class EtdsInfo {
        private String etdsCode;
        private String companyDtid;
        private Long expirationTime;
    }
}
