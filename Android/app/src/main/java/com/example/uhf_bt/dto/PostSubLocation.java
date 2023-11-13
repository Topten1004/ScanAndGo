package com.example.uhf_bt.dto;

import com.google.gson.annotations.SerializedName;

public class PostSubLocation {

    @SerializedName("name")
    public String name;

    @SerializedName("locationId")

    public int locationId;
    public PostSubLocation() {

    }

    public PostSubLocation(String name, int locationId) {

        this.name = name;
        this.locationId = locationId;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"locationId\":\"" + this.locationId + "\""
                + "}";
    }
}
