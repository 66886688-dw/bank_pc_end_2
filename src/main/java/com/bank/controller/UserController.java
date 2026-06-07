package com.bank.controller;

import com.bank.dto.RegisterDTO;
import com.bank.dto.Result;
import com.bank.entity.SysUser;
import com.bank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @PostMapping("/register")
    public Result<SysUser> register(@Validated @RequestBody RegisterDTO dto) {
        log.info("用户注册请求：{}", dto);
        try {
            SysUser user = userService.register(dto);
            user.setTradePassword(null);
            return Result.success("注册成功", user);
        } catch (Exception e) {
            log.error("注册失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/{id}")
    public Result<SysUser> getById(@PathVariable Long id) {
        try {
            SysUser user = userService.getById(id);
            if (user == null) {
                return Result.error("用户不存在");
            }
            user.setTradePassword(null);
            return Result.success(user);
        } catch (Exception e) {
            log.error("查询用户失败", e);
            return Result.error(e.getMessage());
        }
    }
}
