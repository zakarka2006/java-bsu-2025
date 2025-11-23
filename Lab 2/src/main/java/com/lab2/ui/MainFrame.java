package com.lab2.ui;

import com.lab2.repository.JdbcRepository;
import com.lab2.repository.Repository;
import com.lab2.service.TransactionManager;

import javax.swing.*;
import java.awt.*;

public class MainFrame extends JFrame {
    public MainFrame() {
        setTitle("типа банк");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(1000, 600);
        setLayout(new GridLayout(1, 3));

        Repository repository = new JdbcRepository();
        TransactionManager transactionManager = TransactionManager.getInstance(repository);

        UserPanel userPanel = new UserPanel(repository);
        AccountPanel accountPanel = new AccountPanel(repository);
        TransactionPanel transactionPanel = new TransactionPanel(repository, transactionManager);

        userPanel.setOnUserSelected(() -> {
            accountPanel.setCurrentUser(userPanel.getSelectedUser());
            transactionPanel.setSelectedAccount(null);
        });

        accountPanel.setOnAccountSelected(() -> {
            transactionPanel.setSelectedAccount(accountPanel.getSelectedAccount());
        });

        transactionManager.addObserver(new com.lab2.service.observer.TransactionObserver() {
            @Override
            public void onTransactionCompleted(com.lab2.model.Transaction transaction) {
                SwingUtilities.invokeLater(accountPanel::refreshAccountList);
            }

            @Override
            public void onTransactionFailed(com.lab2.model.Transaction transaction, Exception e) {
            }
        });

        add(userPanel);
        add(accountPanel);
        add(transactionPanel);
    }

    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            new MainFrame().setVisible(true);
        });
    }
}
