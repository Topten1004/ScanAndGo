package com.example.ScanAndGo.dto;

import java.util.Date;

public class ResponseCheckItem {

    public int id;
    public String item_name;
    public String category_name;
    public String building_name;
    public String area_name;
    public String floor_name;
    public String detail_location_name;
    public Date purchase_date;
    public Date last_date;
    public String ref_client;
    public Date reg_date;
    public int status;
    public String comment;

    public String barcode;
    public String username;
    public String room_assignment;

    public String category_df_immonet;
    public String purchase_amount;

    public ResponseCheckItem(){

    }
    public ResponseCheckItem(int id, String item_name, String category_name, String building_name, String area_name, String floor_name, String detail_location_name, int status, String comment, String username, String barcode)
    {
        this.id = id;
        this.item_name = item_name;
        this.category_name = category_name;
        this.building_name = building_name;
        this.area_name = area_name;
        this.floor_name = floor_name;
        this.detail_location_name = detail_location_name;
        this.status = status;
        this.comment = comment;
        this.barcode = barcode;
        this.username = username;
    }
}
