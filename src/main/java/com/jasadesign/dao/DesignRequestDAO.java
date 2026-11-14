package com.jasadesign.dao;

import com.jasadesign.database.DatabaseConnection;
import com.jasadesign.model.DesignRequest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Date;

import java.time.LocalDate;

import java.util.ArrayList;
import java.util.List;

public class DesignRequestDAO {

    // Menambahkan pesanan desain
    public boolean create(DesignRequest request) throws SQLException {

        String sql = """
                INSERT INTO design_requests (
                    user_id,
                    title,
                    design_type,
                    description,
                    color,
                    size,
                    reference_link,
                    deadline
                )
                VALUES (?, ?, ?, ?, ?, ?, ?, ?)
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, request.getUserId());
            stmt.setString(2, request.getTitle());
            stmt.setString(3, request.getDesignType());
            stmt.setString(4, request.getDescription());
            stmt.setString(5, request.getColor());
            stmt.setString(6, request.getSize());
            stmt.setString(7, request.getReferenceLink());

            if (request.getDeadline() != null) {
                stmt.setDate(8, Date.valueOf(request.getDeadline()));
            } else {
                stmt.setNull(8, java.sql.Types.DATE);
            }

            return stmt.executeUpdate() > 0;
        }
    }

    // Mengambil pesanan berdasarkan ID
    public DesignRequest getById(int id) throws SQLException {

        String sql = """
                SELECT *
                FROM design_requests
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            try (ResultSet rs = stmt.executeQuery()) {

                if (rs.next()) {
                    return mapResultSet(rs);
                }
            }
        }

        return null;
    }

    // Mengambil semua pesanan milik seorang pemesan
    public List<DesignRequest> getByUserId(int userId) throws SQLException {

        String sql = """
                SELECT *
                FROM design_requests
                WHERE user_id = ?
                ORDER BY created_at DESC
                """;

        List<DesignRequest> requests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, userId);

            try (ResultSet rs = stmt.executeQuery()) {

                while (rs.next()) {
                    requests.add(mapResultSet(rs));
                }
            }
        }

        return requests;
    }

    // Mengambil seluruh pesanan
    public List<DesignRequest> getAll() throws SQLException {

        String sql = """
                SELECT *
                FROM design_requests
                ORDER BY created_at DESC
                """;

        List<DesignRequest> requests = new ArrayList<>();

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {

            while (rs.next()) {
                requests.add(mapResultSet(rs));
            }
        }

        return requests;
    }

    // Mengubah status pesanan
    public boolean updateStatus(int id, String status) throws SQLException {

        String sql = """
                UPDATE design_requests
                SET status = ?
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setString(1, status);
            stmt.setInt(2, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // Menghapus pesanan
    public boolean delete(int id) throws SQLException {

        String sql = """
                DELETE FROM design_requests
                WHERE id = ?
                """;

        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {

            stmt.setInt(1, id);

            return stmt.executeUpdate() > 0;
        }
    }

    // Mengubah hasil query menjadi objek DesignRequest
    private DesignRequest mapResultSet(ResultSet rs) throws SQLException {

        Date deadlineDate = rs.getDate("deadline");

        LocalDate deadline = deadlineDate != null
                ? deadlineDate.toLocalDate()
                : null;

        return new DesignRequest(
                rs.getInt("id"),
                rs.getInt("user_id"),
                rs.getString("title"),
                rs.getString("design_type"),
                rs.getString("description"),
                rs.getString("color"),
                rs.getString("size"),
                rs.getString("reference_link"),
                deadline,
                rs.getString("status")
        );
    }
}
