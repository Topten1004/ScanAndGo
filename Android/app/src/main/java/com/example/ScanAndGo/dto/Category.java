package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Category implements  Comparable<Category> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

    @SerializedName("isUsed")

    public boolean isUsed;

    @Override
    public int compareTo(Category otherCategory) {
        // Compare by id
        return Integer.compare(this.id, otherCategory.id);
    }

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
