package com.example.android.miriamsbaking.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.fragment.StepFragment;
import com.example.android.miriamsbaking.model.Step;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

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

            stepFragment.setStepIndex(mStepIndex);
            stepFragment.setSteps(mSteps);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_container, stepFragment)
                    .commit();

        } else return;


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
