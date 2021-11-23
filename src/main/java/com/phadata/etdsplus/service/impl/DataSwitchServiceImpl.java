package com.phadata.etdsplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.po.DataSwitch;
import com.phadata.etdsplus.mapper.DataSwitchMapper;
import com.phadata.etdsplus.service.DataSwitchService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.util.Date;
import java.util.List;

/**
 * <p>
 * 数据开关表，标志etds能否发送数据 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-23
 */
@Service
public class DataSwitchServiceImpl extends ServiceImpl<DataSwitchMapper, DataSwitch> implements DataSwitchService {
    private final DataSwitchMapper dataSwitchMapper;

    public DataSwitchServiceImpl(DataSwitchMapper dataSwitchMapper) {
        this.dataSwitchMapper = dataSwitchMapper;
    }

    @Override
    public void updateDataSwitch(boolean flag) {
        List<DataSwitch> dataSwitches = dataSwitchMapper.selectList(new QueryWrapper<>());
        if (dataSwitches.isEmpty()) {
            DataSwitch dataSwitch = new DataSwitch();
            dataSwitch.setFlag(flag ? 0 : 1);
            dataSwitchMapper.insert(dataSwitch);
        } else {
            DataSwitch dataSwitch = dataSwitches.get(0);
            dataSwitch.setUpdateTime(new Date());
            dataSwitch.setFlag(flag ? 0 : 1);
            dataSwitchMapper.updateById(dataSwitch);
        }
    }

    @Override
    public boolean findFlag() {
        List<DataSwitch> dataSwitches = dataSwitchMapper.selectList(new QueryWrapper<>());
        if (dataSwitches.isEmpty()) {
            //如果不存在，默认就设置一个开启的开关
            DataSwitch dataSwitch = new DataSwitch();
            dataSwitch.setFlag(0);
            dataSwitchMapper.insert(dataSwitch);
            return true;
        } else {
            DataSwitch dataSwitch = dataSwitches.get(0);
            Integer flag = dataSwitch.getFlag();
            return flag == 0;
        }
    }
}
