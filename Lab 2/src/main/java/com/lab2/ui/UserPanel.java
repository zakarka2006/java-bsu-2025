package com.lab2.ui;

import com.lab2.model.User;
import com.lab2.repository.Repository;

import javax.swing.*;
import java.awt.*;
import java.util.List;

public class UserPanel extends JPanel {
    private final Repository repository;
    private final DefaultListModel<User> userListModel;
    private final JList<User> userList;
    private Runnable onUserSelected;

    public UserPanel(Repository repository) {
        this.repository = repository;
        this.userListModel = new DefaultListModel<>();
        this.userList = new JList<>(userListModel);

        setLayout(new BorderLayout());
        setBorder(BorderFactory.createTitledBorder("Users"));

        refreshUserList();

        add(new JScrollPane(userList), BorderLayout.CENTER);

        JPanel inputPanel = new JPanel(new BorderLayout());
        JTextField nicknameField = new JTextField();
        JButton addButton = new JButton("Add User");

        addButton.addActionListener(e -> {
            String nickname = nicknameField.getText();
            if (!nickname.isEmpty()) {
                User user = new User(nickname);
                repository.saveUser(user);
                refreshUserList();
                nicknameField.setText("");
            }
        });

        inputPanel.add(nicknameField, BorderLayout.CENTER);
        inputPanel.add(addButton, BorderLayout.EAST);
        add(inputPanel, BorderLayout.SOUTH);

        userList.addListSelectionListener(e -> {
            if (!e.getValueIsAdjusting() && onUserSelected != null) {
                onUserSelected.run();
            }
        });
    }

    public void setOnUserSelected(Runnable onUserSelected) {
        this.onUserSelected = onUserSelected;
    }

    public User getSelectedUser() {
        return userList.getSelectedValue();
    }

    public void refreshUserList() {
        userListModel.clear();
        List<User> users = repository.findAllUsers();
        for (User user : users) {
            userListModel.addElement(user);
        }
    }
}
