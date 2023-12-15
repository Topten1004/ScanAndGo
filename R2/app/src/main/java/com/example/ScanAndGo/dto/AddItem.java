package com.example.ScanAndGo.dto;

public class AddItem {


    public int id;

    public int type; // 1: Sub location Items 2: Assign Item part
    public String date;

    public String name;

    public String barCode;

    public boolean isCheck;
    public AddItem(int id, int type, String name, String date, String barCode, boolean isCheck) {

        this.id = id;
        this.type = type;
        this.name = name;
        this.date = date;
        this.barCode = barCode;
        this.isCheck = isCheck;
    }

    public String getName () {
        return name;
    }

}
