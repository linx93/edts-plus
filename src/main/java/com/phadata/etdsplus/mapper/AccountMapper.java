package com.phadata.etdsplus.mapper;

import com.phadata.etdsplus.entity.po.Account;
import com.baomidou.mybatisplus.core.mapper.BaseMapper;
import org.apache.ibatis.annotations.Mapper;

/**
 * <p>
 * 用户/账户表 Mapper 接口
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Mapper
public interface AccountMapper extends BaseMapper<Account> {

}
