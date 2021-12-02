package com.phadata.etdsplus.interceptor;


import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DataSwitchService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * 处理定制层请求的拦截器
 *
 * @author linx
 */
@Slf4j
public class CustomInterceptor implements HandlerInterceptor {

    private final DataSwitchService dataSwitchService;

    public CustomInterceptor(DataSwitchService dataSwitchService) {
        this.dataSwitchService = dataSwitchService;
    }

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        // 检查数据开关状态
        boolean flag = dataSwitchService.findFlag();
        if (!flag) {
            throw new BussinessException("TDaaS已关闭了ETDS的工作");
        }
        return true;
    }
}
