package com.example.uhf_bt.dto;

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
