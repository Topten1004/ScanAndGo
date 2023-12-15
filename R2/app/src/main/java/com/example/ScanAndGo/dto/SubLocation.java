package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class SubLocation  implements  Comparable<SubLocation> {

    @SerializedName("id")
    public int id;

    @SerializedName("locationId")

    public int locationId;

    @SerializedName("name")

    public String name;

    @Override
    public int compareTo(SubLocation otherLocation) {
        // Compare by id
        return Integer.compare(this.id, otherLocation.id);
    }

    public SubLocation()

    {

    }

    public String getName()
    {
        return name;
    }

    public SubLocation(int id, String name, int locationId) {

        this.id = id;
        this.name = name;
        this.locationId = locationId;
    }
}
