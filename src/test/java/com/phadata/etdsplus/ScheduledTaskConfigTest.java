package com.phadata.etdsplus;

import com.phadata.etdsplus.config.ScheduledTaskConfig;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

/**
 * 定时任务配置读取测试
 *
 * @author linx
 * @since 2021-12-31 11:14
 */
@SpringBootTest
public class ScheduledTaskConfigTest {
    @Autowired
    private ScheduledTaskConfig scheduledTaskConfig;

    @Test
    void test() {
        System.out.println(scheduledTaskConfig);
    }
}
