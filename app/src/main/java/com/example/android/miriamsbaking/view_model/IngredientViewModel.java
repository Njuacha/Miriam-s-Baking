package com.example.android.miriamsbaking.view_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.model.Ingredient;

import java.util.List;

public class IngredientViewModel extends ViewModel {
    private LiveData<List<Ingredient>> ingredients;

    public IngredientViewModel(int recipeId, AppDatabase db){
        ingredients = db.ingredientDao().getIngredientWithRecipeId(recipeId);
    }

    public LiveData<List<Ingredient>> getIngredients() {
        return ingredients;
    }
}
