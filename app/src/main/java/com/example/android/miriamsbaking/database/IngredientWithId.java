package com.example.android.miriamsbaking.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;
import android.support.annotation.NonNull;

import com.example.android.miriamsbaking.model.Ingredient;

@Entity(primaryKeys = {"recipeId","ingredient"})
public class IngredientWithId {

    int recipeId;

    float quantity;

    String measure;
    @NonNull
    String ingredient;

    @Ignore
    public IngredientWithId(int recipeId, Ingredient ingredient){

        this.recipeId = recipeId;
        quantity = ingredient.getQuantity();
        measure = ingredient.getMeasure();
        this.ingredient = ingredient.getIngredient();

    }

    public IngredientWithId(){

    }

    public int getRecipeId() {
        return recipeId;
    }

    public float getQuantity() {
        return quantity;
    }

    public String getMeasure() {
        return measure;
    }

    public String getIngredient() {
        return ingredient;
    }
}
