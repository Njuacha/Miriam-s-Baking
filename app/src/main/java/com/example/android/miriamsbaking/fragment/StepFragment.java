package com.example.android.miriamsbaking.fragment;

import android.content.Context;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.model.Step;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private int mStepIndex;
    private Parcelable[] mSteps;
    private StepFragListener stepFragListener;

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_previous)
    Button btnPrevious;

    public interface StepFragListener{
        void onNextBtnClicked();
        void onPrevBtnClicked();
    }

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try{
            stepFragListener = (StepFragListener) context;
        }catch (ClassCastException e){
            throw new ClassCastException(e + " "
                    + context.toString() + " must implement StepFragListener");
        }

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       View rootView = inflater.inflate(R.layout.step_fragment,container,false);
        ButterKnife.bind(this,rootView);

       if (mSteps != null){
           TextView tvTextDescription = rootView.findViewById(R.id.tv_step_description);

           tvTextDescription.setText(((Step)mSteps[mStepIndex]).getDescription());
       }

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepFragListener.onNextBtnClicked();
            }
        });

        btnPrevious.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                stepFragListener.onPrevBtnClicked();
            }
        });

        return rootView;
    }

    public void setStepIndex(int stepIndex){
        mStepIndex = stepIndex;
    }

    public void setSteps(Parcelable[] steps){
        mSteps = steps;
    }



}
