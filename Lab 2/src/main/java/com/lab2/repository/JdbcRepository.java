package com.lab2.repository;

import com.lab2.model.*;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class JdbcRepository implements Repository {
    private final Connection connection;

    public JdbcRepository() {
        this.connection = DatabaseConnection.getInstance().getConnection();
    }

    @Override
    public void saveUser(User user) {
        try (PreparedStatement stmt = connection.prepareStatement("MERGE INTO users (id, nickname) KEY(id) VALUES (?, ?)")) {
            stmt.setObject(1, user.getId());
            stmt.setString(2, user.getNickname());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public User findUserById(UUID id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM users WHERE id = ?")) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                User user = new User();
                user.setId((UUID) rs.getObject("id"));
                user.setNickname(rs.getString("nickname"));
                user.setAccountIds(findAccountIdsByUserId(user.getId()));
                return user;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        List<User> users = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM users");
            while (rs.next()) {
                User user = new User();
                user.setId((UUID) rs.getObject("id"));
                user.setNickname(rs.getString("nickname"));
                user.setAccountIds(findAccountIdsByUserId(user.getId()));
                users.add(user);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return users;
    }

    @Override
    public void saveAccount(Account account, UUID userId) {
        try (PreparedStatement stmt = connection.prepareStatement("MERGE INTO accounts (id, balance, status, user_id) KEY(id) VALUES (?, ?, ?, ?)")) {
            stmt.setObject(1, account.getId());
            stmt.setBigDecimal(2, account.getBalance());
            stmt.setString(3, account.getStatus().name());
            stmt.setObject(4, userId);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void updateAccount(Account account) {
        try (PreparedStatement stmt = connection.prepareStatement("UPDATE accounts SET balance = ?, status = ? WHERE id = ?")) {
            stmt.setBigDecimal(1, account.getBalance());
            stmt.setString(2, account.getStatus().name());
            stmt.setObject(3, account.getId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deleteAccount(UUID id) {
        try (PreparedStatement stmt = connection.prepareStatement("DELETE FROM accounts WHERE id = ?")) {
            stmt.setObject(1, id);
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public Account findAccountById(UUID id) {
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM accounts WHERE id = ?")) {
            stmt.setObject(1, id);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                Account account = new Account();
                account.setId((UUID) rs.getObject("id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));
                return account;
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null;
    }

    @Override
    public List<Account> findAccountsByUserId(UUID userId) {
        List<Account> accounts = new ArrayList<>();
        try (PreparedStatement stmt = connection.prepareStatement("SELECT * FROM accounts WHERE user_id = ?")) {
            stmt.setObject(1, userId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                Account account = new Account();
                account.setId((UUID) rs.getObject("id"));
                account.setBalance(rs.getBigDecimal("balance"));
                account.setStatus(AccountStatus.valueOf(rs.getString("status")));
                accounts.add(account);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    private List<UUID> findAccountIdsByUserId(UUID userId) {
        List<UUID> ids = new ArrayList<>();
        for (Account acc : findAccountsByUserId(userId)) {
            ids.add(acc.getId());
        }
        return ids;
    }

    @Override
    public void saveTransaction(Transaction transaction) {
        try (PreparedStatement stmt = connection.prepareStatement("INSERT INTO transactions (id, timestamp, type, amount, account_id, target_account_id) VALUES (?, ?, ?, ?, ?, ?)")) {
            stmt.setObject(1, transaction.getId());
            stmt.setTimestamp(2, Timestamp.valueOf(transaction.getTimestamp()));
            stmt.setString(3, transaction.getType().name());
            stmt.setBigDecimal(4, transaction.getAmount());
            stmt.setObject(5, transaction.getAccountId());
            stmt.setObject(6, transaction.getTargetAccountId());
            stmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public List<Transaction> findAllTransactions() {
        List<Transaction> transactions = new ArrayList<>();
        try (Statement stmt = connection.createStatement()) {
            ResultSet rs = stmt.executeQuery("SELECT * FROM transactions ORDER BY timestamp DESC");
            while (rs.next()) {
                Transaction transaction = new Transaction();
                transaction.setId((UUID) rs.getObject("id"));
                transaction.setTimestamp(rs.getTimestamp("timestamp").toLocalDateTime());
                transaction.setType(TransactionType.valueOf(rs.getString("type")));
                transaction.setAmount(rs.getBigDecimal("amount"));
                transaction.setAccountId((UUID) rs.getObject("account_id"));
                transaction.setTargetAccountId((UUID) rs.getObject("target_account_id"));
                transactions.add(transaction);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return transactions;
    }
}
