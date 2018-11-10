package com.example.android.miriamsbaking.model;

import com.google.gson.annotations.SerializedName;

public class Ingredient {
    @SerializedName("quantity")
    private final
    float quantity;
    @SerializedName("measure")
    private final
    String measure;
    @SerializedName("ingredient")
    private final
    String ingredient;

    public Ingredient(float quantity, String measure, String ingredient) {
        this.quantity = quantity;
        this.measure = measure;
        this.ingredient = ingredient;
    }

    public String getIngredient() {
        return ingredient;
    }

    public String getMeasure() {
        return measure;
    }

    public float getQuantity() {
        return quantity;
    }
}
