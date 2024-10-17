package com.cogent;

import com.cogent.entity.Account;
import com.cogent.entity.Customer;
import com.cogent.entity.Transaction;
import com.cogent.entity.User;
import com.cogent.service.CustomerDao;
import com.cogent.service.EmployeeDao;
import com.cogent.service.UserDao;
import com.cogent.service.impl.CustomerDaoImpl;
import com.cogent.service.impl.EmployeeDaoImpl;
import com.cogent.service.impl.UserDaoImpl;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;


public class App {


    private static final Scanner input = new Scanner(System.in);
    private static final Connection connection = ConnectionFactory.getConnection();

    public static void main(String[] args) throws SQLException {


        while (true) {
            System.out.println("1. Customer Login");
            System.out.println("2. Employee Login");
            System.out.println("3. Register New User");
            System.out.println("4. Exit");
            int choice = input.nextInt();
            switch (choice) {
                case 1:
                    customerMenu();
                    break;
                case 2:
                    employeeMenu();
                    break;
                case 3:
                    register();
                    break;
                case 4:
                    System.out.println("Exiting...");
                    return;
                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static void register( ) throws SQLException {
        System.out.println("Enter username:");
        String userName = input.next();
        System.out.println("Enter password:");
        String password = input.next();
        System.out.println("Enter role(customer/employee):");
        String role = input.next();

        // create instance of Random class
        //Random rand = new Random();
        //int userId = rand.nextInt();

        UserDao userDao = new UserDaoImpl();
        User user =  userDao.registerUser(userName, password, role);
    }

    private static void customerMenu() throws SQLException {
        System.out.println("Enter customer name:");
        String username = input.next();
        System.out.println("Enter password:");
        String password = input.next();

        // retrieve user ID
        int userId = retrieveUserId(username, password, "customer");

        Customer customer = new Customer(userId, username, password);

        while (true) {
            System.out.println("1. Apply for Account");
            System.out.println("2. View Balance");
            System.out.println("3. Deposit");
            System.out.println("4. Withdraw");
            System.out.println("5. Transfer to other account");
            System.out.println("6. Logout");

            CustomerDao customerDao = new CustomerDaoImpl();
            int choice = input.nextInt();
            switch (choice) {
                case 1: {
                    System.out.println("Enter starting balance:");
                    double initialBalance = input.nextDouble();
                    customerDao.applyForAccount(userId, initialBalance);
                    System.out.println("your account is created, ask employee to approve!");
                    break;
                }

                case 2: {
                    System.out.println("Enter account ID:");
                    int accountId = input.nextInt();
                    customerDao.viewBalance(accountId);
                    break;
                }

                case 3: {
                    System.out.println("Enter account ID:");
                    int accountId = input.nextInt();
                    System.out.println("Enter deposit amount:");
                    double amount = input.nextDouble();
                    customerDao.deposit(accountId, amount);
                    break;
                }

                case 4: {
                    System.out.println("Enter account ID:");
                    int accountId = input.nextInt();

                    System.out.println("Enter deposit amount:");
                    double withdrawAmount = input.nextDouble();

                    customerDao.withdraw( accountId, withdrawAmount);
                    break;
                }

                case 5: {
                    System.out.println("Enter origin account ID:");
                    int fromAccountId = input.nextInt();

                    System.out.println("Enter target account ID:");
                    int toAccountId = input.nextInt();

                    System.out.println("Enter transfer amount:");
                    double transferAmount = input.nextDouble();

                    customerDao.postMoneyTransfer(fromAccountId, toAccountId, transferAmount);
                    break;
                }

                case 6:
                    System.out.println("Exiting...");
                    return;

                default:
                    System.out.println("Invalid choice. Please try again.");
            }


        }
    }


    private static void employeeMenu() throws SQLException {

        System.out.println("Enter username:");
        String username = input.next();
        System.out.println("Enter password:");
        String password = input.next();

        // retrieve user ID
        int empUserId = retrieveUserId(username, password, "employee");

        while (true) {

            System.out.println("1. Approve Account");
            System.out.println("2. View Customer Account");
            System.out.println("3. View Transaction");
            System.out.println("4. Logout");

            EmployeeDao employeeDao = new EmployeeDaoImpl(connection);
            int choice = input.nextInt();

            switch (choice) {
                case 1: {
                    System.out.println("Enter Account ID:");
                    int accountId = input.nextInt();

                    employeeDao.approveAccount(accountId);
                    System.out.println("Approved!");
                    break;
                }

                case 2: {
                    System.out.println("Enter user ID:");
                    int userId = input.nextInt();

                    List<Account> accounts =employeeDao.viewCustomerAccounts(userId);
                    accounts.stream().forEach((acc)-> System.out.println("Account Id: "+acc.getAccountId()+" User Id: "+acc.getUserId()+" Balance: "+acc.getBalance()) );
                    break;
                }

                case 3: {
                    System.out.println("Enter Account ID:");
                    int fromAccountId = input.nextInt();

                    List<Transaction> transactions = employeeDao.viewAllTransactions(fromAccountId);
                    transactions.stream().forEach((trans)-> System.out.println("Original Account: "+trans.getFromAccountId()+
                    " Target Account: "+trans.getToAccountId()+" Transaction Amount: "+trans.getAmount()+" Date: "+trans.getDate()));
                    break;
                }

                case 4: {
                    System.out.println("Exiting...");
                    return;
                }

                default:
                    System.out.println("Invalid choice. Please try again.");
            }
        }
    }

    private static int retrieveUserId(String username, String password, String role) {
        User user = new User(0, username, password, role);
        UserDao userDao = new UserDaoImpl();
        return userDao.getUserId(user);
    }

}
