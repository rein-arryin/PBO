package com.jasadesign.model;

public class Pembuat extends User {

    public Pembuat(int id, String username, String password) {
        super(id,username, password);
    }

    @Override
    public String getRole() {
        return "pembuat";
    }
}
