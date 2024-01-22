package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostBuilding {

    @SerializedName("name")
    public String name;

    public PostBuilding() {

    }

    public PostBuilding(String name) {

        this.name = name;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\""
                + "}";
    }

}
