package com.cogent.service;

import java.sql.SQLException;

public interface CustomerDao {
    boolean isAccountApproved(int accountId);
    void applyForAccount(int userId, double initialBalance);
    void viewBalance(int accountId);
    void deposit(int accountId, double amount) throws SQLException;
    void withdraw(int accountId, double amount) throws SQLException;
    void postMoneyTransfer(int fromAccountId, int toAccountId, double amount) throws SQLException;

}
