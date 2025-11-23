package com.lab2.service.strategy;

import com.lab2.model.Account;

import java.math.BigDecimal;

public interface TransactionStrategy {
    void execute(Account account, Account targetAccount, BigDecimal amount);
}
