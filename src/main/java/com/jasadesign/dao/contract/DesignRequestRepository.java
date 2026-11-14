package com.jasadesign.dao.contract;

import com.jasadesign.model.DesignRequest;

import java.sql.SQLException;
import java.util.List;

public interface DesignRequestRepository {

    boolean create(DesignRequest request) throws SQLException;

    DesignRequest getById(int id) throws SQLException;

    List<DesignRequest> getByUserId(int userId) throws SQLException;

    // Overloading: semua pesanan
    List<DesignRequest> getAll() throws SQLException;

    // Overloading: pesanan berdasarkan status
    List<DesignRequest> getAll(String status) throws SQLException;

    boolean updateStatus(int id, String status) throws SQLException;

    boolean delete(int id) throws SQLException;
}
