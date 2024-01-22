package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Item  implements  Comparable<Item> {


    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("category_id")

    public int categoryId;

    @SerializedName("barcode")

    public String barcode;

    @Override
    public int compareTo(Item otherItem) {
        // Compare by id
        return Integer.compare(this.id, otherItem.id);
    }

    @Override
    public String toString() {
        return "Item {" +
                "id=" + id +
                ", name='" + name + ",categoryId=" + categoryId + ", barcode=" + barcode +
                '}';
    }
    public Item()
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
    public Item(int id, int categoryId, String name, String barcode) {
        this.id = id;
        this.categoryId = categoryId;
        this.name = name;
        this.barcode = barcode;
    }
}
