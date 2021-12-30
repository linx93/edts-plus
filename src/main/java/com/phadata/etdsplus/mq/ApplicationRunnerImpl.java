package com.phadata.etdsplus.mq;

import com.phadata.etdsplus.service.EtdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.ApplicationArguments;
import org.springframework.boot.ApplicationRunner;
import org.springframework.stereotype.Component;


/**
 * springboot启动成功后执行下面的run方法
 *
 * @author linx
 */
@Component
public class ApplicationRunnerImpl implements ApplicationRunner {
    @Autowired
    private InitMQInfo initMQInfo;
    @Autowired
    private EtdsService etdsService;

    @Override
    public void run(ApplicationArguments args) throws Exception {
        System.out.println("通过实现ApplicationRunner接口，在spring boot项目启动后执行代码逻辑");
        //初始化队列一句绑定关系
        initMQInfo.initMQInfo(etdsService);
        //添加监听
        initMQInfo.executeListener(etdsService);
    }
}