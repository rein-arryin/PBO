package com.jasadesign.service;

import com.jasadesign.dao.UserDAO;
import com.jasadesign.dao.contract.UserRepository;
import com.jasadesign.model.User;

import java.sql.SQLException;

public class AuthService {

    private final UserRepository userRepository;

    public AuthService() {
        this(new UserDAO());
    }

    public AuthService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User login(String username, String password) throws SQLException {
        if (username == null || username.isBlank()) {
            throw new IllegalArgumentException("Username tidak boleh kosong.");
        }
        if (password == null || password.isBlank()) {
            throw new IllegalArgumentException("Password tidak boleh kosong.");
        }

        User user = userRepository.login(username.trim(), password);
        if (user == null) {
            throw new IllegalArgumentException("Username atau password salah.");
        }
        return user;
    }

    public User getUser(int id) throws SQLException {
        return userRepository.findById(id);
    }
}