package com.bank.dao;

import com.bank.entity.SysUser;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;

@Mapper
public interface SysUserMapper {
    int insert(SysUser user);
    SysUser selectById(@Param("id") Long id);
    SysUser selectByIdCard(@Param("idCard") String idCard);
    SysUser selectByPhone(@Param("phone") String phone);
}
