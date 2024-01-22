package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostFloor {

    @SerializedName("name")
    public String name;

    @SerializedName("areaId")

    public int areaId;
    public PostFloor() {

    }

    public PostFloor(String name, int areaId) {

        this.name = name;
        this.areaId = areaId;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"areaId\":\"" + this.areaId + "\""
                + "}";
    }
}
