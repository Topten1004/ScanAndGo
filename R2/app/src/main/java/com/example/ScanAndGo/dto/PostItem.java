package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostItem {

    @SerializedName("subCategoryId")
    public int subCategoryId;

    @SerializedName("name")
    public String name;

    public PostItem() {

    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"subCategoryId\":\"" + this.subCategoryId + "\""
                + "}";
    }

    public PostItem(String name, int subCategoryId)
    {
        this.subCategoryId = subCategoryId;
        this.name = name;
    }
}
