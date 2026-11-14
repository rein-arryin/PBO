package com.jasadesign.model;

public abstract class User {

    protected int id;
    protected String username;

    public User(int id, String username) {
        this.id = id;
        this.username = username;
    }

    public abstract String getRole();

    public int getId() {
        return id;
    }

    public String getUsername() {
        return username;
    }
}