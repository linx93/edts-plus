package com.phadata.etdsplus.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.mapper.EtdsMapper;
import com.phadata.etdsplus.service.EtdsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;


/**
 * <p>
 * license激活码 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */
@Service
public class EtdsServiceImpl extends ServiceImpl<EtdsMapper, Etds> implements EtdsService {

    @Value("${server.port}")
    private String port;


    @Value("${auth-center.app-key}")
    private String appKey;


    @Value("${auth-center.secret}")
    private String secret;

    @Override
    public void register(ETDSRegisterDTO etdsRegisterDTO) {
        //TODO 根绝激活码去调用杰哥鉴权中心的接口,返回相关信息进行保存

        //TODO 1. 去鉴权中心获取token
        HttpResponse getToken = HttpRequest.post("getToken").body("请求token的参数【appKey、secret】").execute();
        //TODO 2. 执行请求相关数据的接口 入参有激活码、本服务的ip+port
        etdsRegisterDTO.setEtdsUrl(etdsRegisterDTO.getEtdsUrl().trim());
        HttpResponse execute = HttpRequest.post("getInfo").header("token", "token").body(JSON.toJSONString(etdsRegisterDTO)).execute();
        //TODO 3. 保存拿回来的数据

        // 4. 放入缓存
        SimpleCache.setCache(CacheEnum.ETDS.getCode(), execute.body());

        //TODO 5. 注册保存成功后，生成MQ的队列以及监听信息
    }

    @Override
    public Etds confirmActivate(String etdsCode) {
        Etds one = getOne(new QueryWrapper<Etds>().lambda().eq(Etds::getEtdsCode, etdsCode));

        //TODO 调用鉴权中心 通知激活成功


        return one;
    }
}
