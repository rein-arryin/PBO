package com.jasadesign.dao;

import com.jasadesign.model.DesignRequest;
import java.util.List;

public interface RequestManagement {
    boolean create(DesignRequest request);

    List<DesignRequest> getAll();

    boolean updateStatus(int id, String status);

    boolean delete(int id);
}
