package com.example.uhf_bt.dto;

import com.google.gson.annotations.SerializedName;

public class PostItem {

    @SerializedName("name")
    public String name;

    public PostItem() {

    }

    public PostItem(String name)
    {
        this.name = name;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + name + "\"" + "}";
    }
}
