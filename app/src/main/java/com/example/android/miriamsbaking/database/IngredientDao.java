package com.example.android.miriamsbaking.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.miriamsbaking.model.Ingredient;

import java.util.List;

@Dao
public interface IngredientDao {
    @Insert
    void insertIngredient(List<IngredientWithId> ingredientWithIds);

    @Query("Select quantity,measure,ingredient from IngredientWithId where recipeId = :recipeId")
    LiveData<List<Ingredient>> getIngredientWithRecipeId(int recipeId);

    @Query("Select quantity,measure,ingredient from IngredientWithId where recipeId = :recipeId")
    List<Ingredient> getIngredientWithRecipeIdNotLiveData(int recipeId);
}
