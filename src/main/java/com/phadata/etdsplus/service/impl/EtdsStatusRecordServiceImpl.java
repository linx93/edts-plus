package com.phadata.etdsplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.update.UpdateWrapper;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.po.EtdsStatusRecord;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.mapper.EtdsMapper;
import com.phadata.etdsplus.mapper.EtdsStatusRecordMapper;
import com.phadata.etdsplus.mq.InitMQInfo;
import com.phadata.etdsplus.service.DataSwitchService;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Date;

/**
 * <p>
 * tdaas对etds暂停/恢复的操作记录表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Slf4j
@Service
public class EtdsStatusRecordServiceImpl extends ServiceImpl<EtdsStatusRecordMapper, EtdsStatusRecord> implements EtdsStatusRecordService {
    private EtdsStatusRecordMapper etdsStatusRecordMapper;
    private final InitMQInfo initMQInfo;
    private final EtdsMapper etdsMapper;
    private final DataSwitchService dataSwitchService;
    private final EtdsService etdsService;


    public EtdsStatusRecordServiceImpl(EtdsStatusRecordMapper etdsStatusRecordMapper, InitMQInfo initMQInfo, EtdsMapper etdsMapper, DataSwitchService dataSwitchService, EtdsService etdsService) {
        this.etdsStatusRecordMapper = etdsStatusRecordMapper;
        this.initMQInfo = initMQInfo;
        this.etdsMapper = etdsMapper;
        this.dataSwitchService = dataSwitchService;
        this.etdsService = etdsService;
    }

    @Override
    public void stopETDS(OperateETDSDTO operateETDSDTO) {
        //0.判断ETDS是否处于正常状态，正常状态才能暂停
        String status = etdsStatusRecordMapper.findStatus() == null ? "0" : etdsStatusRecordMapper.findStatus();
        if (!"0".equals(status)) {
            throw new BussinessException("当前ETDS已经处于暂停状态，不能重复操作");
        }
        //payload 1.检查发送和接收数据的状态
        log.info("检查发送和接收数据的状态");
        // 2.停止MQ的binding
        initMQInfo.removeBinding(etdsService);
        // 3.将etds状态修改为暂停
        Etds etds = new Etds();
        etds.setState(1);
        int update = etdsMapper.update(etds, new UpdateWrapper<Etds>().lambda().eq(Etds::getEtdsCode, operateETDSDTO.getEtdsCode()));
        if (update < 1) {
            log.error("更新etds状态失败");
            //更新失败得移除绑定关系
            initMQInfo.initMQInfo(etdsService);
            throw new BussinessException("更新etds状态失败");
        }
        //4. 数据发送的开关 关闭
        dataSwitchService.updateDataSwitch(false);

        //5.保存暂停etds的操作记录
        EtdsStatusRecord etdsStatusRecord = new EtdsStatusRecord();
        etdsStatusRecord.setCompanyDtid(operateETDSDTO.getCompanyDtid());
        etdsStatusRecord.setEtdsCode(operateETDSDTO.getEtdsCode());
        etdsStatusRecord.setType(1);
        etdsStatusRecord.setCreatedTime(new Date());
        save(etdsStatusRecord);
        //6. 缓存更新
        SimpleCache.setCache(CacheEnum.ETDS_STATUS.getCode(), "1");
    }

    @Override
    public void recoverETDS(OperateETDSDTO operateETDSDTO) {
        //0.判断ETDS是否处于暂停状态，暂停状态才能恢复/启用
        String status = etdsStatusRecordMapper.findStatus();
        status = status == null ? "0" : status;
        if (!"1".equals(status)) {
            throw new BussinessException("当前ETDS已经处于正常状态，不能重复操作");
        }
        // 1.恢复MQ的binding
        initMQInfo.initMQInfo(etdsService);
        // 2.将etds状态修改为正常
        Etds etds = new Etds();
        etds.setState(0);
        int update = etdsMapper.update(etds, new UpdateWrapper<Etds>().lambda().eq(Etds::getEtdsCode, operateETDSDTO.getEtdsCode()));
        if (update < 1) {
            log.error("更新etds状态失败");
            //更新失败得移除绑定关系
            initMQInfo.removeBinding(etdsService);
            throw new BussinessException("更新etds状态失败");
        }
        //3. 数据发送的开关 开启
        dataSwitchService.updateDataSwitch(true);
        //4.保存恢复etds的操作记录
        EtdsStatusRecord etdsStatusRecord = new EtdsStatusRecord();
        etdsStatusRecord.setCompanyDtid(operateETDSDTO.getCompanyDtid());
        etdsStatusRecord.setEtdsCode(operateETDSDTO.getEtdsCode());
        etdsStatusRecord.setType(0);
        etdsStatusRecord.setCreatedTime(new Date());
        save(etdsStatusRecord);
        //6. 缓存更新
        SimpleCache.setCache(CacheEnum.ETDS_STATUS.getCode(), "0");
    }

    @Override
    public String findStatus() {
        return etdsStatusRecordMapper.findStatus();
    }
}
