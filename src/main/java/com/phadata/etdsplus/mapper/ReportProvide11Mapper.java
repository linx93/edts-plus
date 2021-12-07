package com.phadata.etdsplus.mapper;

import com.phadata.etdsplus.entity.po.ReportProvide11;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import com.phadata.etdsplus.entity.vo.Sjltj;
import com.phadata.etdsplus.entity.vo.Tj15;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

import java.util.List;

/**
 * <p>
 *  Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2021-12-07
 */
@Mapper
public interface ReportProvide11Mapper extends BaseMapper<ReportProvide11> {

    /**
     * 最近15天授权凭证折线图（数据提供方）
     *
     * @param days
     * @param authDtcId
     * @return
     */
    List<Tj15> dataProvideLine(@Param("days") Integer days, @Param("authDtcId") String authDtcId);

    /**
     * 查询某个授权凭证所以的数据
     * @param authDtcId
     * @return
     */
    Sjltj dataProvideSum(@Param("authDtcId") String authDtcId);
}
