package com.bank.controller;

import com.bank.dto.DepositDTO;
import com.bank.dto.Result;
import com.bank.dto.WithdrawDTO;
import com.bank.entity.BankAccount;
import com.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

import java.math.BigDecimal;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/account")
public class AccountController {

    private static final Logger log = LoggerFactory.getLogger(AccountController.class);

    @Autowired
    private AccountService accountService;

    @GetMapping("/balance/{userId}")
    public Result<Map<String, Object>> getBalance(@PathVariable Long userId) {
        log.info("查询余额，用户ID：{}", userId);
        try {
            BigDecimal balance = accountService.getBalance(userId);
            BankAccount account = accountService.getByUserId(userId);
            Map<String, Object> data = new HashMap<>();
            data.put("userId", userId);
            data.put("accountNo", account.getAccountNo());
            data.put("balance", balance);
            return Result.success(data);
        } catch (Exception e) {
            log.error("查询余额失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/deposit")
    public Result<BankAccount> deposit(@Validated @RequestBody DepositDTO dto) {
        log.info("存款请求，用户ID：{}，金额：{}", dto.getUserId(), dto.getAmount());
        try {
            BankAccount account = accountService.deposit(dto);
            return Result.success("存款成功", account);
        } catch (Exception e) {
            log.error("存款失败", e);
            return Result.error(e.getMessage());
        }
    }

    @PostMapping("/withdraw")
    public Result<BankAccount> withdraw(@Validated @RequestBody WithdrawDTO dto) {
        log.info("取款请求，用户ID：{}，金额：{}", dto.getUserId(), dto.getAmount());
        try {
            BankAccount account = accountService.withdraw(dto);
            return Result.success("取款成功", account);
        } catch (Exception e) {
            log.error("取款失败", e);
            return Result.error(e.getMessage());
        }
    }
}
