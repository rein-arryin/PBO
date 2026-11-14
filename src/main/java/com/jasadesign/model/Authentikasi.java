package com.jasadesign.model;

public interface Authentikasi {
    boolean login(String username, String password);

    void logout();
}
