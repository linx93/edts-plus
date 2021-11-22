package com.phadata.etdsplus.config;

import com.phadata.etdsplus.interceptor.JwtInterceptor;
import com.phadata.etdsplus.interceptor.TdaasInterceptor;
import com.phadata.etdsplus.service.EtdsService;
import org.springframework.beans.factory.annotation.Autowired;
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
    /**
     * 注册自定义拦截器
     */
    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(new JwtInterceptor())
                .addPathPatterns("/**").excludePathPatterns("/api/v1/etds/sync-etds-info","/api/v1/tdaas/**", "/api/v1/account/login","/doc.html", "/webjars/**", "/swagger-resources/**", "/web/**", "/error");
        registry.addInterceptor(new TdaasInterceptor(etdsService))
                .addPathPatterns("/api/v1/tdaas/**");
    }
}

