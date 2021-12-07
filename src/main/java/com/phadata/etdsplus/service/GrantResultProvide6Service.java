package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.GrantResultProvide6;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phadata.etdsplus.entity.vo.DataProvideAuthVO;
import com.phadata.etdsplus.entity.vo.LineChartVO;
import com.phadata.etdsplus.entity.vo.PageInfo;

import java.util.List;

/**
 * <p>
 * 授权结果返回表(提供方) <6> 服务类
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
public interface GrantResultProvide6Service extends IService<GrantResultProvide6> {

    /**
     * 授权凭证列表（数据提供方）
     *
     * @param page
     * @param size
     * @return
     */
    PageInfo<List<DataProvideAuthVO>> listAuthList(Integer page, Integer size);
}
