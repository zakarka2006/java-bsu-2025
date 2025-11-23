package com.lab2.model;

import java.math.BigDecimal;
import java.util.UUID;

public class Account {
    private UUID id;
    private BigDecimal balance;
    private AccountStatus status;

    public Account() {
        this.id = UUID.randomUUID();
        this.balance = BigDecimal.ZERO;
        this.status = AccountStatus.ACTIVE;
    }

    public Account(UUID id, BigDecimal balance, AccountStatus status) {
        this.id = id;
        this.balance = balance;
        this.status = status;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public BigDecimal getBalance() {
        return balance;
    }

    public void setBalance(BigDecimal balance) {
        this.balance = balance;
    }

    public AccountStatus getStatus() {
        return status;
    }

    public void setStatus(AccountStatus status) {
        this.status = status;
    }

    @Override
    public String toString() {
        return "Account{" + "id=" + id + ", balance=" + balance + ", status=" + status + '}';
    }
}
