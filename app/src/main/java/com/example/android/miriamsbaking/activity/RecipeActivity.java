package com.example.android.miriamsbaking.activity;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Intent;
import android.support.annotation.Nullable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.RecipeUtil;
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

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

import static com.example.android.miriamsbaking.activity.MainActivity.RECIPE_EXTRA;

public class RecipeActivity extends AppCompatActivity implements StepAdapter.StepListener, StepFragment.StepFragListener{

    public static final String STEP_INDEX_EXTRA = "step id";
    public static final String STEPS_EXTRA = "steps";
    public static final String RECIPE_NAME_EXTRA = "recipe name";
    private boolean mTabletMode;
    private List<Step> mRecipeSteps;
    private StepAdapter mStepAdapter;
    private int mStepIndex;
    private String mRecipeName = "";

    @BindView(R.id.tv_ingredients)
    TextView mTvIngredients;
    @BindView(R.id.rv_steps)
    RecyclerView mRvSteps;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_recipe);
        ButterKnife.bind(this);

        ActionBar actionBar = this.getSupportActionBar();
        if( actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }


        mTabletMode = findViewById(R.id.step_container) != null;
        // Get the recipe from intent and use to get Recipe Name, Steps and Ingredients
        Intent intent = getIntent();

        if (intent.hasExtra(RECIPE_EXTRA)) {
            // Initialize the step adapter since there recipe exist
            mStepAdapter = new StepAdapter(this,this);
            mRvSteps.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
            mRvSteps.setAdapter(mStepAdapter);


            Recipe recipe = intent.getParcelableExtra(RECIPE_EXTRA);
            mRecipeName = recipe.getName();
            if( mRecipeName != null){
                // Set the name of the Recipe as title of Action bar
                setTitle(mRecipeName);

            }
            // Get the recipeId from the recipe and use to query database and obtain the ingredients and steps
            setUpIngredients(recipe.getId());
            setUpSteps(recipe.getId());

            if(savedInstanceState != null){
                mStepIndex = savedInstanceState.getInt(STEP_INDEX_EXTRA);
            }else{
                mStepIndex = 0;
            }

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
                    fragment.setStepIndex(mStepIndex);
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

                mTvIngredients.setText(RecipeUtil.assembleIngredients(ingredients));

            }
        });
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_INDEX_EXTRA,mStepIndex);
        super.onSaveInstanceState(outState);
    }


    @Override
    public void onStepClicked(int stepIndex) {
        if (mTabletMode){
            mStepIndex = stepIndex;
            updateStepFragment();
            
        }else{
            Intent stepIntent = new Intent(RecipeActivity.this,StepActivity.class);
            stepIntent.putExtra(STEP_INDEX_EXTRA,stepIndex);
            stepIntent.putExtra(RECIPE_NAME_EXTRA,mRecipeName);
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
        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container,fragment)
                .commit();
    }
}
