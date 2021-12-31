package com.phadata.etdsplus.config;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

/**
 * 定时任务配置读取
 *
 * @author linx
 * @since 2021-12-31 11:00
 */
@Data
@Configuration
@ConfigurationProperties(prefix = "scheduled-task")
public class ScheduledTaskConfig {
    /**
     * cron表达式
     */
    private Cron cron;

    @Data
    public static class Cron {
        private String checkEtds;
    }

}
