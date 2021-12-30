package com.phadata.etdsplus.utils;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.stereotype.Component;

import java.util.List;

/**
 * 获取etds信息工具类
 * @author: xionglin
 * @since 2021-11-23 10:31
 */
@Component
public class EtdsUtil {
    public Etds EtdsInfo(EtdsService etdsService) {
        String etds = SimpleCache.getCache(CacheEnum.ETDS.getCode());
        Etds etdsInfo = StringUtils.isBlank(etds) ? null : JSON.parseObject(etds, Etds.class);
        //缓存不存在查库
        if (etdsInfo == null) {
            List<Etds> list = etdsService.list();
            if (list.isEmpty()) {
                throw new BussinessException("etds暂未注册");
            }
            if (list.size() > 1) {
                throw new BussinessException("存在多个etds错误");
            }
            etdsInfo = list.get(0);
            //放入缓存
            SimpleCache.setCache(CacheEnum.ETDS.getCode(), JSON.toJSONString(etdsInfo), 24 * 30);
        }
        return etdsInfo;
    }
}
