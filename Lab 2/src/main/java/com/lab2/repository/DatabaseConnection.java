package com.lab2.repository;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {
    private static DatabaseConnection instance;
    private Connection connection;

    private DatabaseConnection() {
        try {
            Class.forName("org.h2.Driver");
            this.connection = DriverManager.getConnection("jdbc:h2:./bank_db", "sa", "");
            initDb();
        } catch (ClassNotFoundException | SQLException e) {
            throw new RuntimeException("Failed to connect to database", e);
        }
    }

    public static synchronized DatabaseConnection getInstance() {
        if (instance == null) {
            instance = new DatabaseConnection();
        }
        return instance;
    }

    public Connection getConnection() {
        return connection;
    }

    private void initDb() throws SQLException {
        try (var stmt = connection.createStatement()) {
            stmt.execute("CREATE TABLE IF NOT EXISTS users (id UUID PRIMARY KEY, nickname VARCHAR(255))");
            stmt.execute("CREATE TABLE IF NOT EXISTS accounts (id UUID PRIMARY KEY, balance DECIMAL(20, 2), status VARCHAR(50), user_id UUID, FOREIGN KEY (user_id) REFERENCES users(id))");
            stmt.execute("CREATE TABLE IF NOT EXISTS transactions (id UUID PRIMARY KEY, timestamp TIMESTAMP, type VARCHAR(50), amount DECIMAL(20, 2), account_id UUID, target_account_id UUID)");
        }
    }
}
