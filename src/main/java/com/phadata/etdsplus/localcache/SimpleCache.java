package com.phadata.etdsplus.localcache;

import cn.hutool.cache.Cache;
import cn.hutool.cache.CacheUtil;
import cn.hutool.core.date.DateUnit;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;


/**
 * @description: hutool-cache实现简单的本地缓存
 * @author: linx
 * @create: 2021-11-17 10:53
 */
@Slf4j
@Component
public class SimpleCache {
    private SimpleCache() {
    }

    /**
     * 先进先出的策略
     */
    private static Cache<String, String> cache = CacheUtil.newLFUCache(30);


    public static void setCache(String key, String obj) {
        //默认过期是3小时
        cache.put(key, obj, DateUnit.SECOND.getMillis() * 60 * 60 * 3);
    }

    /**
     * @param key
     * @param obj
     * @param hours 小时数
     */
    public static void setCache(String key, String obj, int hours) {
        cache.put(key, obj, DateUnit.SECOND.getMillis() * 60 * 60 * hours);
    }

    public static String getCache(String key) {
        return cache.get(key);
    }

    public static void removeCache(String key) {
        cache.remove(key);
    }

}
