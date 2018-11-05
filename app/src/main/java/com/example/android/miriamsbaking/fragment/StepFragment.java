package com.example.android.miriamsbaking.fragment;

import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.model.Step;

import java.util.List;

public class StepFragment extends Fragment {

    private int mStepIndex;
    private Parcelable[] mSteps;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.step_fragment,container,false);


       if (mSteps != null){
           TextView tvTextDescription = rootView.findViewById(R.id.tv_step_description);

           tvTextDescription.setText(((Step)mSteps[mStepIndex]).getDescription());
       }

        return rootView;
    }

    public void setStepIndex(int stepIndex){
        mStepIndex = stepIndex;
    }

    public void setSteps(Parcelable[] steps){
        mSteps = steps;
    }

}
