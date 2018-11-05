package com.example.android.miriamsbaking.database;

import android.arch.lifecycle.LiveData;
import android.arch.persistence.room.Dao;
import android.arch.persistence.room.Insert;
import android.arch.persistence.room.Query;

import com.example.android.miriamsbaking.model.Step;

import java.util.List;

@Dao
public interface StepDao {
    @Insert
    void insertStep(List<StepWithId> stepWithId);

    @Query("Select id,shortDescription,description,thumbnailURL,videoURL from StepWithId where recipeId =:recipeId")
    LiveData<List<Step>> getStepsWithRecipeId(int recipeId);

}
