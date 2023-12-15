package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class SubCategory implements  Comparable<SubCategory> {

    @SerializedName("id")
    public int id;

    @SerializedName("categoryId")

    public int categoryId;

    @SerializedName("name")

    public String name;

    @SerializedName("isUsed")

    public boolean isUsed;

    @Override
    public int compareTo(SubCategory otherCategory) {
        // Compare by id
        return Integer.compare(this.id, otherCategory.id);
    }

    public SubCategory()

    {

    }

    public String getName()
    {
        return name;
    }

    public SubCategory(int id, String name, boolean isUsed, int categoryId) {

        this.id = id;
        this.name = name;
        this.isUsed = isUsed;
        this.categoryId = categoryId;
    }
}