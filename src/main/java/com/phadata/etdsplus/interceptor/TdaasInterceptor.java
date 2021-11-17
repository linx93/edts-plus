package com.phadata.etdsplus.interceptor;


import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.web.servlet.ModelAndView;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * tdaas拦截器
 * @author linx
 */
@Slf4j
public class TdaasInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //从请求头中取出 appKey
        //从请求头中取出 sign


        String appKey = httpServletRequest.getHeader("appKey");
        String sign = httpServletRequest.getHeader("sign");
        if(StringUtils.isBlank(appKey)){
            throw new BussinessException("appKey不能为空");
        }
        if(StringUtils.isBlank(appKey)){
            throw new BussinessException("sign不能为空");
        }

        //TODO 具体验证appKey和签名的逻辑  需要和杰哥、博哥一起确定


        return true;
    }
}
