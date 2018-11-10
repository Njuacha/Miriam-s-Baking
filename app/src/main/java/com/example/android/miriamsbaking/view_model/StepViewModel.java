package com.example.android.miriamsbaking.view_model;

import android.arch.lifecycle.LiveData;
import android.arch.lifecycle.ViewModel;

import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.model.Step;

import java.util.List;

public class StepViewModel extends ViewModel {
    final LiveData<List<Step>> steps;

    public StepViewModel(int recipeId, AppDatabase db){
        steps = db.stepDao().getStepsWithRecipeId(recipeId);
    }

    public LiveData<List<Step>> getSteps() {
        return steps;
    }
}
