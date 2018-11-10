package com.example.android.miriamsbaking.activity;

import android.content.Intent;
import android.content.res.Configuration;
import android.os.Parcelable;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.fragment.StepFragment;

import static com.example.android.miriamsbaking.activity.RecipeActivity.RECIPE_NAME_EXTRA;
import static com.example.android.miriamsbaking.activity.RecipeActivity.STEPS_EXTRA;
import static com.example.android.miriamsbaking.activity.RecipeActivity.STEP_INDEX_EXTRA;

public class StepActivity extends AppCompatActivity implements StepFragment.StepFragListener {

    private int mStepIndex;
    private int mNumberOfSteps;
    private Parcelable[] mSteps;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        ActionBar actionBar = this.getSupportActionBar();
        if(actionBar != null){
            actionBar.setDisplayHomeAsUpEnabled(true);
        }

        StepFragment stepFragment = new StepFragment();

        Intent intent = getIntent();


        if (intent.hasExtra(STEP_INDEX_EXTRA)) {


            mSteps = intent.getParcelableArrayExtra(STEPS_EXTRA);
            mNumberOfSteps = mSteps.length;

            if (savedInstanceState != null){
                mStepIndex = savedInstanceState.getInt(STEP_INDEX_EXTRA);
            }else{
                mStepIndex = intent.getIntExtra(STEP_INDEX_EXTRA, 0);
            }

            // Set title to Name of Recipe so you are also reminded
            setTitle(intent.getStringExtra(RECIPE_NAME_EXTRA) + " Steps");

            stepFragment.setStepIndex(mStepIndex);
            stepFragment.setSteps(mSteps);

            // If in landscape mode set the fullscreen to true in fragment
            if ( getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE){
                stepFragment.setFullScreen(true);
            }

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.step_container, stepFragment)
                    .commit();


        }


    }

    @Override
    public void onNextBtnClicked() {
        mStepIndex += 1;
        mStepIndex %= mNumberOfSteps;
        updateFragment();
    }

    @Override
    public void onPrevBtnClicked() {
        mStepIndex -= 1;
        if (mStepIndex < 0) {
            mStepIndex = mNumberOfSteps - 1;
        }
        updateFragment();
    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {
        outState.putInt(STEP_INDEX_EXTRA,mStepIndex);
        super.onSaveInstanceState(outState);
    }


    public void updateFragment() {
        StepFragment fragment = new StepFragment();
        fragment.setStepIndex(mStepIndex);
        fragment.setSteps(mSteps);

        getSupportFragmentManager().beginTransaction()
                .replace(R.id.step_container, fragment)
                .commit();
    }
}
