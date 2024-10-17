package com.cogent.entity;

public class Customer {
    private int userId;
    private String username;
    private String password;
    private Account account;

    public Customer(int userId, String username, String password, Account account) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.account = account;
    }

    public Customer(int userId, String username, String password) {
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public Account getAccount() {
        return account;
    }

    public void setAccount(Account account) {
        this.account = account;
    }
}
