package com.lab2.service.strategy;

import com.lab2.model.Account;
import com.lab2.model.AccountStatus;

import java.math.BigDecimal;

public class TransferStrategy implements TransactionStrategy {
    @Override
    public void execute(Account account, Account targetAccount, BigDecimal amount) {
        if (account.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Source account is frozen");
        }
        if (targetAccount.getStatus() == AccountStatus.FROZEN) {
            throw new IllegalStateException("Target account is frozen");
        }
        if (account.getBalance().compareTo(amount) < 0) {
            throw new IllegalStateException("Insufficient funds");
        }
        account.setBalance(account.getBalance().subtract(amount));
        System.out.println("Transfer from " + account.getId() + " to " + targetAccount.getId() + " for " + amount);
        targetAccount.setBalance(targetAccount.getBalance().add(amount));
    }
}
