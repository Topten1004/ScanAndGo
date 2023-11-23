package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class ReadAllItem implements  Comparable<ReadAllItem> {

    @SerializedName("id")
    public int id;

    @SerializedName("subcategoryId")
    public int subcategoryId;

    @SerializedName("name")

    public String name;
    @SerializedName("barcode")

    public String barcode;
    public  ReadAllItem() {}

    public ReadAllItem(int id, int subCategoryId, String name, String barcode) {

        this.id = id;

        this.subcategoryId = subCategoryId;

        this.name = name;

        this.barcode = barcode;
    }

    @Override
    public int compareTo(ReadAllItem otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

}
