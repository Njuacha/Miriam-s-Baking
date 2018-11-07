package com.example.android.miriamsbaking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.miriamsbaking.model.Ingredient;

import java.util.List;

public class RecipeUtil {

    public static String assembleIngredients(List<Ingredient> ingredients){
        StringBuilder allIngredients = new StringBuilder();
        String anIngredient;

        if(ingredients == null) return null;

        for(Ingredient ingredient: ingredients){
            anIngredient = String.format("%.1f %s %s%n",ingredient.getQuantity()
                    ,ingredient.getMeasure(),ingredient.getIngredient());
            allIngredients.append(anIngredient);
        }
        return allIngredients.toString();
    }

    public static void setRecipeAsPrefferedToDisplay(Context context,int recipeId){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.favorite_key),recipeId);
        editor.apply();
    }
}
