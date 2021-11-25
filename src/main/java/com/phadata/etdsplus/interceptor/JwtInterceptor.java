package com.phadata.etdsplus.interceptor;


import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;


/**
 * jwtToken拦截器
 *
 * @author linx
 */
@Slf4j
public class JwtInterceptor implements HandlerInterceptor {

    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {
        //从http请求头中取出 token
        String token = httpServletRequest.getHeader("token");
        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }
        //如果token为空
        if (token == null) {
            throw new BussinessException("token不能为空");
        }
        //验证token
        JwtUtil.verifyToken(token);
        //最后直接返回true
        return true;
    }
}
