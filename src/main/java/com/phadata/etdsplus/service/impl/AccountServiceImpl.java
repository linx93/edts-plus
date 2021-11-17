package com.phadata.etdsplus.service.impl;

import com.phadata.etdsplus.entity.po.Account;
import com.phadata.etdsplus.mapper.AccountMapper;
import com.phadata.etdsplus.service.AccountService;
import com.baomidou.mybatisplus.extension.service.impl.ServiceImpl;
import org.springframework.stereotype.Service;

/**
 * <p>
 * 用户/账户表 服务实现类
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Service
public class AccountServiceImpl extends ServiceImpl<AccountMapper, Account> implements AccountService {

}
