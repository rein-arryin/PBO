package com.jasadesign.dao;

import com.jasadesign.database.DatabaseConnection;
import com.jasadesign.model.User;
import com.jasadesign.model.Pemesan;
import com.jasadesign.model.Pembuat;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class UserDAO {

    // Login pengguna
    public User login(String username, String password)
            throws SQLException {

        String sql = """
                SELECT id, username, role
                FROM users
                WHERE username = ? AND password = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }

        return null;
    }

    // Registrasi pengguna
    public boolean register(String username, String password, String role)
            throws SQLException {

        String sql = """
                INSERT INTO users (username, password, role)
                VALUES (?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);
            stmt.setString(2, password);
            stmt.setString(3, role);

            return stmt.executeUpdate() > 0;
        }
    }

    // Mengambil pengguna berdasarkan username
    public User getUserByUsername(String username)
            throws SQLException {

        String sql = """
                SELECT id, username, role
                FROM users
                WHERE username = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, username);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapUser(rs);
                }
            }
        }

        return null;
    }

    // Mengubah hasil database menjadi objek sesuai role
    private User mapUser(ResultSet rs) throws SQLException {

        int id = rs.getInt("id");
        String username = rs.getString("username");
        String role = rs.getString("role");

        return switch (role) {
            case "pemesan" -> new Pemesan(id, username);
            case "pembuat" -> new Pembuat(id, username);
            default -> throw new SQLException(
                    "Role pengguna tidak valid: " + role
            );
        };
    }
}

