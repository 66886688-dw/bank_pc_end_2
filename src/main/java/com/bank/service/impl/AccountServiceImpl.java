package com.bank.service.impl;

import com.bank.dao.BankAccountMapper;
import com.bank.dao.BankTransactionMapper;
import com.bank.dao.SysUserMapper;
import com.bank.dto.DepositDTO;
import com.bank.dto.WithdrawDTO;
import com.bank.entity.BankAccount;
import com.bank.entity.BankTransaction;
import com.bank.entity.SysUser;
import com.bank.service.AccountService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Random;

@Service
public class AccountServiceImpl implements AccountService {

    private static final Logger log = LoggerFactory.getLogger(AccountServiceImpl.class);

    @Autowired
    private BankAccountMapper bankAccountMapper;

    @Autowired
    private SysUserMapper sysUserMapper;

    @Autowired
    private BankTransactionMapper bankTransactionMapper;

    @Override
    public BankAccount createAccount(Long userId) {
        String accountNo = generateAccountNo();
        BankAccount account = new BankAccount();
        account.setUserId(userId);
        account.setAccountNo(accountNo);
        account.setBalance(BigDecimal.ZERO);
        account.setStatus(1);
        bankAccountMapper.insert(account);
        log.info("账户创建成功，用户ID：{}，账号：{}", userId, accountNo);
        return account;
    }

    @Override
    public BankAccount getByUserId(Long userId) {
        return bankAccountMapper.selectByUserId(userId);
    }

    @Override
    public BigDecimal getBalance(Long userId) {
        BankAccount account = bankAccountMapper.selectByUserId(userId);
        if (account == null) {
            throw new RuntimeException("账户不存在");
        }
        return account.getBalance();
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankAccount deposit(DepositDTO dto) {
        log.info("存款开始，用户ID：{}，金额：{}", dto.getUserId(), dto.getAmount());

        SysUser user = sysUserMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getTradePassword().equals(dto.getTradePassword())) {
            log.warn("交易密码错误，用户ID：{}", dto.getUserId());
            throw new RuntimeException("交易密码错误");
        }

        BankAccount account = bankAccountMapper.selectByUserId(dto.getUserId());
        if (account == null) {
            throw new RuntimeException("账户不存在");
        }

        if (account.getStatus() != 1) {
            throw new RuntimeException("账户状态异常");
        }

        BigDecimal newBalance = account.getBalance().add(dto.getAmount());
        bankAccountMapper.updateBalance(account.getId(), newBalance);
        log.info("存款成功，用户ID：{}，原余额：{}，新余额：{}", dto.getUserId(), account.getBalance(), newBalance);

        BankTransaction transaction = new BankTransaction();
        transaction.setAccountId(account.getId());
        transaction.setUserId(dto.getUserId());
        transaction.setTransType(1);
        transaction.setAmount(dto.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setRemark("存款");
        bankTransactionMapper.insert(transaction);

        account.setBalance(newBalance);
        return account;
    }

    @Override
    @Transactional(rollbackFor = Exception.class)
    public BankAccount withdraw(WithdrawDTO dto) {
        log.info("取款开始，用户ID：{}，金额：{}", dto.getUserId(), dto.getAmount());

        SysUser user = sysUserMapper.selectById(dto.getUserId());
        if (user == null) {
            throw new RuntimeException("用户不存在");
        }

        if (!user.getTradePassword().equals(dto.getTradePassword())) {
            log.warn("交易密码错误，用户ID：{}", dto.getUserId());
            throw new RuntimeException("交易密码错误");
        }

        BankAccount account = bankAccountMapper.selectByUserId(dto.getUserId());
        if (account == null) {
            throw new RuntimeException("账户不存在");
        }

        if (account.getStatus() != 1) {
            throw new RuntimeException("账户状态异常");
        }

        if (account.getBalance().compareTo(dto.getAmount()) < 0) {
            log.warn("余额不足，用户ID：{}，当前余额：{}，取款金额：{}", dto.getUserId(), account.getBalance(), dto.getAmount());
            throw new RuntimeException("余额不足");
        }

        BigDecimal newBalance = account.getBalance().subtract(dto.getAmount());
        bankAccountMapper.updateBalance(account.getId(), newBalance);
        log.info("取款成功，用户ID：{}，原余额：{}，新余额：{}", dto.getUserId(), account.getBalance(), newBalance);

        BankTransaction transaction = new BankTransaction();
        transaction.setAccountId(account.getId());
        transaction.setUserId(dto.getUserId());
        transaction.setTransType(2);
        transaction.setAmount(dto.getAmount());
        transaction.setBalanceAfter(newBalance);
        transaction.setRemark("取款");
        bankTransactionMapper.insert(transaction);

        account.setBalance(newBalance);
        return account;
    }

    private String generateAccountNo() {
        String prefix = "622202";
        String datePart = LocalDateTime.now().format(DateTimeFormatter.ofPattern("yyyyMMdd"));
        Random random = new Random();
        int suffix = random.nextInt(1000000);
        return prefix + datePart + String.format("%06d", suffix);
    }
}
