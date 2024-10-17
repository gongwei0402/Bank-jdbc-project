package com.cogent.service.impl;

import com.cogent.ConnectionFactory;
import com.cogent.entity.Account;
import com.cogent.entity.Transaction;
import com.cogent.service.EmployeeDao;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class EmployeeDaoImpl implements EmployeeDao {
    private Connection connection = ConnectionFactory.getConnection();

    public EmployeeDaoImpl(Connection connection) {
        this.connection = connection;
    }

    @Override
    public void approveAccount(int accountId) {
        String query = "update accounts set isApproved = '1' where account_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, accountId);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    @Override
    public List<Account> viewCustomerAccounts(int userId) {
        List<Account> accounts = new ArrayList<>();
        String query = "SELECT * FROM accounts WHERE user_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, userId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                int accountId = rs.getInt("account_id");
                userId = rs.getInt("user_id");
                double balance = rs.getDouble("balance");
                boolean isApproved = Boolean.parseBoolean(rs.getString("isApproved"));
                accounts.add(new Account(accountId, userId, balance, isApproved));
            } else {
                throw new SQLException("Customer account not found.");
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return accounts;
    }

    @Override
    public List<Transaction> viewAllTransactions(int fromAccountId) throws SQLException {
        List<Transaction> transactions = new ArrayList<>();
        String query = "SELECT * FROM transactions WHERE fromAccount_id = ?";
        try (PreparedStatement stmt = connection.prepareStatement(query)) {
            stmt.setInt(1, fromAccountId);
            ResultSet rs = stmt.executeQuery();
            while (rs.next()) {
                int accountId = rs.getInt("fromAccount_id");
                int transactionId = rs.getInt("transaction_id");
                int toAccountId = rs.getInt("toAccount_id");
                double amount = rs.getDouble("amount");
                String type = rs.getString("type");
                Date date = rs.getDate("date");
                transactions.add(new Transaction(accountId, transactionId, fromAccountId, toAccountId, amount, type, date));
               // System.out.println(transactionId);
                //System.out.println(amount);

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return transactions;


    }
}