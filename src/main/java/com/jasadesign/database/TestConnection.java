package com.jasadesign.database;

import java.sql.Connection;
import java.sql.SQLException;

public class TestConnection {

    public static void main(String[] args) {

        try (Connection connection =
                     DatabaseConnection.getConnection()) {

            System.out.println("Berhasil terhubung ke MariaDB!");

        } catch (SQLException e) {

            System.out.println("Gagal terhubung ke database!");
            e.printStackTrace();
        }
    }
}
