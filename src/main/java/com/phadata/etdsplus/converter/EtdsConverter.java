package com.phadata.etdsplus.converter;

import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.vo.EtdsVO;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

/**
 * Etds相关映射
 *
 * @author linx
 * @since 2021-12-31 09:11
 */
@Mapper(componentModel = "spring")
public interface EtdsConverter {
    /**
     * 映射
     *
     * @param etds Etds
     * @return EtdsVO
     */
    @Mapping(source = "licenseExpirationTime", target = "expirationTime")
    EtdsVO etds2EtdsVO(Etds etds);
}
