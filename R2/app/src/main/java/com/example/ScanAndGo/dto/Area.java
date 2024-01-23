package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Area implements  Comparable<Area> {


    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("building_id")

    public int buildingId;

    @Override
    public int compareTo(Area otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

    @Override
    public String toString() {
        return "Item {" +
                "id=" + id +
                ", name='" + name + ",building_id=" + this.buildingId + "'}'";
    }
    public Area()
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
    public Area(int id, int buildingId, String name) {
        this.id = id;
        this.buildingId = buildingId;
        this.name = name;
    }
}
