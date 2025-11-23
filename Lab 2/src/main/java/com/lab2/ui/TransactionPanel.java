package com.lab2.ui;

import com.lab2.model.Account;
import com.lab2.model.Transaction;
import com.lab2.model.TransactionType;
import com.lab2.repository.Repository;
import com.lab2.service.TransactionManager;
import com.lab2.service.command.TransactionCommand;
import com.lab2.service.factory.TransactionFactory;
import com.lab2.service.observer.TransactionObserver;
import com.lab2.service.strategy.*;

import javax.swing.*;
import javax.swing.table.DefaultTableModel;
import java.awt.*;
import java.math.BigDecimal;
import java.util.List;
import java.util.UUID;

public class TransactionPanel extends JPanel implements TransactionObserver {
    private final Repository repository;
    private final TransactionManager transactionManager;
    private final DefaultTableModel transactionTableModel;
    private final JComboBox<TransactionType> typeComboBox;
    private final JTextField amountField;
    private final JTextField targetAccountIdField;
    private Account selectedAccount;

    public TransactionPanel(Repository repository, TransactionManager transactionManager) {
        this.repository = repository;
        this.transactionManager = transactionManager;
        this.transactionManager.addObserver(this);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Transactions"));

        String[] columnNames = {"ID", "Time", "Type", "Amount", "Account", "Target"};
        transactionTableModel = new DefaultTableModel(columnNames, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false;
            }
        };
        JTable transactionTable = new JTable(transactionTableModel);
        add(new JScrollPane(transactionTable), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new GridLayout(4, 2));

        inputPanel.add(new JLabel("Type:"));
        typeComboBox = new JComboBox<>(TransactionType.values());
        inputPanel.add(typeComboBox);

        typeComboBox.addActionListener(e -> {
            TransactionType type = (TransactionType) typeComboBox.getSelectedItem();
            boolean isAmountRequired = type != TransactionType.FREEZE && type != TransactionType.UNFREEZE;
            amountField.setEnabled(isAmountRequired);
            if (!isAmountRequired) {
                amountField.setText("");
            }
        });

        inputPanel.add(new JLabel("Amount:"));
        amountField = new JTextField();
        inputPanel.add(amountField);

        inputPanel.add(new JLabel("Target Account ID (Transfer):"));
        targetAccountIdField = new JTextField();
        inputPanel.add(targetAccountIdField);

        JButton executeButton = new JButton("Execute");
        executeButton.addActionListener(e -> executeTransaction());
        inputPanel.add(executeButton);

        add(inputPanel, BorderLayout.SOUTH);

        refreshTransactionList();
    }

    public void setSelectedAccount(Account account) {
        this.selectedAccount = account;
    }

    private void executeTransaction() {
        if (selectedAccount == null) {
            JOptionPane.showMessageDialog(this, "Select an account first");
            return;
        }

        try {
            TransactionType type = (TransactionType) typeComboBox.getSelectedItem();
            BigDecimal amount = null;

            if (type != TransactionType.FREEZE && type != TransactionType.UNFREEZE) {
                try {
                    amount = new BigDecimal(amountField.getText());
                } catch (NumberFormatException ex) {
                    JOptionPane.showMessageDialog(this, "Invalid amount format");
                    return;
                }

                if (amount.compareTo(BigDecimal.ZERO) <= 0) {
                    JOptionPane.showMessageDialog(this, "Amount must be positive");
                    return;
                }
            }

            UUID targetAccountId = null;
            Account targetAccount = null;

            if (type == TransactionType.TRANSFER) {
                String targetIdStr = targetAccountIdField.getText();
                if (targetIdStr.isEmpty()) {
                    JOptionPane.showMessageDialog(this, "Target Account ID required for transfer");
                    return;
                }
                targetAccountId = UUID.fromString(targetIdStr);
                targetAccount = repository.findAccountById(targetAccountId);
                if (targetAccount == null) {
                    JOptionPane.showMessageDialog(this, "Target Account not found");
                    return;
                }
                if (targetAccount.getId().equals(selectedAccount.getId())) {
                    JOptionPane.showMessageDialog(this, "Cannot transfer to the same account");
                    return;
                }
            }

            Transaction transaction = TransactionFactory.createTransaction(type, amount, selectedAccount.getId(), targetAccountId);
            TransactionStrategy strategy = getStrategy(type);
            TransactionCommand command = new TransactionCommand(transaction, strategy, selectedAccount, targetAccount);

            transactionManager.submitTransaction(command);

        } catch (Exception e) {
            JOptionPane.showMessageDialog(this, "Error: " + e.getMessage());
        }
    }

    private TransactionStrategy getStrategy(TransactionType type) {
        switch (type) {
            case DEPOSIT:
                return new DepositStrategy();
            case WITHDRAW:
                return new WithdrawStrategy();
            case TRANSFER:
                return new TransferStrategy();
            case FREEZE:
                return new FreezeStrategy();
            case UNFREEZE:
                return new UnfreezeStrategy();
            default:
                throw new IllegalArgumentException("Unknown type");
        }
    }

    @Override
    public void onTransactionCompleted(Transaction transaction) {
        SwingUtilities.invokeLater(() -> {
            addTransactionToTable(transaction);
            JOptionPane.showMessageDialog(this, "Transaction Completed");
        });
    }

    @Override
    public void onTransactionFailed(Transaction transaction, Exception e) {
        SwingUtilities.invokeLater(() -> {
            JOptionPane.showMessageDialog(this, "Transaction Failed: " + e.getMessage());
        });
    }

    private void refreshTransactionList() {
        transactionTableModel.setRowCount(0);
        List<Transaction> transactions = repository.findAllTransactions();
        for (Transaction t : transactions) {
            addTransactionToTable(t);
        }
    }

    private void addTransactionToTable(Transaction t) {
        transactionTableModel.addRow(new Object[]{t.getId(), t.getTimestamp(), t.getType(), t.getAmount(), t.getAccountId(), t.getTargetAccountId()});
    }
}
