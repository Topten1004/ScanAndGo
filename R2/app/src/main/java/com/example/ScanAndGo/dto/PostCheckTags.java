package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class PostCheckTags {

    @SerializedName("floor_id")
    public int floor_id;
    @SerializedName("barcode_list")

    public List<String> barcodes = new ArrayList<>();

    public PostCheckTags()
    {
        barcodes = new ArrayList<>();
    }
    public PostCheckTags(int floor_id, List<String> barcodes)
    {
        this.floor_id = floor_id;
        this.barcodes = barcodes;
    }

    public String toJsonString() {
        StringBuilder jsonString = new StringBuilder();
        jsonString.append("{");
        jsonString.append("\"floor_id\":").append(floor_id).append(",");
        jsonString.append("\"barcode_list\":").append("[");

        for (int i = 0; i < barcodes.size(); i++) {
            jsonString.append("\"").append(barcodes.get(i)).append("\"");
            if (i < barcodes.size() - 1) {
                jsonString.append(",");
            }
        }

        jsonString.append("]");
        jsonString.append("}");

        return jsonString.toString();
    }
}
