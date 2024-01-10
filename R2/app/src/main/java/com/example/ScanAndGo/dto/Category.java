package com.example.ScanAndGo.dto;

import com.google.gson.annotations.SerializedName;

public class Category implements  Comparable<Category> {

    @SerializedName("id")
    public int id;

    @SerializedName("name")

    public String name;

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

    public Category(int _id, String _name) {
        id = _id;
        name = _name;
    }

    // Add getters and setters as needed

    @Override
    public String toString() {
        return "Category {" +
                "id=" + id +
                ", name='" + name +
                '}';
    }

    public int getId() {
        return this.id;
    }
}
