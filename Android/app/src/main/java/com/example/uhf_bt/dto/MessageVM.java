package com.example.uhf_bt.dto;

import com.google.gson.annotations.SerializedName;

public class MessageVM {

    @SerializedName("message")
    public String message;

    public MessageVM() {

    }

    public MessageVM(String message){

        this.message = message;
    }
}
