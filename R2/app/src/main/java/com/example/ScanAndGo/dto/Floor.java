package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Floor  implements  Comparable<Floor> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("area_id")

    public int areaId;

    @Override
    public int compareTo(Floor otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

    @Override
    public String toString() {
        return "Item {" +
                "id=" + id +
                ", name='" + name + ",area_id=" + this.areaId + "'}'";
    }
    public Floor()
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
    public Floor(int id, int areaId, String name) {
        this.id = id;
        this.areaId = areaId;
        this.name = name;
    }
}
