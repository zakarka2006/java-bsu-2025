package com.lab2.service;

import com.lab2.model.Transaction;
import com.lab2.repository.Repository;
import com.lab2.service.command.TransactionCommand;
import com.lab2.service.observer.TransactionObserver;
import com.lab2.service.visitor.TransactionVisitor;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class TransactionManager {
    private static TransactionManager instance;
    private final ExecutorService executorService;
    private final Repository repository;
    private final List<TransactionObserver> observers;

    private TransactionManager(Repository repository) {
        this.executorService = Executors.newFixedThreadPool(10);
        this.repository = repository;
        this.observers = new ArrayList<>();
    }

    public static synchronized TransactionManager getInstance(Repository repository) {
        if (instance == null) {
            instance = new TransactionManager(repository);
        }
        return instance;
    }

    public void addObserver(TransactionObserver observer) {
        observers.add(observer);
    }

    public void removeObserver(TransactionObserver observer) {
        observers.remove(observer);
    }

    public void submitTransaction(TransactionCommand command) {
        executorService.submit(() -> {
            try {
                synchronized (TransactionManager.class) {
                    command.execute();
                    Transaction transaction = command.getTransaction();

                    if (command.getAccount() != null) {
                        repository.updateAccount(command.getAccount());
                    }

                    if (command.getTargetAccount() != null) {
                        repository.updateAccount(command.getTargetAccount());
                    }

                    repository.saveTransaction(transaction);
                    notifyObserversSuccess(transaction);
                }
            } catch (Exception e) {
                notifyObserversFailure(command.getTransaction(), e);
            }
        });
    }

    private void notifyObserversSuccess(Transaction transaction) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionCompleted(transaction);
        }
    }

    private void notifyObserversFailure(Transaction transaction, Exception e) {
        for (TransactionObserver observer : observers) {
            observer.onTransactionFailed(transaction, e);
        }
    }

    public void accept(TransactionVisitor visitor) {
        List<Transaction> transactions = repository.findAllTransactions();
        for (Transaction transaction : transactions) {
            visitor.visit(transaction);
        }
    }
}
