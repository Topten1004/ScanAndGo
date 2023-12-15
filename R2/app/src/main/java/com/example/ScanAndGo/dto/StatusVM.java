package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class StatusVM {

    @SerializedName("status")
    public int status;

    public StatusVM() {

    }

    public StatusVM(int status){

        this.status = status;

    }
}
