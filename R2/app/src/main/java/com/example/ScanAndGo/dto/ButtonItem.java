package com.example.ScanAndGo.dto;

public class ButtonItem {

    public int type; // 1: Category, 2: Item, 3: Building, 4: Area, 5: Floor, 6: Detail_location

    public int id;
    public boolean isUsed;
    private String mainButtonText;

    public ButtonItem(String mainButtonText, int type, int id) {

        this.mainButtonText = mainButtonText;
        this.type = type;
        this.id = id;

    }

    public String getMainButtonText() {
        return mainButtonText;
    }
}
