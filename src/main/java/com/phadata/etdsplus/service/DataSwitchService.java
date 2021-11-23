package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.DataSwitch;
import com.baomidou.mybatisplus.extension.service.IService;

/**
 * <p>
 * 数据开关表，标志etds能否发送数据 服务类
 * </p>
 *
 * @author linx
 * @since 2021-11-23
 */
public interface DataSwitchService extends IService<DataSwitch> {

    /**
     * 开启或关闭数据发送控制的
     *
     * @param flag true:开启  false：关闭
     */
    void updateDataSwitch(boolean flag);

    /**
     * 获取当前数据库中 开关的状态 true:开启  false：关闭
     * @return
     */
    boolean findFlag();
}
