package com.bank.service;

import com.bank.dto.LoginDTO;
import com.bank.dto.RegisterDTO;
import com.bank.entity.SysUser;

public interface UserService {
    SysUser register(RegisterDTO dto);
    SysUser getById(Long id);
    String login(LoginDTO dto);
}
