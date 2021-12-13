package com.phadata.etdsplus.service.impl;

import com.baomidou.mybatisplus.core.conditions.query.LambdaQueryWrapper;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.baomidou.mybatisplus.extension.plugins.pagination.Page;
import com.phadata.etdsplus.entity.po.ReportApply11;
import com.phadata.etdsplus.entity.vo.*;
import com.phadata.etdsplus.exception.BussinessException;
import com.phadata.etdsplus.mapper.ReportApply11Mapper;
import com.phadata.etdsplus.service.ReportApply11Service;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicReference;

/**
 * <p>
 * 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-12-07
 */
@Service
public class ReportApply11ServiceImpl extends ServiceImpl<ReportApply11Mapper, ReportApply11> implements ReportApply11Service {
    private final ReportApply11Mapper reportApply11Mapper;

    public ReportApply11ServiceImpl(ReportApply11Mapper reportApply11Mapper) {
        this.reportApply11Mapper = reportApply11Mapper;
    }


    @Override
    public LineChartVO dataApplyLine(Integer days, String authDtcId) {
        //1.初始化自己15天
        List<Tj15> tj15s = initDays(days);
        //2.填充Tj15的数据
        if (tj15s.isEmpty()) {
            throw new BussinessException("天数不能小与0");
        }
        //15天总分片
        AtomicReference<Integer> totalSum = new AtomicReference<>(0);
        //15天总数据大小
        AtomicReference<Long> sizeSum = new AtomicReference<>(0L);
        List<Tj15> tj15List = reportApply11Mapper.dataApplyLine(days, authDtcId);
        tj15s.forEach(tj15 -> tj15List.forEach(ele -> {
            if (tj15.getTime().equals(ele.getTime())) {
                Integer totals = ele.getTotals();
                totalSum.updateAndGet(v -> v + totals);
                Long size = ele.getSize();
                sizeSum.updateAndGet(v -> v + size);
                tj15.setSize(size);
                tj15.setTotals(totals);
            }
        }));
        Sjltj sjltj = reportApply11Mapper.dataApplySum(authDtcId);
        LineChartVO lineChartVO = new LineChartVO().setSjltj(sjltj)
                .setTj(new LineChartVO.Tj().setTotal(totalSum.get()).setTotalSize(sizeSum.get()).setLineChart(tj15s));
        return lineChartVO;
    }

    @Override
    public PageInfo<List<ReportApply11>> applyMoveLogs(Integer page, Integer size, String authDtcId) {
        LambdaQueryWrapper<ReportApply11> eq = new QueryWrapper<ReportApply11>().lambda().eq(ReportApply11::getAuthDtc, authDtcId).orderByDesc(ReportApply11::getRequestedAt);
        Page<ReportApply11> applyPage = reportApply11Mapper.selectPage(new Page<>(page, size), eq);
        List<ReportApply11> records = applyPage.getRecords();
        PageInfo<List<ReportApply11>> listPageInfo = new PageInfo<List<ReportApply11>>()
                .setTotal(applyPage.getTotal())
                .setSize(size)
                .setCurrent(page)
                .setRecords(records);
        return listPageInfo;
    }

    private List<Tj15> initDays(Integer days) {
        //获取前一天
        ArrayList<Tj15> list = new ArrayList<>(days);
        for (int i = -(days - 1); i <= 0; i++) {
            String dataStr = LocalDate.now().plusDays(i).format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
            list.add(new Tj15().setTime(dataStr).setSize(0L).setTotals(0));
        }
        return list;
    }
}
