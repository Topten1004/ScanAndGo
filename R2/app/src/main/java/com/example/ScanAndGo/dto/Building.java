package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Building {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    public Building() {

    }

    public Building(int id, String name) {

        this.id = id;
        this.name = name;
    }
}
