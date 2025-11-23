package com.lab2.service.command;

import com.lab2.model.Account;
import com.lab2.model.Transaction;
import com.lab2.service.strategy.TransactionStrategy;

public class TransactionCommand {
    private final Transaction transaction;
    private final TransactionStrategy strategy;
    private final Account account;
    private final Account targetAccount;

    public TransactionCommand(Transaction transaction, TransactionStrategy strategy, Account account, Account targetAccount) {
        this.transaction = transaction;
        this.strategy = strategy;
        this.account = account;
        this.targetAccount = targetAccount;
    }

    public void execute() {
        strategy.execute(account, targetAccount, transaction.getAmount());
    }

    public Transaction getTransaction() {
        return transaction;
    }

    public Account getAccount() {
        return account;
    }

    public Account getTargetAccount() {
        return targetAccount;
    }
}
