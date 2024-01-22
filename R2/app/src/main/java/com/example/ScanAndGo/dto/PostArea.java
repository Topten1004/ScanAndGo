package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostArea {

    @SerializedName("name")
    public String name;

    @SerializedName("buildingId")

    public int buildingId;
    public PostArea() {

    }

    public PostArea(String name, int categoryId) {

        this.name = name;
        this.buildingId = categoryId;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"buildingId\":\"" + this.buildingId + "\""
                + "}";
    }
}
