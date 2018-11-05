package com.example.android.miriamsbaking.view_model;


import android.arch.lifecycle.ViewModel;
import android.arch.lifecycle.ViewModelProvider;
import android.support.annotation.NonNull;

import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.view_model.IngredientViewModel;

public class IngredientViewModelFactory extends ViewModelProvider.NewInstanceFactory{
    private int recipeId;
    private AppDatabase db;

    public IngredientViewModelFactory(int recipeId,AppDatabase db){
        this.recipeId = recipeId;
        this.db = db;
    }

    @NonNull
    @Override
    public <T extends ViewModel> T create(@NonNull Class<T> modelClass) {
        return (T) new IngredientViewModel(recipeId,db);
    }
}
