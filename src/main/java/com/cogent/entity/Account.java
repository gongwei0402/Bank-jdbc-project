package com.cogent.entity;

public class Account {
    private int accountId;
    private int userId;
    private double balance;
    private boolean isApproved;

    public Account(int accountId, int userId, double balance, boolean isApproved) {
        this.accountId = accountId;
        this.userId = userId;
        this.balance = balance;
        this.isApproved = isApproved;
    }

    public int getAccountId() {
        return accountId;
    }

    public void setAccountId(int accountId) {
        this.accountId = accountId;
    }

    public int getUserId() {
        return userId;
    }

    public void setUserId(int userId) {
        this.userId = userId;
    }

    public double getBalance() {
        return balance;
    }

    public void setBalance(double balance) {
        this.balance = balance;
    }

    public boolean isApproved() {
        return isApproved;
    }

    public void setApproved(boolean approved) {
        isApproved = approved;
    }
}
