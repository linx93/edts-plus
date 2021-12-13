package com.phadata.etdsplus.service;

import com.alibaba.fastjson.JSONObject;
import com.phadata.etdsplus.entity.dto.ETDSRegisterDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phadata.etdsplus.entity.vo.FrontPageVO;

import java.net.UnknownHostException;

/**
 * <p>
 * license激活码 服务类
 * </p>
 *
 * @author linx
 * @since 2021-11-16
 */
public interface EtdsService extends IService<Etds> {


    /**
     * etds注册
     *
     * @param etdsRegisterDTO
     * @throws UnknownHostException
     */
    void activationEtds(ETDSRegisterDTO etdsRegisterDTO);

    /**
     * 提供给tdaas确认激活
     *
     * @param etdsCode etds唯一吗
     * @return
     */
    Etds confirmActivate(String etdsCode);


    /**
     * 提供给鉴权中心同步etds信息到etds中
     *
     * @param etdsInfo
     */
    void syncEtdsInfo(JSONObject etdsInfo);


    /**
     * 首页的统计
     *
     * @param months
     * @return
     */
    FrontPageVO dashboardStatistics(Integer months);
}
