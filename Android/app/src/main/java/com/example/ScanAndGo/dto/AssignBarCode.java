package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class AssignBarCode {

    @SerializedName("barcode")
    public String barcode;

    public AssignBarCode()
    {

    }

    public AssignBarCode(String barcode)
    {
        this.barcode = barcode;
    }

    public String toJsonString() {
        return "{"
                + "\"barcode\":\"" + this.barcode + "\"" + "}";
    }
}
