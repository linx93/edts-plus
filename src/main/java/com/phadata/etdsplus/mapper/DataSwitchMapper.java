package com.phadata.etdsplus.mapper;

import com.phadata.etdsplus.entity.po.DataSwitch;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 数据开关表，标志etds能否发送数据 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2021-11-23
 */
@Mapper
public interface DataSwitchMapper extends BaseMapper<DataSwitch> {

}
