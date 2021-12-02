package com.phadata.etdsplus.controller;

import com.phadata.etdsplus.entity.dto.ApplyAuthDTO;
import com.phadata.etdsplus.entity.dto.ApplyDataDTO;
import com.phadata.etdsplus.entity.dto.ResponseDataDTO;
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
     * @param applyAuth
     * @return
     */
    @PostMapping(value = "/apply-auth")
    @ApiOperation(value = "申请授权")
    public Result<Boolean> applyAuth(@Valid @RequestBody ApplyAuthDTO applyAuth) {
        return customService.applyAuth(applyAuth);
    }


    /**
     * 申请数据
     *
     * @param applyData
     * @return
     */
    @PostMapping(value = "/apply-data")
    @ApiOperation(value = "申请数据")
    public Result applyData(@Valid @RequestBody ApplyDataDTO applyData) {
        return customService.applyData(applyData);
    }

    /**
     * 响应数据，这个接口是提供给定制层获取到真正数据时候调用的，用于接收定制层的数据
     *
     * @param responseData
     * @return
     */
    @PostMapping(value = "/receive-data")
    @ApiOperation(value = "响应数据")
    public Result receiveData(@Valid @RequestBody ResponseDataDTO responseData) {
        return customService.receiveData(responseData);
    }

    /**
     * 提供给定制层获取etds的唯一码和数字身份
     *
     * @param
     * @return
     */
    @GetMapping(value = "/find-etds-info")
    @ApiOperation(value = "提供给定制层获取etds的唯一码和数字身份")
    public Result findEtdsInfo() {
        return customService.findEtdsInfo();
    }
}

