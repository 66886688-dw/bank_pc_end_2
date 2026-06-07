package com.bank.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BankAccount {
    private Long id;
    private Long userId;
    private String accountNo;
    private BigDecimal balance;
    private Integer status;
    private LocalDateTime createTime;
    private LocalDateTime updateTime;
}
