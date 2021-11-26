package com.phadata.etdsplus.mapper;

import com.phadata.etdsplus.entity.po.TdaasPrivateKey;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;
import org.springframework.stereotype.Component;

/**
 * <p>
 * tdaas的publicKey和privateKey
 * Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2021-11-17
 */
@Component
@Mapper
public interface TdaasPrivateKeyMapper extends BaseMapper<TdaasPrivateKey> {
    /**
     * 获取tdaas的公私钥信息
     *
     * @return
     */
    TdaasPrivateKey findPriInfo();
}
