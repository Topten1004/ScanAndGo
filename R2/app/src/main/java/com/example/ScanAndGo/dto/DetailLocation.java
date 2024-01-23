package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class DetailLocation  implements  Comparable<DetailLocation> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("floor_id")

    public int floorId;

    @SerializedName("img_data")

    public String imgData;
    @Override
    public int compareTo(DetailLocation otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

    public String toJsonString() {
        return "{" +
                "id=" + id + "\","
                + "\"name\":\"" + this.name + "\","
                + "\"floorId\":\"" + this.floorId + "\","
                + "\"img_data\":\"" + this.imgData + "\""
                + "}";
    }
    public DetailLocation()
    {

    }

    public String getName()
    {
        return this.name;
    }

    public int getId()
    {
        return this.id;
    }
    public DetailLocation(int id, int floorId, String name, String imgData) {
        this.id = id;
        this.floorId = floorId;
        this.name = name;
        this.imgData = imgData;
    }
}
