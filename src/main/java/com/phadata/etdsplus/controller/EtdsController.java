package com.phadata.etdsplus.controller;


import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.vo.DataApplyAuthVO;
import com.phadata.etdsplus.entity.vo.DataProvideAuthVO;
import com.phadata.etdsplus.entity.vo.PageInfo;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.GrantResultApply4Service;
import com.phadata.etdsplus.service.GrantResultProvide6Service;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.List;

/**
 * <p>
 * license激活码 前端控制器
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */
@Slf4j
@Validated
@CrossOrigin
@RestController
@RequestMapping("api/v1")
public class EtdsController {

    private final EtdsService etdsService;
    private final GrantResultApply4Service grantResultApply4Service;
    private final GrantResultProvide6Service grantResultProvide6Service;

    public EtdsController(EtdsService etdsService, GrantResultApply4Service grantResultApply4Service, GrantResultProvide6Service grantResultProvide6Service) {
        this.etdsService = etdsService;
        this.grantResultApply4Service = grantResultApply4Service;
        this.grantResultProvide6Service = grantResultProvide6Service;
    }

    /**
     * 激活etds
     *
     * @param etdsRegisterDTO
     * @return
     */
    @PostMapping(value = "/etds/activation-etds")
    @ApiOperation(value = "激活etds")
    public Result activationEtds(@Valid @RequestBody ETDSRegisterDTO etdsRegisterDTO) {
        etdsService.activationEtds(etdsRegisterDTO);
        return Result.success();
    }


    /**
     * 提供给鉴权中心同步etds信息到etds中
     *
     * @param etdsInfo
     * @return
     */
    @PostMapping(value = "/etds/sync-etds-info")
    @ApiOperation(value = "提供给鉴权中心同步etds信息到etds中")
    public Result syncEtdsInfo(@RequestBody JSONObject etdsInfo) {
        etdsService.syncEtdsInfo(etdsInfo);
        return Result.success();
    }

    /**
     * 授权凭证列表（数据提供方）
     *
     * @param
     * @return
     */
    @PostMapping(value = "/etds/data-provide/auth-list")
    @ApiOperation(value = "授权凭证列表（数据提供方）")
    public Result<PageInfo<List<DataProvideAuthVO>>> provideAuthList(@ApiParam(value = "页码（默认1）", name = "page") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                     @ApiParam(value = "页数（默认5）", name = "size") @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageInfo<List<DataProvideAuthVO>> list;
        try {
            list = grantResultProvide6Service.listAuthList(page, size);
        } catch (Exception e) {
            log.error("授权凭证列表（数据提供方）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(list);
    }

    /**
     * 授权凭证列表（数据请求方）
     *
     * @param
     * @return
     */
    @PostMapping(value = "/etds/data-apply/auth-list")
    @ApiOperation(value = "授权凭证列表（数据请求方）")
    public Result<PageInfo<List<DataApplyAuthVO>>> applyAuthList(@ApiParam(value = "页码（默认1）", name = "page") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                       @ApiParam(value = "页数（默认5）", name = "size") @RequestParam(value = "size", defaultValue = "5") Integer size) {
        PageInfo<List<DataApplyAuthVO>> list;
        try {
            list = grantResultApply4Service.listAuthList(page, size);
        } catch (Exception e) {
            log.error("查询授权凭证列表（数据请求方）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(list);
    }


}

