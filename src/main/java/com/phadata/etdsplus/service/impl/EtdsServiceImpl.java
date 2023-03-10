package com.phadata.etdsplus.service.impl;

import cn.hutool.http.HttpRequest;
import cn.hutool.http.HttpResponse;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.vo.FrontPageVO;
import com.phadata.etdsplus.entity.vo.Sjltj;
import com.phadata.etdsplus.entity.vo.Tj15;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.mapper.DataSwitchMapper;
import com.phadata.etdsplus.mapper.EtdsMapper;
import com.phadata.etdsplus.mapper.ReportApply11Mapper;
import com.phadata.etdsplus.mapper.ReportProvide11Mapper;
import com.phadata.etdsplus.mq.InitMQInfo;
import com.phadata.etdsplus.service.DTCComponent;
import com.phadata.etdsplus.service.DataSwitchService;
import com.phadata.etdsplus.service.EtdsService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import com.phadata.etdsplus.utils.AKUtil;
import com.phadata.etdsplus.utils.result.Result;
import com.phadata.etdsplus.utils.result.ResultCodeMessage;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


/**
 * <p>
 * license激活码 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */
@Slf4j
@Service
public class EtdsServiceImpl extends ServiceImpl<EtdsMapper, Etds> implements EtdsService {

    private final InitMQInfo initMQInfo;
    private final DataSwitchMapper dataSwitchMapper;
    private final DataSwitchService dataSwitchService;
    private final EtdsMapper etdsMapper;
    private final ReportApply11Mapper reportApply11Mapper;
    private final ReportProvide11Mapper reportProvide11Mapper;
    private final DTCComponent dtcComponent;

    @Value("${auth-center.app-key:}")
    private String appKey;

    @Value("${auth-center.secret:}")
    private String secret;

    @Value("${auth-center.get-token-url:}")
    private String getTokenUrl;

    @Value("${auth-center.get-etds-info-url:}")
    private String getEtdsInfoUrl;

    @Value("${auth-center.notify-etds-activate-success:}")
    private String activateSuccessUrl;

    public EtdsServiceImpl(InitMQInfo initMQInfo, DataSwitchMapper dataSwitchMapper, DataSwitchService dataSwitchService, EtdsMapper etdsMapper, ReportApply11Mapper reportApply11Mapper, ReportProvide11Mapper reportProvide11Mapper, DTCComponent dtcComponent) {
        this.initMQInfo = initMQInfo;
        this.dataSwitchMapper = dataSwitchMapper;
        this.dataSwitchService = dataSwitchService;
        this.etdsMapper = etdsMapper;
        this.reportApply11Mapper = reportApply11Mapper;
        this.reportProvide11Mapper = reportProvide11Mapper;
        this.dtcComponent = dtcComponent;
    }

    @Override
    public void activationEtds(ETDSRegisterDTO etdsRegisterDTO) {
        //0. 先判断此服务是否以及激活，【是否已经存在etds信息】
        Integer count = etdsMapper.selectCount(new QueryWrapper<>());
        if (count > 0) {
            throw new BussinessException("此etds已经激活，不能重复激活");
        }
        //1. 去鉴权中心获取token
        String token = getToken();

        //2. 执行请求相关数据的接口 入参有激活码、本服务的etds的地址/可以是域名也可以是http://ip:port[http://192.168.1.44:18001]
        log.info("start->请求鉴权中心获取etds相关信息");
        etdsRegisterDTO.setEtdsUrl(etdsRegisterDTO.getEtdsUrl().trim());
        HttpResponse execute = HttpRequest.post(getEtdsInfoUrl).header("token", token).body(JSON.toJSONString(etdsRegisterDTO)).execute();
        Result<JSONObject> resultEtdsData = JSON.parseObject(execute.body(), Result.class);
        if (!ResultCodeMessage.SUCCESS.getCode().equals(resultEtdsData.getCode())) {
            log.info("请求鉴权中心获取etds相关信息接口失败---->{}:", resultEtdsData.toString());
            throw new BussinessException(resultEtdsData.getMessage());
        }
        JSONObject etdsInfo = resultEtdsData.getPayload();
        log.info("end->请求鉴权中心获取etds相关信息");
        log.info("请求鉴权中心获取etds相关信息:{}", JSON.toJSONString(etdsInfo, true));
        // 4. 默认插入,存在就更新
        dataSwitchService.updateDataSwitch(true);

        // 5. 注册保存成功后，生成MQ的队列以及binding信息
        initMQInfo.initMQInfo(this);
        // 6. mq信息生成后，对队列信息监听
        initMQInfo.executeListener(this);

        //7. 注册成为发行方
        dtcComponent.registerIssuer(this);
    }


    /**
     * 提供给鉴权中心同步etds信息到etds中
     *
     * @param etdsInfo
     */
    @Override
    public void syncEtdsInfo(JSONObject etdsInfo) {
        if (etdsInfo == null) {
            throw new BussinessException("鉴权中心推送给etds的数据为空");
        }
        log.info("鉴权中心推送给etds的数据:{}", etdsInfo);
        Etds etds = convertEtds(etdsInfo);
        boolean save = save(etds);
        if (!save) {
            throw new BussinessException("鉴权中心推送数据给etds但etds内部存库失败");
        }
        // 4. 放入缓存
        SimpleCache.setCache(CacheEnum.ETDS.getCode(), JSON.toJSONString(etds));
    }

