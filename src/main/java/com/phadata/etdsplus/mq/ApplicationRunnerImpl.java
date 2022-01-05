package com.phadata.etdsplus.mq;

import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.service.EtdsService;
import lombok.AllArgsConstructor;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * springboot启动成功后执行下面的run方法
 *
 * @author linx
 */
@Component
@AllArgsConstructor
public class ApplicationRunnerImpl implements ApplicationRunner {

    private final InitMQInfo initMQInfo;

    private final EtdsService etdsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后执行代码逻辑");
        Etds one = etdsService.getOne(null);
        if (one != null) {
            //初始化队列一句绑定关系
            initMQInfo.initMQInfo(etdsService);
            //添加监听
            initMQInfo.executeListener(etdsService);
        }
    }
}