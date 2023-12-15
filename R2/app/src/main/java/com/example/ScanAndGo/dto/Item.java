package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Item  implements  Comparable<Item> {


    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("isUsed")

    public boolean isUsed;

    @SerializedName("subcategoryId")

    public int subcategoryId;

    @Override
    public int compareTo(Item otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

    public Item()

    {

    }

    public String getName()
    {
        return name;
    }

    public Item(int id, int subCategoryId, String name, boolean isUsed) {
        this.id = id;
        this.subcategoryId = subCategoryId;
        this.name = name;
        this.isUsed = isUsed;
    }
}
