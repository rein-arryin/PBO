package com.jasadesign.model;

public class Pembuat extends User {

    public Pembuat(int id, String username){
        super(id,username);
    }

    @Override
    public String getRole() {
        return "pembuat";
    }
}
