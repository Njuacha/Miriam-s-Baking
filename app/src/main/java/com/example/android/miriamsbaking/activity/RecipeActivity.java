package com.example.android.miriamsbaking.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.adapter.StepAdapter;
import com.example.android.miriamsbaking.database.AppDatabase;
import com.example.android.miriamsbaking.fragment.StepFragment;
import com.example.android.miriamsbaking.model.Ingredient;
import com.example.android.miriamsbaking.model.Recipe;
import com.example.android.miriamsbaking.model.Step;
import com.example.android.miriamsbaking.view_model.IngredientViewModel;
import com.example.android.miriamsbaking.view_model.IngredientViewModelFactory;
import com.example.android.miriamsbaking.view_model.StepViewModel;
import com.example.android.miriamsbaking.view_model.StepViewModelFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.miriamsbaking.activity.MainActivity.RECIPE_EXTRA;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.StepListener, StepFragment.StepFragListener{

    public static final String STEP_INDEX_EXTRA = "step id";
    public static final String STEPS_EXTRA = "";
    private boolean mTabletMode;
    private List<Step> mRecipeSteps;
    private StepAdapter mStepAdapter;
    private int mStepIndex;
    private SimpleExoPlayer mExoPlayer;

    @BindView(R.id.tv_ingredients)
    TextView mTvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;





    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);


        if (findViewById(R.id.step_container) == null){
            mTabletMode = false;
        }else{
            mTabletMode = true;
        }
        // Get the recipe from intent and use to get Recipe Name, Steps and Ingredients
        Intent intent = getIntent();

        if (intent.hasExtra(RECIPE_EXTRA)) {
            // Initialize the step adapter since there recipe exist
            mStepAdapter = new StepAdapter(this,this);
            mRvSteps.setAdapter(mStepAdapter);

            Recipe recipe = intent.getParcelableExtra(RECIPE_EXTRA);
            String name = recipe.getName();
            if( name == null){
                // Set the name of the Recipe as title of Action bar
                setTitle(name);
            }

            // Get the recipeId from the recipe and use to query database and obtain the ingredients and steps
            setUpIngredients(recipe.getId());
            setUpSteps(recipe.getId());


        }






    }

    private void setUpSteps(int recipeId) {
        StepViewModelFactory factory = new StepViewModelFactory(recipeId,AppDatabase.getDatabaseInstance(this));
        StepViewModel viewModel = ViewModelProviders.of(this,factory).get(StepViewModel.class);

        viewModel.getSteps().observe(this, new Observer<List<Step>>() {
            @Override
            public void onChanged(@Nullable List<Step> steps) {
                mRecipeSteps = steps ;
                mStepAdapter.setSteps(steps);

                if(mTabletMode){
                    StepFragment fragment = new StepFragment();
                    fragment.setStepIndex(0);
                    fragment.setSteps(mRecipeSteps.toArray(new Step[0]));
                    getSupportFragmentManager().beginTransaction()
                            .add(R.id.step_container,fragment)
                            .commit();
                }
            }
        });
    }

    private void setUpIngredients(int recipeId) {
        IngredientViewModelFactory factory = new IngredientViewModelFactory(recipeId, AppDatabase.getDatabaseInstance(this));
        IngredientViewModel viewModel = ViewModelProviders.of(this,factory).get(IngredientViewModel.class);

        viewModel.getIngredients().observe(this, new Observer<List<Ingredient>>() {
            @Override
            public void onChanged(@Nullable List<Ingredient> ingredients) {
                StringBuilder allIngredients = new StringBuilder();
                String anIngredient;
                for(Ingredient ingredient: ingredients){
                    anIngredient = String.format("%.1f %s %s%n",ingredient.getQuantity()
                            ,ingredient.getMeasure(),ingredient.getIngredient());
                    allIngredients.append(anIngredient);
                }

                mTvIngredients.setText(allIngredients);
            }
        });
    }

    @Override
    public void onStepClicked(int stepIndex) {
        if (mTabletMode){
            mStepIndex = stepIndex;
            updateStepFragment();
            
        }else{
            Intent stepIntent = new Intent(RecipeActivity.this,StepActivity.class);
            stepIntent.putExtra(STEP_INDEX_EXTRA,stepIndex);
            Step[] steps = mRecipeSteps.toArray(new Step[0]);
            stepIntent.putExtra(STEPS_EXTRA,  steps);

            startActivity(stepIntent);
        }
    }

    @Override
    public void onNextBtnClicked() {
        mStepIndex += 1;
        mStepIndex %= mRecipeSteps.size();
        updateStepFragment();
    }

    @Override
    public void onPrevBtnClicked() {
        mStepIndex -= 1;
        if(mStepIndex < 0){
            mStepIndex = mRecipeSteps.size() -1;
        }
        updateStepFragment();

    }

    public void updateStepFragment(){
        StepFragment fragment = new StepFragment();
        fragment.setStepIndex(mStepIndex);
        fragment.setSteps(mRecipeSteps.toArray(new Step[0]));
        // TODO consider updating the stepIndex of the fragment and updating just the appropriate view
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container,fragment)
                .commit();
    }
}
