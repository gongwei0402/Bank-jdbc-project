package com.cogent;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.ResourceBundle;

public class ConnectionFactory {
    private  static Connection connection;

    public static Connection getConnection() {
        if (connection == null) {
            String url = "jdbc:mysql://localhost:3306/cogent";
            String user = "root";
            String password = "root";
            try{
                connection = DriverManager.getConnection(url, user, password);
            }catch(SQLException e){
                throw new RuntimeException(e);
            }

        }
        return connection;
    }
}
