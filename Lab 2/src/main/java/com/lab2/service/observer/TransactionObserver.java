package com.lab2.service.observer;

import com.lab2.model.Transaction;

public interface TransactionObserver {
    void onTransactionCompleted(Transaction transaction);

    void onTransactionFailed(Transaction transaction, Exception e);
}
