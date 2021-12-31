package com.phadata.etdsplus.controller;

import com.phadata.etdsplus.entity.dto.*;
import com.phadata.etdsplus.service.CustomService;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;

/**
 * <p>
 * 提供接口给定制层调用的控制器
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/custom")
public class CustomController {
    private final CustomService customService;

    public CustomController(CustomService customService) {
        this.customService = customService;
    }


    /**
     * 申请授权
     *
     * @param applyAuth 申请授权参数
     * @return result
     */
    @PostMapping(value = "/apply-auth")
    @ApiOperation(value = "申请授权")
    public Result<Boolean> applyAuth(@Valid @RequestBody ApplyAuthDTO applyAuth) {
        return customService.applyAuth(applyAuth);
    }


    /**
     * 申请数据
     *
     * @param applyData 申请数据参数
     * @return result true false
     */
    @PostMapping(value = "/apply-data")
    @ApiOperation(value = "申请数据")
    public Result<Boolean> applyData(@Valid @RequestBody ApplyDataDTO applyData) {
        return customService.applyData(applyData);
    }

    /**
     * 接收数据，这个接口是提供给定制层获取到真正数据时候调用的，用于接收定制层的数据
     *
     * @param responseData 接收数据参数
     * @return result true false
     */
    @PostMapping(value = "/receive-data")
    @ApiOperation(value = "接收数据")
    public Result<Boolean> receiveData(@Valid @RequestBody ResponseDataDTO responseData) {
        return customService.receiveData(responseData);
    }


    /**
     * 接收数据的统计信息，这个接口是提供给定制层获取到真正数据时候调用的，用于接收定制层数据的统计信息
     *
     * @param reportDTO 入参
     * @return result true false
     */
    @PostMapping(value = "/receive-statistics-data")
    @ApiOperation(value = "接收数据的统计信息")
    public Result<Boolean> receiveStatisticData(@RequestBody ReportDTO reportDTO) {
        return customService.receiveStatisticData(reportDTO);
    }

    /**
     * 提供给定制层获取etds的唯一码和数字身份
     *
     * @return
     */
    @GetMapping(value = "/find-etds-info")
    @ApiOperation(value = "提供给定制层获取etds的唯一码和数字身份")
    public Result findEtdsInfo() {
        return customService.findEtdsInfo();
    }
}

