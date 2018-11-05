package com.example.android.miriamsbaking.database;

import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.miriamsbaking.model.Recipe;

import java.util.List;

@Dao
public interface RecipeDao {
    @Insert
    void insertRecipes(List<Recipe> recipes);

    @Query("Select * from Recipe ")
    List<Recipe> getRecipe();
}
