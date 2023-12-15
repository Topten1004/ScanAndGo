package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostLocation {

    @SerializedName("name")
    public String name;

    public PostLocation() {

    }

    public PostLocation(String name)
    {
        this.name = name;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + name + "\"" + "}";
    }

}
