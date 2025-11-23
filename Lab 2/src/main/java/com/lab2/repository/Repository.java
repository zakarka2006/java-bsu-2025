package com.lab2.repository;

import com.lab2.model.Account;
import com.lab2.model.Transaction;
import com.lab2.model.User;

import java.util.List;
import java.util.UUID;

public interface Repository {
    void saveUser(User user);

    User findUserById(UUID id);

    List<User> findAllUsers();

    void saveAccount(Account account, UUID userId);

    void updateAccount(Account account);

    void deleteAccount(UUID id);

    Account findAccountById(UUID id);

    List<Account> findAccountsByUserId(UUID userId);

    void saveTransaction(Transaction transaction);

    List<Transaction> findAllTransactions();
}
