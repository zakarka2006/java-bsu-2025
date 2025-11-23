package com.lab2.model;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;

public class Transaction {
    private UUID id;
    private LocalDateTime timestamp;
    private TransactionType type;
    private BigDecimal amount;
    private UUID accountId;
    private UUID targetAccountId;

    public Transaction() {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
    }

    public Transaction(TransactionType type, BigDecimal amount, UUID accountId) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
    }

    public Transaction(TransactionType type, BigDecimal amount, UUID accountId, UUID targetAccountId) {
        this.id = UUID.randomUUID();
        this.timestamp = LocalDateTime.now();
        this.type = type;
        this.amount = amount;
        this.accountId = accountId;
        this.targetAccountId = targetAccountId;
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public TransactionType getType() {
        return type;
    }

    public void setType(TransactionType type) {
        this.type = type;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public void setAmount(BigDecimal amount) {
        this.amount = amount;
    }

    public UUID getAccountId() {
        return accountId;
    }

    public void setAccountId(UUID accountId) {
        this.accountId = accountId;
    }

    public UUID getTargetAccountId() {
        return targetAccountId;
    }

    public void setTargetAccountId(UUID targetAccountId) {
        this.targetAccountId = targetAccountId;
    }

    @Override
    public String toString() {
        return "Transaction{" + "id=" + id + ", timestamp=" + timestamp + ", type=" + type + ", amount=" + amount + ", accountId=" + accountId + ", targetAccountId=" + targetAccountId + '}';
    }
}
