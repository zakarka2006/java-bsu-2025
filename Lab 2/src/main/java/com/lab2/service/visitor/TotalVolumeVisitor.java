package com.lab2.service.visitor;

import com.lab2.model.Transaction;

import java.math.BigDecimal;

public class TotalVolumeVisitor implements TransactionVisitor {
    private BigDecimal totalVolume = BigDecimal.ZERO;

    @Override
    public void visit(Transaction transaction) {
        if (transaction.getAmount() != null) {
            totalVolume = totalVolume.add(transaction.getAmount());
        }
    }

    public BigDecimal getTotalVolume() {
        return totalVolume;
    }
}
