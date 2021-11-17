package com.phadata.etdsplus.service.impl;

import com.phadata.etdsplus.entity.po.EtdsStatusRecord;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;
import com.phadata.etdsplus.localcache.CacheEnum;
import com.phadata.etdsplus.localcache.SimpleCache;
import com.phadata.etdsplus.mapper.EtdsStatusRecordMapper;
import com.phadata.etdsplus.service.EtdsStatusRecordService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
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
@Service
public class EtdsStatusRecordServiceImpl extends ServiceImpl<EtdsStatusRecordMapper, EtdsStatusRecord> implements EtdsStatusRecordService {
    private EtdsStatusRecordMapper etdsStatusRecordMapper;

    public EtdsStatusRecordServiceImpl(EtdsStatusRecordMapper etdsStatusRecordMapper) {
        this.etdsStatusRecordMapper = etdsStatusRecordMapper;
    }

    @Override
    public void stopETDS(OperateETDSDTO operateETDSDTO) {
        //TODO 1.检查发送和接收数据的状态
        //TODO 2.停止MQ的监听
        //TODO 3.停止发送数据
        //TODO 4.将etds状态修改为暂停

        //5.保存暂停etds的操作记录
        EtdsStatusRecord etdsStatusRecord = new EtdsStatusRecord();
        etdsStatusRecord.setCompanyDtid(operateETDSDTO.getCompanyDTID());
        etdsStatusRecord.setEtdsCode(operateETDSDTO.getEtdsCode());
        etdsStatusRecord.setType(1);
        etdsStatusRecord.setCreatedTime(new Date());
        save(etdsStatusRecord);
        //6. 缓存更新
        SimpleCache.setCache(CacheEnum.ETDS_STATUS.getCode(),"1");
    }

    @Override
    public void recoverETDS(OperateETDSDTO operateETDSDTO) {
        //TODO 1.恢复MQ的监听
        //TODO 2.恢复发送数据
        //TODO 3.将etds状态修改为正常
        //4.保存恢复etds的操作记录
        EtdsStatusRecord etdsStatusRecord = new EtdsStatusRecord();
        etdsStatusRecord.setCompanyDtid(operateETDSDTO.getCompanyDTID());
        etdsStatusRecord.setEtdsCode(operateETDSDTO.getEtdsCode());
        etdsStatusRecord.setType(0);
        etdsStatusRecord.setCreatedTime(new Date());
        save(etdsStatusRecord);
        //6. 缓存更新
        SimpleCache.setCache(CacheEnum.ETDS_STATUS.getCode(),"0");
    }

    @Override
    public String findStatus() {
        return etdsStatusRecordMapper.findStatus();
    }
}
