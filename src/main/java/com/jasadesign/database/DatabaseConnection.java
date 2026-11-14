package com.jasadesign.database;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DatabaseConnection {

    private static final String URL =
            "jdbc:mariadb://localhost:3306/jasa_desain";

    private static final String USER = "root";
    private static final String PASSWORD = "root123";

    public static Connection getConnection()
            throws SQLException {

        return DriverManager.getConnection(
                URL, USER, PASSWORD
        );
    }
}
