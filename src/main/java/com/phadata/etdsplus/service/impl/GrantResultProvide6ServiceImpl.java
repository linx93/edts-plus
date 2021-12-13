package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.core.metadata.IPage;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phadata.etdsplus.entity.dto.Address;
import com.phadata.etdsplus.entity.po.GrantResultApply4;
import com.phadata.etdsplus.entity.po.GrantResultProvide6;
import com.phadata.etdsplus.entity.vo.*;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.GrantResultProvide6Mapper;
import com.phadata.etdsplus.mapper.ReportProvide11Mapper;
import com.phadata.etdsplus.service.GrantResultProvide6Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 授权结果返回表(提供方) <6> 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Service
public class GrantResultProvide6ServiceImpl extends ServiceImpl<GrantResultProvide6Mapper, GrantResultProvide6> implements GrantResultProvide6Service {
    private final ReportProvide11Mapper reportProvide11Mapper;

    public GrantResultProvide6ServiceImpl(ReportProvide11Mapper reportProvide11Mapper) {
        this.reportProvide11Mapper = reportProvide11Mapper;
    }


    @Override
    public PageInfo<List<DataProvideAuthVO>> listAuthList(Integer page, Integer size) {
        PageInfo<List<DataProvideAuthVO>> listPageInfo;
        List<DataProvideAuthVO> result = new ArrayList<>();
        IPage<GrantResultProvide6> pageInfo = page(new Page<>(page, size), new QueryWrapper<GrantResultProvide6>().lambda().orderByDesc(GrantResultProvide6::getId));
        List<GrantResultProvide6> records = pageInfo.getRecords();
        if (!records.isEmpty()) {
            records.forEach(GrantResultProvide6 -> result.add(convert(GrantResultProvide6)));
        }
        listPageInfo = new PageInfo<List<DataProvideAuthVO>>().setCurrent(page).setSize(size).setTotal(pageInfo.getTotal()).setRecords(result);
        return listPageInfo;
    }


    /**
     * 转换数据给前端
     *
     * @param grantResultProvide6
     * @return
     */
    private DataProvideAuthVO convert(GrantResultProvide6 grantResultProvide6) {
        DataProvideAuthVO dataProvideAuthVO = new DataProvideAuthVO();
        if (grantResultProvide6 == null) {
            return dataProvideAuthVO;
        }
        String grantDocument = grantResultProvide6.getGrantDocument();
        VerifiableClaim verifiableClaim = JSON.parseObject(grantDocument, VerifiableClaim.class);
        Map<String, Object> bizData = verifiableClaim.getCredentialSubject().getBizData();
        Object object = bizData.get("from");
        Address from = new Address();
        if (object != null) {
            from = JSON.parseObject(JSON.toJSONString(object), Address.class);
        }
        dataProvideAuthVO.setId(grantResultProvide6.getId());
        dataProvideAuthVO.setAuthDtcId(verifiableClaim.getId());
        dataProvideAuthVO.setCreateTime(verifiableClaim.getIssuanceDate().toEpochSecond());
        dataProvideAuthVO.setAuthDtid(verifiableClaim.getIssuer());
        dataProvideAuthVO.setAuthName(grantResultProvide6.getGrantName());
        dataProvideAuthVO.setApplyDtid(from.getTdaas());
        dataProvideAuthVO.setApplyName(grantResultProvide6.getApplyName());
        dataProvideAuthVO.setAuthDtcState(grantResultProvide6.getUseStatus());
        dataProvideAuthVO.setDesc(bizData.getOrDefault("desc", "").toString());
        return dataProvideAuthVO;
    }
}
