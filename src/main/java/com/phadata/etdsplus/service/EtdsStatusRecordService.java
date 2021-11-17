package com.phadata.etdsplus.service;

import com.phadata.etdsplus.entity.po.EtdsStatusRecord;
import com.baomidou.mybatisplus.extension.service.IService;
import com.phadata.etdsplus.entity.dto.OperateETDSDTO;

/**
 * <p>
 * tdaas对etds暂停/恢复的操作记录表 服务类
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
public interface EtdsStatusRecordService extends IService<EtdsStatusRecord> {

    /**
     * 暂停ETDS
     *
     * @param operateETDSDTO
     */
    void stopETDS(OperateETDSDTO operateETDSDTO);

    /**
     * 恢复ETDS
     *
     * @param operateETDSDTO
     */
    void recoverETDS(OperateETDSDTO operateETDSDTO);

    /**
     * 获取最新的etds状态[0:正常  1:暂停]
     *
     * @return
     */
    String findStatus();
}
