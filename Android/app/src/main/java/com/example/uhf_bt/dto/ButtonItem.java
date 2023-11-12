package com.example.uhf_bt.dto;

public class ButtonItem {

    public int type;

    public int id;

    public boolean isUsed;
    private String mainButtonText;

    public ButtonItem(String mainButtonText, int type, int id, boolean isUsed) {

        this.mainButtonText = mainButtonText;
        this.type = type;
        this.id = id;
        this.isUsed = isUsed;
    }

    public String getMainButtonText() {
        return mainButtonText;
    }
}
