package com.lab2.service.factory;

import com.lab2.model.Transaction;
import com.lab2.model.TransactionType;

import java.math.BigDecimal;
import java.util.UUID;

public class TransactionFactory {
    public static Transaction createTransaction(TransactionType type, BigDecimal amount, UUID accountId, UUID targetAccountId) {
        if (type == TransactionType.TRANSFER && targetAccountId == null) {
            throw new IllegalArgumentException("Target account ID is required for transfer");
        }
        return new Transaction(type, amount, accountId, targetAccountId);
    }

    public static Transaction createTransaction(TransactionType type, BigDecimal amount, UUID accountId) {
        return new Transaction(type, amount, accountId);
    }
}
