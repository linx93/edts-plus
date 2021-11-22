package com.phadata.etdsplus.mapper;

import com.phadata.etdsplus.entity.po.EtdsStatusRecord;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * tdaas对etds暂停/恢复的操作记录表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Mapper
public interface EtdsStatusRecordMapper extends BaseMapper<EtdsStatusRecord> {
    /**
     * 获取最新的etds状态[0:正常  1:暂停]
     *
     * @return
     */
    String findStatus();
}
