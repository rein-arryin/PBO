package com.jasadesign.dao.contract;

import com.jasadesign.model.User;

import java.sql.SQLException;

public interface UserRepository {

    User login(String username, String password) throws SQLException;

    User findById(int id) throws SQLException;

    User findByUsername(String username) throws SQLException;
}
