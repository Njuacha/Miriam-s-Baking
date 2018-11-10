package com.example.android.miriamsbaking.fragment;

import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.android.miriamsbaking.R;
import com.example.android.miriamsbaking.model.Step;
import com.google.android.exoplayer2.C;
import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.squareup.picasso.Picasso;


import butterknife.BindView;
import butterknife.ButterKnife;

public class StepFragment extends Fragment {

    private static final String PLAYER_POSITION = "player position" ;
    private static final String PLAY_STATE = "play state";
    private int mStepIndex;
    private Parcelable[] mSteps;
    private StepFragListener stepFragListener;
    private SimpleExoPlayer mExoPlayer;
    private long mPlayerPosition;
    private boolean mPlayWhenReady;

    @BindView(R.id.btn_next)
    Button btnNext;
    @BindView(R.id.btn_previous)
    Button btnPrevious;
    @BindView(R.id.simple_exo_player_view)
    SimpleExoPlayerView mPlayerView;
    @BindView(R.id.tv_step_description)
    TextView tvDescript;
    @BindView(R.id.view_small_padding)
    View vwPadding;
    @BindView(R.id.image_view)
    ImageView imageView;

    private boolean isFullScreen = false;

    public interface StepFragListener {
        void onNextBtnClicked();
        void onPrevBtnClicked();
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

        try {
            stepFragListener = (StepFragListener) context;
        } catch (ClassCastException e) {
            throw new ClassCastException(e + " "
                    + context.toString() + " must implement StepFragListener");
        }


    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View rootView = inflater.inflate(R.layout.step_fragment, container, false);
        ButterKnife.bind(this, rootView);

        if (mSteps != null) {

            Step step = (Step) mSteps[mStepIndex];
            String description = step.getDescription();

            if (description != null) {
                TextView tvTextDescription = rootView.findViewById(R.id.tv_step_description);
                tvTextDescription.setText(description);
            }

            String videoUrl = step.getVideoURL();
            // If there is videoUrl use it else try the thumbnailUrl.
            if ((videoUrl != null) && !videoUrl.isEmpty()) {

                if (isFullScreen) {
                    makeVideoFullScreen();
                }
                // Restore values from any previous state
                if (savedInstanceState != null){
                    
                    mPlayerPosition = savedInstanceState.getLong(PLAYER_POSITION);
                    mPlayWhenReady = savedInstanceState.getBoolean(PLAY_STATE);
                }else{

                    mPlayWhenReady = true;
                    mPlayerPosition = 0;
                }


                setUpVideo(Uri.parse(videoUrl));

            } else  {

                toggleVisibilityToImageView();

                if( !(step.getThumbnailURL().isEmpty()))
                Picasso.with(getContext()).load(step.getThumbnailURL())
                        .placeholder(R.mipmap.ic_launcher)
                        .error(R.mipmap.ic_launcher)
                        .into(imageView);

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
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        Log.d("Values","Position: "+mPlayerPosition +"playState " + mPlayWhenReady);
        outState.putLong(PLAYER_POSITION,mPlayerPosition);
        outState.putBoolean(PLAY_STATE, mPlayWhenReady);
        super.onSaveInstanceState(outState);
    }

    @Override
    public void onPause() {
        super.onPause();
        if( mExoPlayer != null){
            mPlayerPosition = mExoPlayer.getCurrentPosition();
            mPlayWhenReady = mExoPlayer.getPlayWhenReady();
        }
        if (Util.SDK_INT <= 23){
            releasePlayer();
        }
    }

    @Override
    public void onStop() {
        super.onStop();

        if(Util.SDK_INT > 23){
            releasePlayer();
        }
    }

    private void toggleVisibilityToImageView() {
        mPlayerView.setVisibility(View.GONE);
        imageView.setVisibility(View.VISIBLE);
    }

    private void makeVideoFullScreen() {
        btnPrevious.setVisibility(View.GONE);
        btnNext.setVisibility(View.GONE);
        vwPadding.setVisibility(View.GONE);
        tvDescript.setVisibility(View.GONE);
    }

    /**
     * Release ExoPlayer.
     */
    private void releasePlayer() {
        if (mExoPlayer != null){
            mExoPlayer.stop();
            mExoPlayer.release();
            mExoPlayer = null;
        }
    }

    public void setStepIndex(int stepIndex) {
        mStepIndex = stepIndex;
    }

    public void setSteps(Parcelable[] steps) {
        mSteps = steps;
    }

    public void setUpVideo(Uri videoUri) {

        if (mExoPlayer == null) {
            // Create an instance of the ExoPlayer.
            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();
            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);
            // Prepare the MediaSource.
            String userAgent = Util.getUserAgent(getContext(), getString(R.string.app_name));
            MediaSource mediaSource = new ExtractorMediaSource(videoUri, new DefaultDataSourceFactory(
                    getContext(), userAgent), new DefaultExtractorsFactory(), null, null);
            mExoPlayer.prepare(mediaSource);

            mExoPlayer.setPlayWhenReady(mPlayWhenReady);

            if (mPlayerPosition > 0){
                mExoPlayer.seekTo(mPlayerPosition);
            }

        }


    }

    public void setFullScreen(boolean fullScreen) {
        isFullScreen = fullScreen;
    }


}
