package com.cogent.service.impl;

import com.cogent.entity.User;
import com.cogent.service.UserDao;

import java.sql.*;

public class UserDaoImpl implements UserDao {
    @Override
    public User registerUser(String username, String password, String role) throws SQLException {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cogent", "root", "root");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)")) {
            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);
            stmt.executeUpdate();

            return null;
        }
    }
    @Override
    public int getUserId(User user) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/cogent", "root", "root");
             PreparedStatement stmt = conn.prepareStatement("INSERT INTO users (username, password, role) VALUES (?, ?, ?)", Statement.RETURN_GENERATED_KEYS)) {
            stmt.setString(1, user.getUsername());
            stmt.setString(2, user.getPassword());
            stmt.setString(3, user.getRole());
            stmt.executeUpdate();
            ResultSet rs = stmt.getGeneratedKeys();
            int userId = 0;
            if(rs.next()){
                userId = rs.getInt(1);
            }
            rs.close();
            return userId;
        } catch (SQLException e) {
            e.printStackTrace();
        }

        return 0;
    }

}
