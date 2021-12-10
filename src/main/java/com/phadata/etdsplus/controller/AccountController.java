package com.phadata.etdsplus.controller;


import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.entity.po.Account;
import com.phadata.etdsplus.entity.dto.LoginDTO;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.service.AccountService;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.BCryptPasswordEncoder;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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
    private final EtdsService etdsService;

    public AccountController(AccountService accountService, EtdsService etdsService) {
        this.accountService = accountService;
        this.etdsService = etdsService;
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
        HashMap<String, Object> result = new HashMap<>(8);
        //查询etds表的信息
        List<Etds> etdsList = etdsService.list();
        result.put("token", jwtToken);
        result.put("account", loginDTO.getAccount());
        if (etdsList.isEmpty()) {
            result.put("active", false);
            result.put("company", null);
        } else {
            //倒序
            List<Etds> collect = etdsList.stream().sorted(Comparator.comparing(Etds::getId).reversed()).collect(Collectors.toList());
            Etds etds = collect.get(0);
            HashMap<String, Object> etdsMap = new HashMap<>(8);
            etdsMap.put("createTime", etds.getCreateTime());
            etdsMap.put("companyName", etds.getCompanyName());
            etdsMap.put("companyDtid", etds.getCompanyDtid());
            etdsMap.put("state", etds.getState());
            etdsMap.put("etdsUrl", etds.getEtdsUrl());
            etdsMap.put("description", etds.getDescription());
            etdsMap.put("etdsCode", etds.getEtdsCode());
            etdsMap.put("etdsName", etds.getEtdsName());
            result.put("active", true);
            result.put("company", etdsMap);
        }
        return Result.success(result);
    }

    /**
     * 刷新
     *
     * @return
     */
    @GetMapping(value = "refresh")
    @ApiOperation(value = "刷新")
    public Result refresh(HttpServletRequest httpServletRequest) {
        //查询etds表的信息
        List<Etds> etdsList = etdsService.list();
        //倒序
        List<Etds> collect = etdsList.stream().sorted(Comparator.comparing(Etds::getId).reversed()).collect(Collectors.toList());
        String token = httpServletRequest.getHeader("x-token");
        Map<String, Claim> stringClaimMap = JwtUtil.verifyToken(token);
        Claim iss = stringClaimMap.get("iss");
        String account = iss.asString();
        Map<String, Object> map = new HashMap<>(8);
        map.put("iss", account);
        String jwtToken = JwtUtil.createJwtToken(map);
        log.info("{} 登陆成功:{}", account, jwtToken);
        Map<String, Object> result = new HashMap<>(8);
        result.put("token", jwtToken);
        result.put("account", account);
        if (etdsList.isEmpty()) {
            result.put("active", false);
            result.put("company", null);
        } else {
            Etds etds = collect.get(0);
            HashMap<String, Object> etdsMap = new HashMap<>(8);
            etdsMap.put("createTime", etds.getCreateTime());
            etdsMap.put("companyName", etds.getCompanyName());
            etdsMap.put("companyDtid", etds.getCompanyDtid());
            etdsMap.put("state", etds.getState());
            etdsMap.put("etdsUrl", etds.getEtdsUrl());
            etdsMap.put("description", etds.getDescription());
            etdsMap.put("etdsCode", etds.getEtdsCode());
            etdsMap.put("etdsName", etds.getEtdsName());
            result.put("active", true);
            result.put("company", etdsMap);
        }
        return Result.success(result);
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
        //对比老密码是否一致
        boolean matches = new BCryptPasswordEncoder().matches(loginDTO.getOldPassword(), one.getPassword());
        if (!matches) {
            return Result.failed("旧密码错误");
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

