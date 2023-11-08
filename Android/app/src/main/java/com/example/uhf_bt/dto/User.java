package com.example.uhf_bt.dto;

public class User {

    public int id ;

    public String username;

    public int role;

    public User() {

    }

    public User(int _id, String _username, int _role) {
        id = _id;
        username = _username;
        role = _role;
    }
}
