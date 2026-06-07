package com.bank.service;

import com.bank.dto.DepositDTO;
import com.bank.dto.WithdrawDTO;
import com.bank.entity.BankAccount;

import java.math.BigDecimal;

public interface AccountService {
    BankAccount createAccount(Long userId);
    BankAccount getByUserId(Long userId);
    BigDecimal getBalance(Long userId);
    BankAccount deposit(DepositDTO dto);
    BankAccount withdraw(WithdrawDTO dto);
}
