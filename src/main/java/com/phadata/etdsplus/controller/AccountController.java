package com.phadata.etdsplus.controller;


import com.auth0.jwt.interfaces.Claim;
import com.baomidou.mybatisplus.core.conditions.query.QueryWrapper;
import com.phadata.etdsplus.converter.EtdsConverter;
import com.phadata.etdsplus.entity.dto.LoginDTO;
import com.phadata.etdsplus.entity.dto.UpdatePasswordDTO;
import com.phadata.etdsplus.entity.po.Account;
import com.phadata.etdsplus.entity.po.Etds;
import com.phadata.etdsplus.entity.res.EtdsResponse;
import com.phadata.etdsplus.entity.vo.EtdsVO;
import com.phadata.etdsplus.service.AccountService;
import com.phadata.etdsplus.service.EtdsService;
import com.phadata.etdsplus.utils.BCryptPasswordEncoder;
import com.phadata.etdsplus.utils.jwt.JwtUtil;
import com.phadata.etdsplus.utils.result.Result;
import io.swagger.annotations.ApiOperation;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;
import java.util.Comparator;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

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
@AllArgsConstructor
@RestController
@RequestMapping("/api/v1/account")
public class AccountController {
    private final AccountService accountService;
    private final EtdsService etdsService;
    private final EtdsConverter etdsConverter;

    /**
     * 登陆
     *
     * @param loginDTO 登陆参数
     * @return result
     */
    @PostMapping(value = "login")
    @ApiOperation(value = "登陆")
    public Result<EtdsResponse> login(@Valid @RequestBody LoginDTO loginDTO) {
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
        //查询etds表的信息
        List<Etds> etdsList = etdsService.list();
        EtdsResponse result = new EtdsResponse();
        result.setToken(jwtToken);
        result.setAccount(loginDTO.getAccount());
        return getEtdsResponseResult(etdsList, result);
    }

    /**
     * 刷新
     *
     * @return result
     */
    @GetMapping(value = "refresh")
    @ApiOperation(value = "刷新")
    public Result<EtdsResponse> refresh(HttpServletRequest httpServletRequest) {
        //查询etds表的信息
        List<Etds> etdsList = etdsService.list();
        String token = httpServletRequest.getHeader("x-token");
        Map<String, Claim> stringClaimMap = JwtUtil.verifyToken(token);
        Claim iss = stringClaimMap.get("iss");
        String account = iss.asString();
        Map<String, Object> map = new HashMap<>(8);
        map.put("iss", account);
        String jwtToken = JwtUtil.createJwtToken(map);
        log.info("{} 刷新成功:{}", account, jwtToken);
        EtdsResponse result = new EtdsResponse();
        result.setToken(jwtToken);
        result.setAccount(account);
        return getEtdsResponseResult(etdsList, result);
    }

    private Result<EtdsResponse> getEtdsResponseResult(List<Etds> etdsList, EtdsResponse result) {
        if (etdsList.isEmpty()) {
            result.setActive(false);
            result.setCompany(null);
        } else {
            //倒序
            List<Etds> collect = etdsList.stream().sorted(Comparator.comparing(Etds::getId).reversed()).collect(Collectors.toList());
            Etds etds = collect.get(0);
            //转换实体
            EtdsVO etdsVO = etdsConverter.etds2EtdsVO(etds);
            result.setActive(true);
            result.setCompany(etdsVO);
        }
        return Result.success(result);
    }


    /**
     * 修改密码
     *
     * @param updatePasswordDTO 更新密码参数
     * @return result
     */
    @PostMapping(value = "update-pwd")
    @ApiOperation(value = "修改密码")
    public Result<?> updatePassword(@Valid @RequestBody UpdatePasswordDTO updatePasswordDTO) {
        Account one = accountService.getOne(new QueryWrapper<Account>().lambda().eq(Account::getAccount, updatePasswordDTO.getAccount()));
        if (one == null) {
            return Result.failed("用户名不存在");
        }
        //对比老密码是否一致
        boolean matches = new BCryptPasswordEncoder().matches(updatePasswordDTO.getOldPassword(), one.getPassword());
        if (!matches) {
            return Result.failed("旧密码错误");
        }
        String newPassword = new BCryptPasswordEncoder().encode(updatePasswordDTO.getPassword());
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

