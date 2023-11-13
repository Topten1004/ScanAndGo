package com.example.uhf_bt.dto;

import com.google.gson.annotations.SerializedName;

public class PostSubCategory {

    @SerializedName("name")
    public String name;

    @SerializedName("categoryId")

    public int categoryId;
    public PostSubCategory() {

    }

    public PostSubCategory(String name, int categoryId) {

        this.name = name;
        this.categoryId = categoryId;
    }

    public String toJsonString() {
        return "{"
                + "\"name\":\"" + this.name + "\","
                + "\"categoryId\":\"" + this.categoryId + "\""
                + "}";
    }
}