    @Override
    public FrontPageVO dashboardStatistics(Integer months) {
        List<Tj15> tj15sApply = reportApply11Mapper.dataApplyLineAll(months);
        List<Tj15> tj15sProvide = reportProvide11Mapper.dataApplyLineAll(months);
        List<Tj15> tj15ApplyList = setData(months, tj15sApply);
        List<Tj15> tj15sProvideList = setData(months, tj15sProvide);
        Sjltj sjltjApply = reportApply11Mapper.dataApplySumAll();
        Sjltj sjltjProvide = reportProvide11Mapper.dataApplySumAll();
        FrontPageVO.Tj tjApply = new FrontPageVO.Tj().setTotal(sjltjApply.getTotals()).setTotalSize(sjltjApply.getSize()).setLineChart(tj15ApplyList);
        FrontPageVO.Tj tjProvide = new FrontPageVO.Tj().setTotal(sjltjProvide.getTotals()).setTotalSize(sjltjProvide.getSize()).setLineChart(tj15sProvideList);
        FrontPageVO frontPageVO = new FrontPageVO().setReceiving(tjApply).setTransmission(tjProvide);
        return frontPageVO;
    }

    private List<Tj15> setData(Integer months, List<Tj15> data) {
        List<Tj15> out = initMonths(months);
        out.forEach(tj15 -> data.forEach(ele -> {
            if (tj15.getTime().equals(ele.getTime())) {
                Integer totals = ele.getTotals();
                Long size = ele.getSize();
                tj15.setSize(size);
                tj15.setTotals(totals);
            }
        }));
        return out;
    }

    private static List<Tj15> initMonths(Integer months) {
        //获取前一天
        ArrayList<Tj15> list = new ArrayList<>(months);
        for (int i = -(months - 1); i <= 0; i++) {
            String dataStr = LocalDate.now().plusMonths(i).format(DateTimeFormatter.ofPattern("yyyy-MM"));
            list.add(new Tj15().setTime(dataStr).setSize(0L).setTotals(0));
        }
        return list;
    }

    /**
     * 将鉴权中心返回的etds相关信息转换成etds对象
     *
     * @param etdsInfo
     * @return
     */
    private Etds convertEtds(JSONObject etdsInfo) {
        String license = etdsInfo.getString("license");
        Long expirationTime = etdsInfo.getLong("expirationTime");
        String activationCode = etdsInfo.getString("activationCode");
        String appKey = etdsInfo.getString("appKey");
        String appSecret = etdsInfo.getString("appSecret");
        String companyName = etdsInfo.getString("companyName");
        String companyDtid = etdsInfo.getString("companyDtid");
        String description = etdsInfo.getString("description");
        String etdsCode = etdsInfo.getString("etdsCode");
        String etdsUrl = etdsInfo.getString("etdsUrl");
        String etdsName = etdsInfo.getString("etdsName");
        Etds build = Etds.builder().license(license)
                .licenseExpirationTime(expirationTime)
                .activationCode(activationCode)
                .appKey(appKey)
                .appSecret(appSecret)
                .companyDtid(companyDtid)
                .companyName(companyName)
                .description(description)
                .etdsCode(etdsCode)
                .etdsUrl(etdsUrl)
                .createTime(Instant.now().getEpochSecond())
                .etdsName(etdsName)
                //默认etds状态正常
                .state(0)
                .build();
        log.info("etds保存的信息:{}", build);
        return build;
    }

    @Override
    public Etds confirmActivate(String etdsCode) {
        Etds one = getOne(new QueryWrapper<Etds>().lambda().eq(Etds::getEtdsCode, etdsCode));
        Map<String, String> boyMap = new HashMap<>(8);
        boyMap.put("etdsCode", etdsCode);
        //调用鉴权中心 通知激活成功
        HttpResponse execute = HttpRequest.post(activateSuccessUrl).header("token", getToken()).body(JSON.toJSONString(boyMap)).execute();
        Result<JSONObject> result = JSON.parseObject(execute.body(), Result.class);
        if (!ResultCodeMessage.SUCCESS.getCode().equals(result.getCode())) {
            log.info("通知鉴权中心失败,鉴权中心返回:{}:", result.toString());
            throw new BussinessException(result.getMessage());
        }
        log.info("通知鉴权中心etds激活成功,鉴权中心返回:{}", execute.body());
        return one;
    }

    /**
     * 去鉴权中心获取token
     *
     * @return
     */
    public String getToken() {
        long epochSecond = Instant.now().getEpochSecond();
        String sign = AKUtil.sign(appKey, secret, String.valueOf(epochSecond));
        //获取缓存
        String cacheToken = SimpleCache.getCache(sign);
        String token;
        if (cacheToken != null && !"".equals(cacheToken.trim())) {
            token = cacheToken;
            log.info("获取缓存的鉴权中心提供的token");
        } else {
            log.info("start->请求鉴权中心获取token");
            HttpResponse getToken = HttpRequest.post(getTokenUrl)
                    .header("x-appKey", appKey)
                    .header("x-timestamp", String.valueOf(epochSecond))
                    .header("x-signature", sign).execute();
            Result<JSONObject> resultTokenData = JSON.parseObject(getToken.body(), Result.class);
            log.info("请求token的响应:{}", resultTokenData);
            if (!ResultCodeMessage.SUCCESS.getCode().equals(resultTokenData.getCode())) {
                log.info("请求鉴权中心获取token失败");
                throw new BussinessException("请求鉴权中心获取token失败");
            }
            token = resultTokenData.getPayload().getString("token");
            //设置缓存
            SimpleCache.setCache(sign, token);
            log.info("end->请求鉴权中心获取token");
            log.info("请求鉴权中心获得到的token:{}", token);
        }
        return token;
    }
}
