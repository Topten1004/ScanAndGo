package com.example.uhf_bt.dto;

public class LoginVM {

    public String message;

    public String access_token;

    public String refresh_token;

    public int status;

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
