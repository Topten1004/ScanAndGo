package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class QrReturn {

    public QrReturn() {

    }

    @SerializedName("building_id")
    public int building_id = -1;

    @SerializedName("area_id")
    public  int area_id = -1;

    @SerializedName("floor_id")
    public int floor_id = -1;

    @SerializedName("block_id")
    public int block_id = -1;
}
