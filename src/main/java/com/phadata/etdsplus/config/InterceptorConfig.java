package com.phadata.etdsplus.config;

import com.baomidou.mybatisplus.annotation.DbType;
import com.baomidou.mybatisplus.extension.plugins.MybatisPlusInterceptor;
import com.baomidou.mybatisplus.extension.plugins.inner.PaginationInnerInterceptor;
import com.phadata.etdsplus.interceptor.CustomInterceptor;
import com.phadata.etdsplus.interceptor.JwtInterceptor;
import com.phadata.etdsplus.interceptor.TdaasInterceptor;
import com.phadata.etdsplus.service.DataSwitchService;
import com.phadata.etdsplus.service.EtdsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;


/**
 * 登录拦截器配置
 *
 * @author linx
 */
@Configuration
public class InterceptorConfig implements WebMvcConfigurer {

    @Autowired
    private EtdsService etdsService;
    @Autowired
    private DataSwitchService dataSwitchService;


    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/api/v1/etds/sync-etds-info", "/api/v1/tdaas/**", "/api/v1/custom/**", "/api/v1/account/login", "/doc.html", "/webjars/**", "/swagger-resources/**", "/web/**", "/error");
        //针对处理TDaaS的请求
        registry.addInterceptor(new TdaasInterceptor(etdsService))
                .addPathPatterns("/api/v1/tdaas/**");
        //针对处理定制层的请求
        registry.addInterceptor(new CustomInterceptor(dataSwitchService))
                .addPathPatterns("/api/v1/custom/**");
    }


    /**
     * mybatis-plus的分页配置
     *
     * @return
     */
    @Bean
    public MybatisPlusInterceptor mybatisPlusInterceptor() {
        MybatisPlusInterceptor interceptor = new MybatisPlusInterceptor();
        interceptor.addInnerInterceptor(new PaginationInnerInterceptor(DbType.MYSQL));
        return interceptor;
    }
}

