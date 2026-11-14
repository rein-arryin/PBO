package com.jasadesign.dao;

import com.jasadesign.dao.contract.DesignRequestRepository;
import com.jasadesign.database.DatabaseConnection;
import com.jasadesign.model.DesignRequest;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;

public class DesignRequestDAO implements DesignRequestRepository {

    @Override
    public boolean create(DesignRequest r) throws SQLException {
        String sql = "INSERT INTO design_requests "
                + "(user_id, title, design_type, description, color, size, reference_link, deadline, status) "
                + "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, r.getUserId());
            ps.setString(2, r.getTitle());
            ps.setString(3, r.getDesignType());
            ps.setString(4, r.getDescription());
            ps.setString(5, r.getColor());
            ps.setString(6, r.getSize());
            ps.setString(7, r.getReferenceLink());
            ps.setDate(8, r.getDeadline() == null ? null : Date.valueOf(r.getDeadline()));
            ps.setString(9, r.getStatus());
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public DesignRequest getById(int id) throws SQLException {
        String sql = "SELECT * FROM design_requests WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            try (ResultSet rs = ps.executeQuery()) {
                return rs.next() ? mapRow(rs) : null;
            }
        }
    }

    @Override
    public List<DesignRequest> getByUserId(int userId) throws SQLException {
        String sql = "SELECT * FROM design_requests WHERE user_id = ? ORDER BY created_at DESC";
        List<DesignRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, userId);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public List<DesignRequest> getAll() throws SQLException {
        String sql = "SELECT * FROM design_requests ORDER BY created_at DESC";
        List<DesignRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {
            while (rs.next()) {
                list.add(mapRow(rs));
            }
        }
        return list;
    }

    @Override
    public List<DesignRequest> getAll(String status) throws SQLException {
        String sql = "SELECT * FROM design_requests WHERE status = ? ORDER BY created_at DESC";
        List<DesignRequest> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            try (ResultSet rs = ps.executeQuery()) {
                while (rs.next()) {
                    list.add(mapRow(rs));
                }
            }
        }
        return list;
    }

    @Override
    public boolean updateStatus(int id, String status) throws SQLException {
        String sql = "UPDATE design_requests SET status = ? WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setString(1, status);
            ps.setInt(2, id);
            return ps.executeUpdate() > 0;
        }
    }

    @Override
    public boolean delete(int id) throws SQLException {
        String sql = "DELETE FROM design_requests WHERE id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement ps = conn.prepareStatement(sql)) {
            ps.setInt(1, id);
            return ps.executeUpdate() > 0;
        }
    }

    private DesignRequest mapRow(ResultSet rs) throws SQLException {
        DesignRequest r = new DesignRequest();
        r.setId(rs.getInt("id"));
        r.setUserId(rs.getInt("user_id"));
        r.setTitle(rs.getString("title"));
        r.setDesignType(rs.getString("design_type"));
        r.setDescription(rs.getString("description"));
        r.setColor(rs.getString("color"));
        r.setSize(rs.getString("size"));
        r.setReferenceLink(rs.getString("reference_link"));

        Date deadline = rs.getDate("deadline");
        r.setDeadline(deadline == null ? null : deadline.toLocalDate());

        r.setStatus(rs.getString("status"));

        Timestamp created = rs.getTimestamp("created_at");
        r.setCreatedAt(created == null ? null : created.toLocalDateTime());
        return r;
    }
}