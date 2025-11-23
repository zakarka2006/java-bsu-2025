package com.lab2.service.strategy;

import com.lab2.model.Account;
import com.lab2.model.AccountStatus;

import java.math.BigDecimal;

public class WithdrawStrategy implements TransactionStrategy {
    @Override
    public void execute(Account account, Account targetAccount, BigDecimal amount) {
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Account is frozen");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
    }
}
