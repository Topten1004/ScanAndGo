package com.example.uhf_bt.dto;

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
