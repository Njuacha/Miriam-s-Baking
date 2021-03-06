package com.example.android.miriamsbaking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ProgressBar;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.adapter.RecipeAdapter;
import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.database.IngredientWithId;
import com.example.android.miriamsbaking.database.StepWithId;
import com.example.android.miriamsbaking.model.Ingredient;
import com.example.android.miriamsbaking.model.Recipe;
import com.example.android.miriamsbaking.model.Step;
import com.example.android.miriamsbaking.rest.ApiClient;
import com.example.android.miriamsbaking.rest.ApiInterface;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity implements RecipeAdapter.RecipeClickListener{

    public static final String RECIPE_EXTRA = "recipe";
    @BindView(R.id.rv_recipe)
    RecyclerView rvRecipe;
    private AppDatabase mDb;
    private RecipeAdapter mAdapter;
    @BindView(R.id.pb)
    ProgressBar mPb;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        ButterKnife.bind(this);

        mDb = AppDatabase.getDatabaseInstance(this);
        mAdapter = new RecipeAdapter(this,this);
        
        if (getScreenSize() == Configuration.SCREENLAYOUT_SIZE_LARGE){
            rvRecipe.setLayoutManager(new GridLayoutManager(this,3));
        }else{
            rvRecipe.setLayoutManager(new LinearLayoutManager(this));
        }

        rvRecipe.setAdapter(mAdapter);
        


        new AsyncTask<Void,Void,List<Recipe>>(){
            @Override
            protected List<Recipe> doInBackground(Void... voids) {
                List<Recipe> recipesInDatabase = mDb.recipeDao().getRecipe();
                if ( recipesInDatabase.size()== 0 ){
                    downLoadAndSaveRecipes();
                    recipesInDatabase = mDb.recipeDao().getRecipe();
                }
                return recipesInDatabase;
            }

            @Override
            protected void onPostExecute(List<Recipe> recipes) {
                super.onPostExecute(recipes);
                mAdapter.setRecipes(recipes);
            }
        }.execute();


    }

    private int getScreenSize() {
        return getResources().getConfiguration().screenLayout &
                Configuration.SCREENLAYOUT_SIZE_MASK;
    }

    private void downLoadAndSaveRecipes() {

        ApiInterface apiService = ApiClient.getClient().create(ApiInterface.class);
        apiService.getDataFromWebURL().enqueue(new Callback<List<Recipe>>() {
            @Override
            public void onResponse(Call<List<Recipe>> call, final Response<List<Recipe>> response) {

                new AsyncTask<Void,Void,Void>(){
                    @Override
                    protected void onPreExecute() {
                        mPb.setVisibility(View.VISIBLE);
                        super.onPreExecute();
                    }

                    @Override
                    protected Void doInBackground(Void... voids) {
                        storeInDatabase(response.body());
                        return null;
                    }

                    @Override
                    protected void onPostExecute(Void aVoid) {
                        mPb.setVisibility(View.INVISIBLE);
                        super.onPostExecute(aVoid);
                    }
                }.execute();
            }

            @Override
            public void onFailure(Call<List<Recipe>> call, Throwable t) {

            }
        });
    }

    private void storeInDatabase(List<Recipe> recipes) {
        List<IngredientWithId> ingredientsWithId ;
        List<StepWithId> stepWithIds;

        // Inserting the recipes in database
        mDb.recipeDao().insertRecipes(recipes);
        // Create list of ingredientsWithId and list of stepsWithId and insert in database for each recipe
        for (Recipe recipe: recipes){
            // Insert the ingredients in Db
            ingredientsWithId = attachRecipeIdToIngredients(recipe.getId(),recipe.getIngredients());
            mDb.ingredientDao().insertIngredient(ingredientsWithId);
            // Insert the steps in Db
            stepWithIds = attachRecipeIdToSteps(recipe.getId(),recipe.getSteps());
            mDb.stepDao().insertStep(stepWithIds);

        }
    }

    private List<IngredientWithId> attachRecipeIdToIngredients(int recipeId, List<Ingredient> ingredients){
        List<IngredientWithId> ingredientsWithId = new ArrayList<>();
        for (Ingredient ingredient: ingredients){
            ingredientsWithId.add(new IngredientWithId(recipeId,ingredient));
        }
        return ingredientsWithId;
    }

    private List<StepWithId> attachRecipeIdToSteps(int recipeId, List<Step> steps){
        List<StepWithId> stepWithIds = new ArrayList<>();
        for (Step step: steps){
            stepWithIds.add(new StepWithId(recipeId,step));
        }
        return stepWithIds;
    }

    @Override
    public void onRecipeClicked(Recipe recipe) {
        startActivity(new Intent(MainActivity.this,RecipeActivity.class)
                .putExtra(RECIPE_EXTRA,recipe));
    }
}

