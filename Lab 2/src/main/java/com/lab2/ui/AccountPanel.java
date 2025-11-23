package com.lab2.ui;

import com.lab2.model.Account;
import com.lab2.model.User;
import com.lab2.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class AccountPanel extends JPanel {
    private final Repository repository;
    private final DefaultListModel<Account> accountListModel;
    private final JList<Account> accountList;
    private User currentUser;

    public AccountPanel(Repository repository) {
        this.repository = repository;
        this.accountListModel = new DefaultListModel<>();
        this.accountList = new JList<>(accountListModel);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Accounts"));

        add(new JScrollPane(accountList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new FlowLayout());
        JButton addButton = new JButton("Create Account");
        JButton deleteButton = new JButton("Delete Account");

        addButton.addActionListener(e -> {
            if (currentUser != null) {
                Account account = new Account();
                repository.saveAccount(account, currentUser.getId());
                refreshAccountList();
            } else {
                JOptionPane.showMessageDialog(this, "Select a user first");
            }
        });

        deleteButton.addActionListener(e -> {
            Account selected = getSelectedAccount();
            if (selected != null) {
                int confirm = JOptionPane.showConfirmDialog(this, "Are you sure you want to delete this account?", "Confirm Delete", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    repository.deleteAccount(selected.getId());
                    refreshAccountList();
                }
            } else {
                JOptionPane.showMessageDialog(this, "Select an account to delete");
            }
        });

        inputPanel.add(addButton);
        inputPanel.add(deleteButton);
        add(inputPanel, BorderLayout.SOUTH);
    }

    public void setCurrentUser(User user) {
        this.currentUser = user;
        refreshAccountList();
    }

    public void setOnAccountSelected(Runnable onAccountSelected) {
        accountList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onAccountSelected != null) {
                onAccountSelected.run();
            }
        });
    }

    public Account getSelectedAccount() {
        return accountList.getSelectedValue();
    }

    public void refreshAccountList() {
        Account selected = accountList.getSelectedValue();
        java.util.UUID selectedId = selected != null ? selected.getId() : null;

        accountListModel.clear();
        if (currentUser != null) {
            List<Account> accounts = repository.findAccountsByUserId(currentUser.getId());
            for (Account account : accounts) {
                accountListModel.addElement(account);
                if (selectedId != null && account.getId().equals(selectedId)) {
                    accountList.setSelectedValue(account, true);
                }
            }
        }
    }
}
