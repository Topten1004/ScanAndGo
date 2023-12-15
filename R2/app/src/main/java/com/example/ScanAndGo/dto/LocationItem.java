package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class LocationItem implements  Comparable<LocationItem>{

    @SerializedName("id")
    public int id;

    @SerializedName("item_name")
    public String item_name;

    @SerializedName("reg_date")
    public String reg_date;

    @SerializedName("barcode")
    public String barcode;

    @Override
    public int compareTo(LocationItem otherLocation) {
        // Compare by id
        return Integer.compare(this.id, otherLocation.id);
    }


    public LocationItem(int id, String item_name, String reg_date, String barcode) {

        this.id = id;
        this.item_name = item_name;
        this.reg_date = reg_date;
        this.barcode = barcode;
    }

    public LocationItem() {

    }
}
