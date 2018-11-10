package com.example.android.miriamsbaking;

import android.app.IntentService;
import android.appwidget.AppWidgetManager;
import android.content.ComponentName;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.preference.PreferenceManager;
import android.support.annotation.Nullable;

public class UpdateWidgetService extends IntentService{


    private static final String ACTION_UPDATE_WIDGETS= "update widgets";

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
        // Get the recipeId from the sharepreferences
        SharedPreferences sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this);
        int recipeId = sharedPreferences.getInt(getString(R.string.favorite_key)
                ,getResources().getInteger(R.integer.default_favorite_id));


        AppWidgetManager widgetManager = AppWidgetManager.getInstance(this);
        int[] widgetIds = widgetManager.getAppWidgetIds(new ComponentName(  this,AppWidget.class));
        // Finally update all widgets
        AppWidget.updateWidgets(this,widgetManager,widgetIds,RecipeUtil.getRecipe(this,recipeId));

    }

    public static void startActionUpdateWidget(Context context){
       Intent intent = new Intent(context,UpdateWidgetService.class);
       intent.setAction(ACTION_UPDATE_WIDGETS);
       context.startService(intent);
    }

}
