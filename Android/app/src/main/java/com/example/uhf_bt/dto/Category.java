package com.example.uhf_bt.dto;

public class Category {

    private int id;

    private String name;

    private boolean isUsed;

    public Category()
    {

    }

    public String getName()
    {
        return name;
    }

    public Category(int _id, String _name, boolean _isUsed) {
        id = _id;
        name = _name;
        isUsed = _isUsed;
    }

    // Add getters and setters as needed

    @Override
    public String toString() {
        return "Category {" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", isUsed=" + isUsed +
                '}';
    }
}
