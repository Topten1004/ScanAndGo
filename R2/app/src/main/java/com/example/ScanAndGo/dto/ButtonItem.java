package com.example.ScanAndGo.dto;

public class ButtonItem {

    public int type; // 1: Category, 2: Location, 3: Sub Category, 4: Sub Location

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
