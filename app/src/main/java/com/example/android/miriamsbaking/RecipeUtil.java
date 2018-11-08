package com.example.android.miriamsbaking;

import android.content.Context;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;

import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.model.Ingredient;
import com.example.android.miriamsbaking.model.Recipe;

import java.util.List;

public class RecipeUtil {


    public static Recipe getRecipe(Context context, int recipeId){
        return AppDatabase.getDatabaseInstance(context).recipeDao().getRecipe(recipeId);
    }

    public static void setRecipeAsPrefferedToDisplay(Context context,int recipeId){
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(context);
        SharedPreferences.Editor editor = preferences.edit();
        editor.putInt(context.getString(R.string.favorite_key),recipeId);
        editor.apply();
    }


    public static String getIngredients(Context context,Recipe recipe){
        // Use the recipeId to get the Ingredients
        List<Ingredient> ingredientList = AppDatabase.getDatabaseInstance(context)
                .ingredientDao().getIngredientWithRecipeIdNotLiveData(recipe.getId());

        return assembleIngredients(ingredientList);
    }

    public static String assembleIngredients(List<Ingredient> ingredientList){
        StringBuilder allIngredients = new StringBuilder();
        String anIngredient;

        if(ingredientList == null) return null;

        for(Ingredient ingredient: ingredientList){
            anIngredient = String.format("%.1f %s %s%n",ingredient.getQuantity()
                    ,ingredient.getMeasure(),ingredient.getIngredient());
            allIngredients.append(anIngredient);
        }

        return  allIngredients.toString();
    }
}
