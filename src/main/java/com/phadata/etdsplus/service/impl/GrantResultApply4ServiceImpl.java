package com.phadata.etdsplus.service.impl;

import com.alibaba.fastjson.JSON;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phadata.etdsplus.entity.dto.Address;
import com.phadata.etdsplus.entity.po.GrantResultApply4;
import com.phadata.etdsplus.entity.vo.*;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.GrantResultApply4Mapper;
import com.phadata.etdsplus.mapper.ReportApply11Mapper;
import com.phadata.etdsplus.service.GrantResultApply4Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import net.phadata.identity.dtc.entity.VerifiableClaim;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 授权结果返回表(请求方) <4> 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-26
 */
@Service
public class GrantResultApply4ServiceImpl extends ServiceImpl<GrantResultApply4Mapper, GrantResultApply4> implements GrantResultApply4Service {

    @Override
    public PageInfo<List<DataApplyAuthVO>> listAuthList(Integer page, Integer size) {
        PageInfo<List<DataApplyAuthVO>> listPageInfo = new PageInfo<>();
        List<DataApplyAuthVO> result = new ArrayList<>();
        Page<GrantResultApply4> list = page(new Page<>(page, size));
        List<GrantResultApply4> records = list.getRecords();
        if (records.isEmpty()) {
            return listPageInfo;
        }
        records.forEach(grantResultApply4 -> result.add(convert(grantResultApply4)));
        listPageInfo = listPageInfo.setRecords(result).setSize(size).setTotal(list.getTotal()).setCurrent(page);
        return listPageInfo;
    }

    /**
     * 数据转换,转换给前端展示需要的数据
     *
     * @param grantResultApply4
     * @return
     */
    private DataApplyAuthVO convert(GrantResultApply4 grantResultApply4) {
        DataApplyAuthVO dataApplyAuthVO = new DataApplyAuthVO();
        if (grantResultApply4 == null) {
            return dataApplyAuthVO;
        }
        String grantDocument = grantResultApply4.getGrantDocument();
        VerifiableClaim verifiableClaim = JSON.parseObject(grantDocument, VerifiableClaim.class);
        Map<String, Object> bizData = verifiableClaim.getCredentialSubject().getBizData();
        Object object = bizData.get("cc");
        List<Address> cc = new ArrayList<>();
        if (object != null) {
            cc = JSON.parseArray(JSON.toJSONString(object), Address.class);
        }
        Integer result = Integer.valueOf(bizData.getOrDefault("result", "").toString());
        dataApplyAuthVO.setId(grantResultApply4.getId());
        dataApplyAuthVO.setAuthDtcId(verifiableClaim.getId());
        dataApplyAuthVO.setCreateTime(verifiableClaim.getIssuanceDate().toEpochSecond());
        dataApplyAuthVO.setAuthDtid(verifiableClaim.getCredentialSubject().getId());
        dataApplyAuthVO.setProvideDtid(cc.get(0).getTdaas());
        dataApplyAuthVO.setProvideEtdsCode(cc.get(0).getTdaas());
        dataApplyAuthVO.setAuthDtcState(result);
        return dataApplyAuthVO;
    }
}
