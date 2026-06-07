package com.bank.service.impl;

import com.bank.dao.SysUserMapper;
import com.bank.dto.RegisterDTO;
import com.bank.entity.SysUser;
import com.bank.service.AccountService;
import com.bank.service.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class UserServiceImpl implements UserService {

    private static final Logger log = LoggerFactory.getLogger(UserServiceImpl.class);

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private AccountService accountService;

    @Override
    @Transactional(rollbackFor = Exception.class)
    public SysUser register(RegisterDTO dto) {
        log.info("用户注册开始，姓名：{}，身份证：{}，手机号：{}", dto.getName(), dto.getIdCard(), dto.getPhone());

        SysUser existUser = sysUserMapper.selectByIdCard(dto.getIdCard());
        if (existUser != null) {
            log.warn("身份证号已注册：{}", dto.getIdCard());
            throw new RuntimeException("该身份证号已注册");
        }

        existUser = sysUserMapper.selectByPhone(dto.getPhone());
        if (existUser != null) {
            log.warn("手机号已注册：{}", dto.getPhone());
            throw new RuntimeException("该手机号已注册");
        }

        SysUser user = new SysUser();
        user.setName(dto.getName());
        user.setIdCard(dto.getIdCard());
        user.setPhone(dto.getPhone());
        user.setTradePassword(dto.getTradePassword());

        sysUserMapper.insert(user);
        log.info("用户信息插入成功，用户ID：{}", user.getId());

        accountService.createAccount(user.getId());
        log.info("用户账户创建成功，用户ID：{}", user.getId());

        return user;
    }

    @Override
    public SysUser getById(Long id) {
        return sysUserMapper.selectById(id);
    }
}
