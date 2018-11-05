package com.example.android.miriamsbaking.activity;

import android.content.Intent;
import android.os.Parcelable;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.util.Log;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.fragment.StepFragment;
import com.example.android.miriamsbaking.model.Step;

import java.util.List;

import static com.example.android.miriamsbaking.activity.RecipeActivity.STEPS_EXTRA;
import static com.example.android.miriamsbaking.activity.RecipeActivity.STEP_INDEX_EXTRA;

public class StepActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_step);

        StepFragment stepFragment = new StepFragment();

        Intent intent = getIntent();

        if(intent.hasExtra(STEP_INDEX_EXTRA)){

            int stepIndex = intent.getIntExtra(STEP_INDEX_EXTRA,0);
            Parcelable[] steps = intent.getParcelableArrayExtra(STEPS_EXTRA);

            Log.d("Steps", String.valueOf(steps.length));
            stepFragment.setStepIndex(stepIndex);
            stepFragment.setSteps(steps);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.step_container,stepFragment)
                    .commit();
        }

        }
}
