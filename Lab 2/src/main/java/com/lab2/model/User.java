package com.lab2.model;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

public class User {
    private UUID id;
    private String nickname;
    private List<UUID> accountIds;

    public User() {
        this.id = UUID.randomUUID();
        this.accountIds = new ArrayList<>();
    }

    public User(String nickname) {
        this.id = UUID.randomUUID();
        this.nickname = nickname;
        this.accountIds = new ArrayList<>();
    }

    public UUID getId() {
        return id;
    }

    public void setId(UUID id) {
        this.id = id;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public List<UUID> getAccountIds() {
        return accountIds;
    }

    public void setAccountIds(List<UUID> accountIds) {
        this.accountIds = accountIds;
    }

    public void addAccountId(UUID accountId) {
        this.accountIds.add(accountId);
    }

    @Override
    public String toString() {
        return "User{" + "id=" + id + ", nickname='" + nickname + '\'' + ", accountIds=" + accountIds + '}';
    }
}
