package com.bank.dao;

import com.bank.entity.BankAccount;
import org.apache.ibatis.annotations.Mapper;
import org.apache.ibatis.annotations.Param;
import java.math.BigDecimal;

@Mapper
public interface BankAccountMapper {
    int insert(BankAccount account);
    BankAccount selectById(@Param("id") Long id);
    BankAccount selectByUserId(@Param("userId") Long userId);
    BankAccount selectByAccountNo(@Param("accountNo") String accountNo);
    int updateBalance(@Param("id") Long id, @Param("balance") BigDecimal balance);
}
