package com.bank.entity;

import lombok.Data;
import java.time.LocalDateTime;

@Data
public class SysUser {
    private Long id;
    private String name;
    private String idCard;
    private String phone;
    private String tradePassword;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
