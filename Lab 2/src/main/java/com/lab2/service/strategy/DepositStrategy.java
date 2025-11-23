package com.lab2.service.strategy;

import com.lab2.model.Account;

import java.math.BigDecimal;

public class DepositStrategy implements TransactionStrategy {
    @Override
    public void execute(Account account, Account targetAccount, BigDecimal amount) {
        account.setBalance(account.getBalance().add(amount));
    }
}
