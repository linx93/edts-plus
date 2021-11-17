package com.phadata.etdsplus.interceptor;


import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.AKUtil;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.method.HandlerMethod;
import org.springframework.web.servlet.HandlerInterceptor;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import java.util.List;
import java.util.Objects;


/**
 * tdaas拦截器
 *
 * @author linx
 */
@Slf4j
public class TdaasInterceptor implements HandlerInterceptor {

    private final EtdsService etdsService;

    public TdaasInterceptor(EtdsService etdsService) {
        this.etdsService = etdsService;
    }


    @Override
    public boolean preHandle(HttpServletRequest httpServletRequest, HttpServletResponse httpServletResponse, Object object) throws Exception {

        // 如果不是映射到方法直接通过
        if (!(object instanceof HandlerMethod)) {
            return true;
        }

        //从请求头中取出 appKey
        String appKey = httpServletRequest.getHeader("appKey");
        //从请求头中取出 sign
        String sign = httpServletRequest.getHeader("sign");
        //从请求头中取出 timestamp
        String timestamp = httpServletRequest.getHeader("timestamp");

        if (StringUtils.isBlank(appKey)) {
            throw new BussinessException("appKey不能为空");
        }
        if (StringUtils.isBlank(sign)) {
            throw new BussinessException("sign不能为空");
        }
        if (StringUtils.isBlank(timestamp)) {
            throw new BussinessException("timestamp不能为空");
        }

        //1. 校验时间戳是不是前后5分钟
       /* if (!AKUtil.checkTimestamp(Long.valueOf(timestamp))) {
            throw new BussinessException("sign已过期");
        }*/
        //2. 校验appKey是否存在
        Etds etds = getEtds();
        if (!checkAppKey(appKey, etds)) {
            throw new BussinessException("appKey无效");
        }
        //3. 校验sign是否有效
        if (!AKUtil.checkSign(sign, appKey, etds.getAppSecret(), timestamp)) {
            throw new BussinessException("sign无效");
        }
        return true;
    }

    /**
     * 获取etds
     *
     * @return
     */
    private Etds getEtds() {
        String etdsStr = SimpleCache.getCache(CacheEnum.ETDS.getCode());
        Etds etds = StringUtils.isBlank(etdsStr) ? null : JSON.parseObject(etdsStr, Etds.class);
        //缓存不存在查库
        if (etds == null) {
            List<Etds> list = etdsService.list();
            if (list.isEmpty()) {
                throw new BussinessException("appKey不存在");
            }
            if (list.size() > 1) {
                throw new BussinessException("存在多个etds错误");
            }
            etds = list.get(0);
        }
        return etds;
    }


    /**
     * 校验appKey
     *
     * @param etds
     * @param appKey
     * @return
     */
    private boolean checkAppKey(String appKey, Etds etds) {
        return Objects.equals(appKey, etds.getAppKey());
    }


}
