package com.cogent.service;

import com.cogent.entity.Account;
import com.cogent.entity.Transaction;

import java.sql.SQLException;
import java.util.List;

public interface EmployeeDao {
    void approveAccount(int accountId);
    List<Account> viewCustomerAccounts(int userId);
    List<Transaction> viewAllTransactions(int FromAccountId) throws SQLException;

}
