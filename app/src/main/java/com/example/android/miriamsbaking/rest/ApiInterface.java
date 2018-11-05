package com.example.android.miriamsbaking.rest;

import com.example.android.miriamsbaking.model.Recipe;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface ApiInterface {

    @GET("59121517_baking/baking.json")
    Call<List<Recipe>> getDataFromWebURL();
}
