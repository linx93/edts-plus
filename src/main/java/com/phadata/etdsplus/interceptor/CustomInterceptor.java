package com.phadata.etdsplus.interceptor;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.DataSwitchService;
import com.phadata.etdsplus.service.EtdsService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.time.Instant;


/**
 * 处理定制层请求的拦截器
 *
 * @author linx
 */
@Slf4j
public class CustomInterceptor implements HandlerInterceptor {

    private final DataSwitchService dataSwitchService;
    private final EtdsService etdsService;

    public CustomInterceptor(DataSwitchService dataSwitchService, EtdsService etdsService) {
        this.dataSwitchService = dataSwitchService;
        this.etdsService = etdsService;
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
        // 检查etds是否已经过期
        Etds one;
        try {
            one = etdsService.getOne(new QueryWrapper<>());
        } catch (Exception e) {
            throw new BussinessException("存在多条ETDS信息异常");
        }
        Long licenseExpirationTime = one.getLicenseExpirationTime();
        //当前时间大于过期时间代表已经过期
        if (Instant.now().getEpochSecond() - licenseExpirationTime >= 0) {
            throw new BussinessException("ETDS已过期，请联系管理员");
        }
        return true;
    }
}
