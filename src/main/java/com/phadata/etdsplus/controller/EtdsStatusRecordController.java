package com.phadata.etdsplus.controller;


import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import org.springframework.web.bind.annotation.RestController;

import javax.validation.Valid;

/**
 * <p>
 * tdaas对etds暂停/恢复的操作记录表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Slf4j
@RestController
@RequestMapping("/api/v1/tdaas/etds-status")
public class EtdsStatusRecordController {

    private final EtdsStatusRecordService etdsStatusRecordService;

    public EtdsStatusRecordController(EtdsStatusRecordService etdsStatusRecordService) {
        this.etdsStatusRecordService = etdsStatusRecordService;
    }



}