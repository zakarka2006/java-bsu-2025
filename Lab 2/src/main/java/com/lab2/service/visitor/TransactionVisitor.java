package com.lab2.service.visitor;

import com.lab2.model.Transaction;

public interface TransactionVisitor {
    void visit(Transaction transaction);
}
