package com.phadata.etdsplus.controller;


import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.po.ReportApply11;
import com.phadata.etdsplus.entity.po.ReportProvide11;
import com.phadata.etdsplus.entity.vo.*;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.service.*;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;
import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.ArrayList;
import java.util.Arrays;
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
    private final ReportApply11Service reportApply11Service;
    private final ReportProvide11Service reportProvide11Service;


    public EtdsController(EtdsService etdsService, GrantResultApply4Service grantResultApply4Service, GrantResultProvide6Service grantResultProvide6Service, ReportApply11Service reportApply11Service, ReportProvide11Service reportProvide11Service) {
        this.etdsService = etdsService;
        this.grantResultApply4Service = grantResultApply4Service;
        this.grantResultProvide6Service = grantResultProvide6Service;
        this.reportApply11Service = reportApply11Service;
        this.reportProvide11Service = reportProvide11Service;
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
    @GetMapping(value = "/etds/data-provide/auth-list")
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
    @GetMapping(value = "/etds/data-apply/auth-list")
    @ApiOperation(value = "授权凭证列表（数据请求方）")
    public Result<PageInfo<List<DataApplyAuthVO>>> applyAuthList(@ApiParam(value = "页码（默认1）", name = "page") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                 @ApiParam(value = "页数（默认5）", name = "size") @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                                 @ApiParam(value = "-1代表查询所有的状态（不传值默认为-1）| 授权凭证的状态[1: 已同意  2:已拒绝 4:已撤销]", name = "authDtcState") @RequestParam(value = "authDtcState", defaultValue = "-1") Integer flag) {
        PageInfo<List<DataApplyAuthVO>> list;
        try {
            ArrayList<Integer> flagList = new ArrayList<>(Arrays.asList(-1, 1, 2, 4));
            if (!flagList.contains(flag)) {
                return Result.failed("授权凭证的状态authDtcState只能传[1: 已同意  2:已拒绝 4:已撤销]");
            }
            list = grantResultApply4Service.listAuthList(page, size, flag);
        } catch (Exception e) {
            log.error("查询授权凭证列表（数据请求方）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(list);
    }


    /**
     * 最近15天授权凭证折线图（数据请求方）
     *
     * @param
     * @return
     */
    @GetMapping(value = "/etds/data-apply/line")
    @ApiOperation(value = "最近15天授权凭证折线图（数据请求方）")
    public Result<LineChartVO> dataApplyLine(@ApiParam(name = "days", value = "默认不传的话就是最近15天") @RequestParam(value = "days", defaultValue = "15", required = false) Integer days,
                                             @ApiParam(name = "authDtcId", value = "授权凭证的id", required = true) @RequestParam(value = "authDtcId") String authDtcId) {
        LineChartVO lineChartVO;
        try {
            lineChartVO = reportApply11Service.dataApplyLine(days, authDtcId);
        } catch (Exception e) {
            log.error("查询最近15天授权凭证折线图）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(lineChartVO);
    }


    /**
     * 最近15天授权凭证折线图（数据提供方）
     *
     * @param
     * @return
     */
    @GetMapping(value = "/etds/data-provide/line")
    @ApiOperation(value = "最近15天授权凭证折线图（数据提供方）")
    public Result<LineChartVO> dataProvideLine(@ApiParam(name = "days", value = "默认不传的话就是最近15天") @RequestParam(value = "days", defaultValue = "15", required = false) Integer days,
                                               @ApiParam(name = "authDtcId", value = "授权凭证的id", required = true) @RequestParam(value = "authDtcId") String authDtcId) {
        LineChartVO lineChartVO;
        try {
            lineChartVO = reportProvide11Service.dataProvideLine(days, authDtcId);
        } catch (Exception e) {
            log.error("查询最近15天授权凭证折线图）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(lineChartVO);
    }


    /**
     * 流转日志（数据请求方）
     *
     * @param
     * @return
     */
    @GetMapping(value = "/etds/apply/move-logs")
    @ApiOperation(value = "流转日志（数据请求方）")
    public Result<PageInfo<List<ReportApply11>>> applyMoveLogs(@ApiParam(value = "页码（默认1）", name = "page") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                               @ApiParam(value = "页数（默认5）", name = "size") @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                               @ApiParam(name = "authDtcId", value = "授权凭证的id", required = true) @RequestParam(value = "authDtcId") String authDtcId) {
        PageInfo<List<ReportApply11>> list;
        try {
            list = reportApply11Service.applyMoveLogs(page, size, authDtcId);
        } catch (Exception e) {
            log.error("查询授权凭证列表（数据请求方）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(list);
    }

    /**
     * 流转日志（数据供应方）
     *
     * @param
     * @return
     */
    @GetMapping(value = "/etds/provide/move-logs")
    @ApiOperation(value = "流转日志（数据供应方）")
    public Result<PageInfo<List<ReportProvide11>>> provideMoveLogs(@ApiParam(value = "页码（默认1）", name = "page") @RequestParam(value = "page", defaultValue = "1") Integer page,
                                                                   @ApiParam(value = "页数（默认5）", name = "size") @RequestParam(value = "size", defaultValue = "5") Integer size,
                                                                   @ApiParam(name = "authDtcId", value = "授权凭证的id", required = true) @RequestParam(value = "authDtcId") String authDtcId) {
        PageInfo<List<ReportProvide11>> list;
        try {
            list = reportProvide11Service.provideMoveLogs(page, size, authDtcId);
        } catch (Exception e) {
            log.error("查询授权凭证列表（数据供应方）失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(list);
    }


    /**
     * 流转日志（数据供应方）
     *
     * @param
     * @return
     */
    @GetMapping(value = "/dashboard/statistics")
    @ApiOperation(value = "首页的统计")
    public Result<FrontPageVO> dashboardStatistics(@ApiParam(name = "days", value = "默认不传的话就是最近15天") @RequestParam(value = "days", defaultValue = "15", required = false) Integer days) {
        FrontPageVO frontPageVO;
        try {
            frontPageVO = etdsService.dashboardStatistics(days);
        } catch (Exception e) {
            log.error("查询首页的统计数据失败:{}", e.getMessage());
            throw new BussinessException("查询失败");
        }
        return Result.success(frontPageVO);
    }

}

