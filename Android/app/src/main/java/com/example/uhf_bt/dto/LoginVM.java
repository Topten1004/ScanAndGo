package com.example.uhf_bt.dto;

import com.google.gson.annotations.SerializedName;

public class LoginVM {

    @SerializedName("message")
    public String message;

    @SerializedName("access_token")

    public String access_token;

    @SerializedName("refresh_token")

    public String refresh_token;

    @SerializedName("status")

    public int status;

    @SerializedName("user")

    public User user;

    public LoginVM()
    {
        user = new User();
    }

    public LoginVM( String _message, String _access_token, String _refresh_token, int _status, User _user)
    {
        message = _message;

        access_token = _access_token;

        refresh_token = _refresh_token;

        status = _status;

        user = _user;
    }
}
