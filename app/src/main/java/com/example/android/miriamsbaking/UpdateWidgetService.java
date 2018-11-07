package com.example.android.miriamsbaking;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.model.Ingredient;
import com.example.android.miriamsbaking.model.Recipe;

import java.util.List;

public class UpdateWidgetService extends IntentService{


    private static String ACTION_UPDATE_WIDGETS= "update widgets";

    public UpdateWidgetService() {
        super("UpdateWidgetService");

    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        if(intent != null){
            final String action = intent.getAction();

            if(ACTION_UPDATE_WIDGETS.equals(action)){
                carryOnWidgetUpdate();
            }
        }

    }

    private void carryOnWidgetUpdate() {
        AppDatabase db= AppDatabase.getDatabaseInstance(this);
        // Get the recipeId from the sharepreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int recipeId = sharedPreferences.getInt(getString(R.string.favorite_key)
                ,getResources().getInteger(R.integer.default_favorite_id));

        // Use the recipeId to get the recipeName and Ingredients
        String recipeName;
        Recipe recipe = db.recipeDao().getRecipe(recipeId);
        recipeName = (recipe == null)?getString(R.string.default_recipe_name):recipe.getName() ;

        // Use the recipeId to get the Ingredients
        String ingredients;
        List<Ingredient> ingredientList = db.ingredientDao().getIngredientWithRecipeIdNotLiveData(recipeId);
        ingredients = RecipeUtil.assembleIngredients(ingredientList);
        ingredients = (ingredients == null)?"List Of ingredients":ingredients;

        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(  this,AppWidget.class));
        // Finally update all widgets
        AppWidget.updateWidgets(this,widgetManager,widgetIds,recipeName,ingredients);


    }

    public static void startActionUpdateWidget(Context context){
       Intent intent = new Intent(context,UpdateWidgetService.class);
       intent.setAction(ACTION_UPDATE_WIDGETS);
       context.startService(intent);
    }

}