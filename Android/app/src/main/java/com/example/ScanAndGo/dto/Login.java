package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Login {

    @SerializedName("username")

    public String username;

    @SerializedName("password")

    public String password;

    public Login()
    {

    }
    public Login(String _username, String _password)
    {
        username = _username;
        password = _password;
    }

    public String toJsonString() {
        return "{"
                + "\"username\":\"" + username + "\","
                + "\"password\":\"" + password + "\""
                + "}";
    }
}
