package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostDetailLocation {

    @SerializedName("name")
    public String name;

    @SerializedName("floorId")

    public int floorId;

    @SerializedName("img_data")

    public String imgData;

    public PostDetailLocation() {

    }

    public PostDetailLocation(String name, int floorId, String imgData) {

        this.name = name;
        this.floorId = floorId;
        this.imgData = imgData;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"floorId\":\"" + this.floorId + "\","
                + "\"img_data\":\"" + this.imgData + "\""
                + "}";
    }
}
