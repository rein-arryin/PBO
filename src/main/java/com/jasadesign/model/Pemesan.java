package com.jasadesign.model;

public class Pemesan extends User {

    public Pemesan(int id, String username, String password) {
        super(id,username,password);
    }

    @Override
    public String getRole() {
        return "pemesan";
    }
}
