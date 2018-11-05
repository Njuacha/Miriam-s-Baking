package com.example.android.miriamsbaking.database;

import android.arch.persistence.room.Database;
import android.arch.persistence.room.Room;
import android.arch.persistence.room.RoomDatabase;
import android.content.Context;

import com.example.android.miriamsbaking.model.Recipe;

@Database(entities = {Recipe.class,IngredientWithId.class,StepWithId.class},version = 1,exportSchema = false)
public abstract class AppDatabase extends RoomDatabase{
    private static final String DATABASE_NAME = "Recipe database";
    private static AppDatabase databaseInstance ;
    private static final Object LOCK = new Object();

    public static AppDatabase getDatabaseInstance(Context context) {
        if (databaseInstance == null){
            synchronized (LOCK){
                databaseInstance = Room.databaseBuilder(context.getApplicationContext(),AppDatabase.class, DATABASE_NAME)
                        .fallbackToDestructiveMigration()
                        .build();
            }
        }
        return databaseInstance;
    }

    public abstract RecipeDao recipeDao();
    public abstract IngredientDao ingredientDao();
    public abstract StepDao stepDao();
}

