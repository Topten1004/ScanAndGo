package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostItem {

    @SerializedName("categoryId")
    public int categoryId;

    @SerializedName("name")
    public String name;

    @SerializedName("barcode")
    public String barcode;

    public PostItem() {

    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"categoryId\":\"" + this.categoryId + "\","
                + "\"barcode\":\"" + this.barcode
                + "}";
    }

    public PostItem(String name, int categoryId, String barcode)
    {
        this.name = name;
        this.categoryId = categoryId;
        this.barcode = barcode;
    }
}
