package com.example.android.miriamsbaking.fragment;

import android.content.Context;
import android.net.Uri;
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
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.PlayerView;
import com.google.android.exoplayer2.upstream.DataSource;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;


import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String CONTENT_POSTION = "content postion";
    private int mStepIndex;
    private Parcelable[] mSteps;
    private StepFragListener stepFragListener;
    private SimpleExoPlayer mExoPlayer;
    private long mContentPositon;

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.simple_exo_player_view)
    PlayerView mPlayerView;

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

           Step step = (Step)mSteps[mStepIndex];
           String description = step.getDescription();

           if(description!= null){
               TextView tvTextDescription = rootView.findViewById(R.id.tv_step_description);
               tvTextDescription.setText(description);
           }

           if(savedInstanceState != null){
               mContentPositon = savedInstanceState.getLong(CONTENT_POSTION);
           }else{
               mContentPositon = 0;
           }

           String videoUrl = ((Step)mSteps[mStepIndex]).getVideoURL();
           if(!(videoUrl.isEmpty()|| videoUrl == null)){
               setUpVideo(Uri.parse(videoUrl));
           }

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

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        if(mExoPlayer != null){
            mContentPositon = mExoPlayer.getContentPosition();
            outState.putLong(CONTENT_POSTION,mContentPositon);
        }


        super.onSaveInstanceState(outState);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(mExoPlayer != null){
            releasePlayer();
        }

    }

    public void setStepIndex(int stepIndex){
        mStepIndex = stepIndex;
    }

    public void setSteps(Parcelable[] steps){
        mSteps = steps;
    }

    public void setUpVideo(Uri videoUri){
        
        if(mExoPlayer != null){
            // Create the player
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext()
                    ,new DefaultTrackSelector(),new DefaultLoadControl());
            // Bind player to view
            mPlayerView.setPlayer(mExoPlayer);

            // Produce DataSource instances through which media data is loaded.
            DataSource.Factory dataSourceFactory = new DefaultDataSourceFactory(getContext(),
                    Util.getUserAgent(getContext(), getString(R.string.app_name)));

            MediaSource mediaSource = new ExtractorMediaSource.Factory(dataSourceFactory)
                    .createMediaSource(videoUri);
            // Set the position so that any previous state can be resumed
            mExoPlayer.seekTo(mContentPositon);
            //mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);
        }


    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }



}
