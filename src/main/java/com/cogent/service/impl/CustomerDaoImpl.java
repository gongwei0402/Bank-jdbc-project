package com.cogent.service.impl;


import com.cogent.ConnectionFactory;
import com.cogent.service.CustomerDao;

import java.sql.*;


public class CustomerDaoImpl implements CustomerDao {
    private Connection connection = ConnectionFactory.getConnection();
    public CustomerDaoImpl(){

    }

    @Override
    public boolean isAccountApproved(int accountId) {
        boolean isApproved = false;
        String query ="SELECT isApproved FROM accounts WHERE account_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)){
            stmt.setInt(1,accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                isApproved = rs.getBoolean("isApproved");

            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

        return isApproved;
    }


    @Override
    public void applyForAccount(int userId, double initialBalance) {
        if (initialBalance < 0) {
            throw new IllegalArgumentException("Initial deposit must be non-negative.");
        }
       String sql = "insert into accounts(user_id, balance, isApproved) values(?, ?, ?)";
       try(PreparedStatement stmt = connection.prepareStatement(sql)) {
            stmt.setInt(1, userId);
            stmt.setDouble(2, initialBalance);
            stmt.setBoolean(3, false);
            stmt.executeUpdate();

        } catch (SQLException e) {
            throw new RuntimeException(e);
        }

    }

    @Override
    public void viewBalance(int accountId) {
        // firstly need to check if account is approved or not

        if (!isAccountApproved(accountId)) {
            System.out.println("Error: Account is not yet approved!");
            return;
        }
        String query ="SELECT balance FROM accounts WHERE account_id = ?";
        try(PreparedStatement stmt = connection.prepareStatement(query)){
          stmt.setInt(1,accountId);
            ResultSet rs = stmt.executeQuery();
            if (rs.next()) {
                System.out.println("balance:"+rs.getDouble("balance"));
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void deposit(int accountId, double amount) throws SQLException {

        // firstly need to check if account is approved or not
        if (!isAccountApproved(accountId)) {
            System.out.println("Error: Account is not yet approved!");
            return;
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Deposit amount must be positive.");
        }
        String query = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(query)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }


    }

    @Override
    public void withdraw( int accountId, double amount) throws SQLException {

        // firstly need to check if account is approved or not
        if (!isAccountApproved(accountId)) {
            System.out.println("Error: Account is not yet approved!");
            return;
        }

        if (amount <= 0) {
            throw new IllegalArgumentException("Withdrawal amount must be positive.");
        }
        String checkBalanceQuery = "SELECT balance FROM accounts WHERE account_id = ?";
        try (PreparedStatement checkStmt = connection.prepareStatement(checkBalanceQuery)) {
            checkStmt.setInt(1, accountId);
            ResultSet rs = checkStmt.executeQuery();
            if (rs.next()) {
                double currentBalance = rs.getDouble("balance");
                if (currentBalance < amount) {
                    throw new IllegalStateException("Insufficient funds.");
                }
            }
        }
        String updateQuery = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
        try (PreparedStatement pstmt = connection.prepareStatement(updateQuery)) {
            pstmt.setDouble(1, amount);
            pstmt.setInt(2, accountId);
            pstmt.executeUpdate();
        }
    }

    @Override
    public void postMoneyTransfer(int fromAccountId, int toAccountId, double amount) throws SQLException {

        // firstly need to check if account is approved or not
        if (!isAccountApproved(fromAccountId) || !isAccountApproved(toAccountId)) {
            System.out.println("Error: Account is not yet approved!");
            return;
        }



            if (amount <= 0) {
                throw new IllegalArgumentException("Transfer amount must be positive.");
            }
            String deductQuery = "UPDATE accounts SET balance = balance - ? WHERE account_id = ?";
            String creditQuery = "UPDATE accounts SET balance = balance + ? WHERE account_id = ?";
            String insertTransactionQuery = "INSERT INTO transactions (fromAccount_id, toAccount_id, amount, date) VALUES (?, ?, ?, ?)";
            try {
                 connection.setAutoCommit(false);

                 try (PreparedStatement deductStmt = connection.prepareStatement(deductQuery)) {
                    deductStmt.setDouble(1, amount);
                    deductStmt.setInt(2, fromAccountId);
                    int rowsDeducted = deductStmt.executeUpdate();

                    if (rowsDeducted == 0) {
                    connection.rollback();
                    return;
                }
            }
                try (PreparedStatement creditStmt = connection.prepareStatement(creditQuery)) {
                    creditStmt.setDouble(1, amount);
                    creditStmt.setInt(2, toAccountId);
                    int rowsCredited = creditStmt.executeUpdate();

                    if (rowsCredited == 0) {
                        connection.rollback();
                        return;
                    }
                }
                try (PreparedStatement insertStmt = connection.prepareStatement(insertTransactionQuery)) {
                    insertStmt.setInt(1, fromAccountId);
                    insertStmt.setInt(2, toAccountId);
                    insertStmt.setDouble(3, amount);
                    insertStmt.setDate(4, new Date(System.currentTimeMillis()));
                    insertStmt.executeUpdate();
                }
                connection.commit();
                System.out.println("Transfer "+amount+" to "+toAccountId+" done.");

            }catch (SQLException e) {
                connection.rollback();
                throw e;
            } finally {
                connection.setAutoCommit(true);
            }

        }

    }

