package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostCategory {

    @SerializedName("name")
    public String name;

    public PostCategory() {

    }

    public PostCategory(String name)
    {
        this.name = name;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + name + "\"" + "}";
    }
}
