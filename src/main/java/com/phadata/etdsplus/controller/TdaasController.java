package com.phadata.etdsplus.controller;

import com.alibaba.fastjson.JSON;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.res.HeartbeatResponse;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.util.List;

/**
 * @description: 提供给tdaas调用的接口
 * @author: linx
 * @create: 2021-11-17 10:25
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tdaas")
public class TdaasController {
    private final EtdsService etdsService;
    private final EtdsStatusRecordService etdsStatusRecordService;

    public TdaasController(EtdsService etdsService, EtdsStatusRecordService etdsStatusRecordService) {
        this.etdsService = etdsService;
        this.etdsStatusRecordService = etdsStatusRecordService;
    }


    /**
     * 暂停ETDS
     *
     * @return
     */
    @PostMapping(value = "stop")
    @ApiOperation(value = "暂停ETDS")
    public Result stopETDS(@Valid @RequestBody OperateETDSDTO operateETDSDTO) {
        etdsStatusRecordService.stopETDS(operateETDSDTO);
        return Result.success();
    }


    /**
     * 恢复ETDS
     *
     * @return
     */
    @PostMapping(value = "recover")
    @ApiOperation(value = "恢复ETDS")
    public Result recoverETDS(@Valid @RequestBody OperateETDSDTO operateETDSDTO) {
        etdsStatusRecordService.recoverETDS(operateETDSDTO);
        return Result.success();
    }

    /**
     * 提供给tdaas确认激活
     *
     * @return
     */
    @GetMapping(value = "/confirm-activate-etds")
    @ApiOperation(value = "提供给tdaas确认激活")
    public Result<Etds> confirmActivate(@NotBlank(message = "etdsCode不能为空") @ApiParam(value = "etds唯一吗", name = "etdsCode", required = true) String etdsCode) {
        Etds one = etdsService.confirmActivate(etdsCode);
        return Result.success(one);
    }

    /**
     * 提供给tdaas的心跳检测接口
     *
     * @return
     */
    @GetMapping(value = "/etds-heartbeat")
    @ApiOperation(value = "提供给tdaas确认激活")
    public Result<HeartbeatResponse> heartbeat() {
        String etdsStr = SimpleCache.getCache(CacheEnum.ETDS.getCode());
        Etds etds = JSON.parseObject(etdsStr, Etds.class);
        //缓存不存在查库
        if (etds == null) {
            List<Etds> list = etdsService.list();
            if (list.isEmpty()) {
                throw new BussinessException("etds暂未注册");
            }
            if (list.size() > 1) {
                throw new BussinessException("存在多个etds错误");
            }
            etds = list.get(0);
            //放入缓存
            SimpleCache.setCache(CacheEnum.ETDS.getCode(), JSON.toJSONString(etds), 24 * 30);
        }
        String etdsStatus = SimpleCache.getCache(CacheEnum.ETDS_STATUS.getCode());
        if (StringUtils.isBlank(etdsStatus)) {
            //缓存不存在查库
            etdsStatus = etdsStatusRecordService.findStatus() == null ? "0" : etdsStatusRecordService.findStatus();
        }
        HeartbeatResponse heartbeatResponse = new HeartbeatResponse();
        heartbeatResponse.setEtdsCode(etds.getEtdsCode());
        heartbeatResponse.setStatus(etdsStatus);
        return Result.success(heartbeatResponse);
    }


}
