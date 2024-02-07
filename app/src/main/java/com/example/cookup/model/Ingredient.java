package com.example.cookup.model;

import com.google.gson.annotations.SerializedName;
public class Ingredient {
    @SerializedName("id")
    private int id;

    @SerializedName("aisle")
    private String aisle;

    @SerializedName("image")
    private String image;

    @SerializedName("consistency")
    private String consistency;

    @SerializedName("name")
    private String name;

    @SerializedName("original")
    private String original;

    @SerializedName("amount")
    private double amount;

    @SerializedName("unit")
    private String unit;

    // Add other necessary fields

    public String getName() {
        return name;
    }

    public String getOriginal() {
        return original;
    }

    // Add getters for other fields
}
