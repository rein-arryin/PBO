package com.jasadesign.model;

public class Pemesan extends User {

    public Pemesan(int id, String username){
        super(id,username);
    }

    @Override
    public String getRole() {
        return "pemesan";
    }
}
