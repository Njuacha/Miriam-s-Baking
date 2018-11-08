package com.example.android.miriamsbaking;

import android.app.PendingIntent;
import android.appwidget.AppWidgetManager;
import android.appwidget.AppWidgetProvider;
import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.widget.RemoteViews;

import com.example.android.miriamsbaking.activity.MainActivity;
import com.example.android.miriamsbaking.activity.RecipeActivity;
import com.example.android.miriamsbaking.model.Recipe;

import static com.example.android.miriamsbaking.activity.MainActivity.RECIPE_EXTRA;

/**
 * Implementation of App Widget functionality.
 */
public class AppWidget extends AppWidgetProvider {

    static void updateAppWidget(Context context, AppWidgetManager appWidgetManager,
                                int appWidgetId, Recipe recipe) {

        String recipeName = (recipe == null)?context.getString(R.string.default_recipe_name)
                :recipe.getName() ;
        String ingredients = (recipe == null)?context.getString(R.string.default_recipe_name)
                :RecipeUtil.getIngredients(context,recipe);

        // Construct the RemoteViews object
        RemoteViews views = new RemoteViews(context.getPackageName(), R.layout.app_widget);
        views.setTextViewText(R.id.tv_recipe, recipeName);
        views.setTextViewText(R.id.tv_ingredients,ingredients);

        // Create a prending intent to launch activity when widget is touched
        Intent intent;
        if(recipe != null){
            intent = new Intent(context, RecipeActivity.class);
            intent.putExtra(RECIPE_EXTRA,recipe);
        }else{
            intent = new Intent(context, MainActivity.class);
        }

        PendingIntent pendingIntent = PendingIntent.getActivity(context,1,intent,PendingIntent.FLAG_UPDATE_CURRENT);
        views.setOnClickPendingIntent(R.id.tv_ingredients,pendingIntent);
        // Instruct the widget manager to update the widget
        appWidgetManager.updateAppWidget(appWidgetId, views);
    }

    @Override
    public void onUpdate(Context context, AppWidgetManager appWidgetManager, int[] appWidgetIds) {
        // This will start a service which would query the database to get the favorite recipe
        // and then call a static method of this class to update to update the widgets
        UpdateWidgetService.startActionUpdateWidget(context);
    }

    public static void updateWidgets(Context context, AppWidgetManager appWidgetManager,int[] appWidgetIds
            ,Recipe recipe){
        // There may be multiple widgets active, so update all of them
        for (int appWidgetId : appWidgetIds) {
            updateAppWidget(context, appWidgetManager, appWidgetId,recipe);
        }
    }

    @Override
    public void onEnabled(Context context) {
        // Enter relevant functionality for when the first widget is created
    }

    @Override
    public void onDisabled(Context context) {
        // Enter relevant functionality for when the last widget is disabled
    }
}

