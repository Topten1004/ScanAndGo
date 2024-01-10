package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class PostInventory {

    @SerializedName("barcode")
    public String barcode;

    @SerializedName("status")

    public int status;

    @SerializedName("photo")
    public String photo;

    @SerializedName("comment")
    public String comment;

    @SerializedName("category_id")

    public int category_id;

    @SerializedName("item_id")
    public int item_id;
    @SerializedName("building_id")

    public int building_id;
    @SerializedName("area_id")


    public int area_id;
    @SerializedName("floor_id")

    public  int floor_id;

    @SerializedName("detail_location_id")

    public int detail_location_id;

    public PostInventory() {

    }

    public PostInventory(String barcode, int status, String photo, String comment, int category_id, int item_id, int building_id, int area_id, int floor_id, int detail_location_id) {

        this.barcode = barcode;
        this.status = status;
        this.photo = photo;
        this.comment = comment;
        this.category_id = category_id;
        this.item_id = item_id;
        this.building_id = building_id;
        this.area_id = area_id;
        this.floor_id = floor_id;
        this.detail_location_id = detail_location_id;
    }

    public String toJsonString() {
        return "{" +
                "\"barcode\":\"" + barcode + "\"," +
                "\"status\":" + status + "," +
                "\"photo\":\"" + photo + "\"," +
                "\"comment\":\"" + comment + "\"," +
                "\"category_id\":" + category_id + "," +
                "\"item_id\":" + item_id + "," +
                "\"building_id\":" + building_id + "," +
                "\"area_id\":" + area_id + "," +
                "\"floor_id\":" + floor_id + "," +
                "\"detail_location_id\":" + detail_location_id +
                "}";
    }

}
