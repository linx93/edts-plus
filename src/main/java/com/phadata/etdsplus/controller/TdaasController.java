package com.phadata.etdsplus.controller;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.phadata.etdsplus.entity.dto.AuthDtcDTO;
import com.phadata.etdsplus.entity.dto.ConfirmActivateEtdsDTO;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.entity.dto.SyncPrivateKeyDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.GrantResultProvide6;
import com.phadata.etdsplus.entity.po.TdaasPrivateKey;
import com.phadata.etdsplus.entity.res.HeartbeatResponse;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.phadata.etdsplus.service.GrantResultProvide6Service;
import com.phadata.etdsplus.service.TdaasPrivateKeyService;
import com.phadata.etdsplus.utils.EtdsUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.StringUtils;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
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
    private final GrantResultProvide6Service grantResultProvide6Service;

    public TdaasController(EtdsService etdsService, EtdsStatusRecordService etdsStatusRecordService, TdaasPrivateKeyService tdaasPrivateKeyService, EtdsUtil etdsUtil, GrantResultProvide6Service grantResultProvide6Service) {
        this.etdsService = etdsService;
        this.etdsStatusRecordService = etdsStatusRecordService;
        this.tdaasPrivateKeyService = tdaasPrivateKeyService;
        this.etdsUtil = etdsUtil;
        this.grantResultProvide6Service = grantResultProvide6Service;
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
        List<TdaasPrivateKey> list = tdaasPrivateKeyService.list();
        if (!list.isEmpty()) {
            Result.failed("数据已存在");
        }
        boolean save = tdaasPrivateKeyService.save(TdaasPrivateKey.builder()
                .companyDtid(syncPrivateKeyDTO.getCompanyDtid())
                .privateKey(syncPrivateKeyDTO.getPrivateKey())
                .publicKey(syncPrivateKeyDTO.getPublicKey())
                .safeCode(syncPrivateKeyDTO.getSafeCode())
                .createTime(new Date()).build());
        if (!save) {
            Result.failed("保存失败");
        }
        return Result.success();
    }


    /**
     * 提供给tdaas暂停某个授权凭证的接口
     *
     * @return
     */
    @PostMapping(value = "/stop-auth-dtc")
    @ApiOperation(value = "提供给tdaas暂停某个授权凭证的接口")
    public Result stopAuthDtc(@Valid @RequestBody AuthDtcDTO authDtcDTO) {
        GrantResultProvide6 grantResultProvide6 = new GrantResultProvide6();
        grantResultProvide6.setUseStatus(1);
        boolean update = grantResultProvide6Service.update(grantResultProvide6, new UpdateWrapper<GrantResultProvide6>().lambda().eq(GrantResultProvide6::getClaimId, authDtcDTO.getClaimId()));
        if (!update) {
            throw new BussinessException("操作失败!");
        }
        return Result.success();
    }

    /**
     * 提供给tdaas恢复某个授权凭证的接口
     *
     * @return
     */
    @PostMapping(value = "/recover-auth-dtc")
    @ApiOperation(value = "提供给tdaas恢复某个授权凭证的接口")
    public Result recoverAuthDtc(@Valid @RequestBody AuthDtcDTO authDtcDTO) {
        GrantResultProvide6 grantResultProvide6 = new GrantResultProvide6();
        grantResultProvide6.setUseStatus(0);
        boolean update = grantResultProvide6Service.update(grantResultProvide6, new UpdateWrapper<GrantResultProvide6>().lambda().eq(GrantResultProvide6::getClaimId, authDtcDTO.getClaimId()));
        if (!update) {
            throw new BussinessException("操作失败!");
        }
        return Result.success();
    }

}
