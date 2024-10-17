package com.cogent.service;

import com.cogent.entity.User;

import java.sql.SQLException;

public interface UserDao {
    User registerUser(String username, String password, String role) throws SQLException;
    int getUserId(User user);
}


