package com.example.android.miriamsbaking.database;

import android.arch.persistence.room.Entity;
import android.arch.persistence.room.Ignore;

import com.example.android.miriamsbaking.model.Step;

@Entity(primaryKeys = {"recipeId","id"})
public class StepWithId {

    int recipeId;
    int id;
    String shortDescription;
    String description;
    String videoURL;
    String thumbnailURL;

    @Ignore
    public StepWithId(int recipeId, Step step){
        this.recipeId = recipeId;
        id = step.getId();
        shortDescription = step.getShortDescription();
        description = step.getDescription();
        videoURL = step.getVideoURL();
        thumbnailURL = step.getThumbnailURL();
    }

    public StepWithId(){

    }

    public int getRecipeId() {
        return recipeId;
    }

    public int getId() {
        return id;
    }

    public String getShortDescription() {
        return shortDescription;
    }

    public String getDescription() {
        return description;
    }

    public String getVideoURL() {
        return videoURL;
    }

    public String getThumbnailURL() {
        return thumbnailURL;
    }
}
