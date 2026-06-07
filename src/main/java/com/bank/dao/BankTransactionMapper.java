package com.bank.dao;

import com.bank.entity.BankTransaction;
import org.apache.ibatis.annotations.Mapper;

@Mapper
public interface BankTransactionMapper {
    int insert(BankTransaction transaction);
}
