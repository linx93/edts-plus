package com.phadata.etdsplus.controller;

import com.phadata.etdsplus.entity.dto.ConfirmActivateEtdsDTO;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.entity.dto.SyncPrivateKeyDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.TdaasPrivateKey;
import com.phadata.etdsplus.entity.res.HeartbeatResponse;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.phadata.etdsplus.service.TdaasPrivateKeyService;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.Date;
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
    private final TdaasPrivateKeyService tdaasPrivateKeyService;
    private final EtdsUtil etdsUtil;

    public TdaasController(EtdsService etdsService, EtdsStatusRecordService etdsStatusRecordService, TdaasPrivateKeyService tdaasPrivateKeyService, EtdsUtil etdsUtil) {
        this.etdsService = etdsService;
        this.etdsStatusRecordService = etdsStatusRecordService;
        this.tdaasPrivateKeyService = tdaasPrivateKeyService;
        this.etdsUtil = etdsUtil;
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
    @PostMapping(value = "/confirm-activate-etds")
    @ApiOperation(value = "提供给tdaas确认激活")
    public Result<Etds> confirmActivate(@Valid @RequestBody ConfirmActivateEtdsDTO confirmActivateEtdsDTO) {
        Etds one = etdsService.confirmActivate(confirmActivateEtdsDTO.getEtdsCode());
        return Result.success(one);
    }

    /**
     * 提供给tdaas的心跳检测接口
     *
     * @return
     */
    @PostMapping(value = "/etds-heartbeat")
    @ApiOperation(value = "提供给tdaas的心跳检测接口")
    public Result<HeartbeatResponse> heartbeat() {
        Etds etds = etdsUtil.EtdsInfo(etdsService);
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

    /**
     * 提供给tdaas同步pubKey和priKey到etds中的接口
     *
     * @return
     */
    @PostMapping(value = "/sync-private-key")
    @ApiOperation(value = "提供给tdaas同步pubKey和priKey到etds中的接口")
    public Result syncPrivateKey(@Valid @RequestBody SyncPrivateKeyDTO syncPrivateKeyDTO) {
        boolean save = tdaasPrivateKeyService.save(TdaasPrivateKey.builder()
                .companyDtid(syncPrivateKeyDTO.getCompanyDtid())
                .privateKey(syncPrivateKeyDTO.getPrivateKey())
                .publicKey(syncPrivateKeyDTO.getPublicKey())
                .createTime(new Date()).build());
        if (!save) {
            Result.failed("保存失败");
        }
        return Result.success();
    }


}
