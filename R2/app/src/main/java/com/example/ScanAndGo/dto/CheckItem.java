package com.example.ScanAndGo.dto;

public class CheckItem {

    public int id;

    public int type; // 1: right, 2: wrong 3: missing 4: unknown
    public String date;
    public String name;
    public String barCode;
    public String comment;

    public String building;

    public String area;

    public String floor;

    public String detailLocation;

    public int status;
    public boolean isCheck;
    public CheckItem(int id, int type, String name, String date, String barCode, String comment, int status, boolean isCheck, String building, String area, String floor, String detailLocation) {
        this.id = id;
        this.type = type;
        this.name = name;
        this.date = date;
        this.comment = comment;
        this.barCode = barCode;
        this.status = status;
        this.isCheck = isCheck;

        this.building = building;
        this.area = area;
        this.floor = floor;
        this.detailLocation = detailLocation;
    }

    public CheckItem(){

    }
    public String getName () {
        return name;
    }
}
