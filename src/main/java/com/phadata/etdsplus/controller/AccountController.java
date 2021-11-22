package com.phadata.etdsplus.controller;


import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.po.Account;
import com.phadata.etdsplus.entity.dto.LoginDTO;
import com.phadata.etdsplus.service.AccountService;
import com.phadata.etdsplus.utils.BCryptPasswordEncoder;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.validation.Valid;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * <p>
 * 用户/账户表 前端控制器
 * </p>
 *
 * @author linx
 * @since 2021-11-15
 */
@Slf4j
@CrossOrigin
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;

    public AccountController(AccountService accountService) {
        this.accountService = accountService;
    }

    /**
     * 登陆
     *
     * @param loginDTO
     * @return
     */
    @PostMapping(value = "login")
    @ApiOperation(value = "登陆")
    public Result login(@Valid @RequestBody LoginDTO loginDTO) {
        List<Account> list = accountService.list(new QueryWrapper<Account>().lambda().eq(Account::getAccount, loginDTO.getAccount()));
        if (list.isEmpty()) {
            return Result.failed("用户名或密码错误");
        }
        if (list.size() > 1) {
            return Result.failed("存在多个相同账户，登陆失败");
        }
        boolean matches = new BCryptPasswordEncoder().matches(loginDTO.getPassword(), list.get(0).getPassword());
        if (!matches) {
            return Result.failed("用户名或密码错误");
        }
        Map<String, Object> map = new HashMap<>(8);
        map.put("iss", loginDTO.getAccount());
        String jwtToken = JwtUtil.createJwtToken(map);
        log.info("{} 登陆成功:{}", loginDTO.getAccount(), jwtToken);
        return Result.success(jwtToken);
    }

    /**
     * 修改密码
     *
     * @param
     * @return
     */
    @PostMapping(value = "update-pwd")
    @ApiOperation(value = "修改密码")
    public Result updatePassword(@Valid @RequestBody LoginDTO loginDTO) {
        Account one = accountService.getOne(new QueryWrapper<Account>().lambda().eq(Account::getAccount, loginDTO.getAccount()));
        if (one == null) {
            return Result.failed("用户名不存在");
        }
        String newPassword = new BCryptPasswordEncoder().encode(loginDTO.getPassword());
        Account account = new Account();
        account.setId(one.getId());
        account.setPassword(newPassword);
        boolean b = accountService.updateById(account);
        if (!b) {
            return Result.failed("修改密码失败");
        }
        return Result.success(null);
    }

}

