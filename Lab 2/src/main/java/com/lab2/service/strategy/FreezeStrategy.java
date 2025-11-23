package com.lab2.service.strategy;

import com.lab2.model.Account;
import com.lab2.model.AccountStatus;

import java.math.BigDecimal;

public class FreezeStrategy implements TransactionStrategy {
    @Override
    public void execute(Account account, Account targetAccount, BigDecimal amount) {
        account.setStatus(AccountStatus.FROZEN);
    }
}
