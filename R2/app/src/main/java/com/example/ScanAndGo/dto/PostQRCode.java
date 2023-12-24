package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostQRCode {

    public PostQRCode(){

    }

    @SerializedName("name")
    public String name;

    public PostQRCode(String name)
    {
        this.name = name;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + name + "\"" + "}";
    }
}
