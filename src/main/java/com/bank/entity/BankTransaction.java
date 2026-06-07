package com.bank.entity;

import lombok.Data;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Data
public class BankTransaction {
    private Long id;
    private Long accountId;
    private Long userId;
    private Integer transType;
    private BigDecimal amount;
    private BigDecimal balanceAfter;
    private String remark;
    private LocalDateTime createTime;
}
