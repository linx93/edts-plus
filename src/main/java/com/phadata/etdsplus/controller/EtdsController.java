package com.phadata.etdsplus.controller;


import com.alibaba.fastjson.JSONObject;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.dto.LoginDTO;
import com.phadata.etdsplus.entity.po.Account;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.BCryptPasswordEncoder;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import javax.validation.constraints.NotBlank;
import java.net.UnknownHostException;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * license激活码 前端控制器
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */
@Validated
@RestController
@RequestMapping("api/v1")
public class EtdsController {

    private final EtdsService etdsService;

    public EtdsController(EtdsService etdsService) {
        this.etdsService = etdsService;
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

}

