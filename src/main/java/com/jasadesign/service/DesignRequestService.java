package com.jasadesign.service;

import com.jasadesign.config.AppConfig;
import com.jasadesign.dao.DesignRequestDAO;
import com.jasadesign.dao.contract.DesignRequestRepository;
import com.jasadesign.model.DesignRequest;
import com.jasadesign.model.User;

import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;

public class DesignRequestService {

    private final DesignRequestRepository repository;

    // Overloading constructor 1
    public DesignRequestService() {
        this(new DesignRequestDAO());
    }

    // Overloading constructor 2
    public DesignRequestService(DesignRequestRepository repository) {
        this.repository = repository;
    }

    public boolean createRequest(DesignRequest r) throws SQLException {
        if (isBlank(r.getTitle())) {
            throw new IllegalArgumentException("Judul pesanan tidak boleh kosong.");
        }
        if (isBlank(r.getDesignType())) {
            throw new IllegalArgumentException("Jenis desain harus dipilih.");
        }
        if (isBlank(r.getDescription())) {
            throw new IllegalArgumentException("Deskripsi tidak boleh kosong.");
        }
        if (r.getDeadline() == null) {
            throw new IllegalArgumentException("Deadline harus diisi.");
        }
        if (r.getDeadline().isBefore(LocalDate.now())) {
            throw new IllegalArgumentException("Deadline tidak boleh di masa lalu.");
        }

        r.setStatus(AppConfig.STATUS_MENUNGGU);
        return repository.create(r);
    }

    public List<DesignRequest> getRequestsByUser(User user) throws SQLException {
        return repository.getByUserId(user.getId());
    }

    // Overloading: semua pesanan
    public List<DesignRequest> getAllRequests() throws SQLException {
        return repository.getAll();
    }

    // Overloading: filter berdasarkan status
    public List<DesignRequest> getAllRequests(String status) throws SQLException {
        return repository.getAll(status);
    }

    public DesignRequest getRequest(int id) throws SQLException {
        DesignRequest r = repository.getById(id);
        if (r == null) {
            throw new IllegalArgumentException("Pesanan tidak ditemukan.");
        }
        return r;
    }

    // Hanya Pembuat yang boleh mengubah status (memakai polymorphism getRole())
    public boolean updateStatus(User actor, int requestId, String newStatus) throws SQLException {
        if (!"pembuat".equals(actor.getRole())) {
            throw new IllegalArgumentException("Hanya pembuat yang boleh mengubah status.");
        }
        if (!isStatusValid(newStatus)) {
            throw new IllegalArgumentException("Status tidak valid.");
        }
        return repository.updateStatus(requestId, newStatus);
    }

    private boolean isStatusValid(String status) {
        return AppConfig.STATUS_MENUNGGU.equals(status)
                || AppConfig.STATUS_DIPROSES.equals(status)
                || AppConfig.STATUS_SELESAI.equals(status);
    }

    private boolean isBlank(String s) {
        return s == null || s.isBlank();
    }
}