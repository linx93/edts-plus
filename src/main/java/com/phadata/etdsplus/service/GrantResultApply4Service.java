package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.GrantResultApply4;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phadata.etdsplus.entity.vo.DataApplyAuthVO;
import com.phadata.etdsplus.entity.vo.LineChartVO;
import com.phadata.etdsplus.entity.vo.PageInfo;

import java.util.List;

/**
 * <p>
 * 授权结果返回表(请求方) <4> 服务类
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
public interface GrantResultApply4Service extends IService<GrantResultApply4> {


    /**
     * 授权凭证列表
     *
     * @param page
     * @param size
     * @return
     */
    PageInfo<List<DataApplyAuthVO>> listAuthList(Integer page, Integer size);

}
