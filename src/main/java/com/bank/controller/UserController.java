package com.bank.controller;

import com.bank.common.SessionManager;
import com.bank.common.UserContext;
import com.bank.dto.LoginDTO;
import com.bank.dto.RegisterDTO;
import com.bank.dto.Result;
import com.bank.entity.SysUser;
import com.bank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/user")
public class UserController {

    private static final Logger log = LoggerFactory.getLogger(UserController.class);

    @Autowired
    private UserService userService;

    @Autowired
    private SessionManager sessionManager;

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

    @PostMapping("/login")
    public Result<Map<String, Object>> login(@Validated @RequestBody LoginDTO dto) {
        log.info("用户登录请求，账号：{}", dto.getAccount());
        try {
            String token = userService.login(dto);
            Map<String, Object> data = new HashMap<>();
            data.put("token", token);
            return Result.success("登录成功", data);
        } catch (Exception e) {
            log.error("登录失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/logout")
    public Result<Void> logout(@RequestHeader(value = "Authorization", required = false) String token) {
        try {
            if (token != null && token.startsWith("Bearer ")) {
                token = token.substring(7);
            }
            if (token != null) {
                sessionManager.removeSession(token);
            }
            return Result.success("登出成功");
        } catch (Exception e) {
            log.error("登出失败", e);
            return Result.error(e.getMessage());
        }
    }

    @GetMapping("/info")
    public Result<SysUser> getCurrentUser() {
        try {
            SysUser user = UserContext.getUser();
            if (user == null) {
                return Result.error("用户未登录");
            }
            return Result.success(user);
        } catch (Exception e) {
            log.error("获取用户信息失败", e);
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
