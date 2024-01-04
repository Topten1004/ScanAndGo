package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

import java.util.ArrayList;
import java.util.List;

public class ResponseCheckTag {
    @SerializedName("right_list")
    public List<String> rightLists = new ArrayList<>();

    @SerializedName("wrong_list")

    public List<String> wrongLists = new ArrayList<>();
    @SerializedName("missing_list")

    public List<String> missingLists = new ArrayList<>();

    @SerializedName("unknown_list")

    public List<String> unknownLists = new ArrayList<>();
    public ResponseCheckTag() {
    }

    public ResponseCheckTag(List<String> rightLists, List<String> wrongLists, List<String> missingLists, List<String> unknownLists) {
        this.rightLists = rightLists;
        this.wrongLists = wrongLists;
        this.missingLists = missingLists;
        this.unknownLists = unknownLists;
    }
}
