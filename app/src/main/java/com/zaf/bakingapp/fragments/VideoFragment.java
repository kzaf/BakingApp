package com.zaf.bakingapp.fragments;

import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.constraint.Guideline;
import android.support.v4.app.Fragment;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.CardView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.exoplayer2.DefaultLoadControl;
import com.google.android.exoplayer2.ExoPlaybackException;
import com.google.android.exoplayer2.ExoPlayer;
import com.google.android.exoplayer2.ExoPlayerFactory;
import com.google.android.exoplayer2.LoadControl;
import com.google.android.exoplayer2.SimpleExoPlayer;
import com.google.android.exoplayer2.Timeline;
import com.google.android.exoplayer2.extractor.DefaultExtractorsFactory;
import com.google.android.exoplayer2.source.ExtractorMediaSource;
import com.google.android.exoplayer2.source.MediaSource;
import com.google.android.exoplayer2.source.TrackGroupArray;
import com.google.android.exoplayer2.trackselection.DefaultTrackSelector;
import com.google.android.exoplayer2.trackselection.TrackSelectionArray;
import com.google.android.exoplayer2.trackselection.TrackSelector;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zaf.bakingapp.R;
import com.zaf.bakingapp.VideoActivity;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class VideoFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Steps> mStepsArray;
    private int mCurrentStep;
    private int allSteps;
    private boolean mIsLargeScreen;
    @BindView(R.id.playerView)
    SimpleExoPlayerView PlayerView;
    @BindView(R.id.next_button)
    Button NextButton;
    @BindView(R.id.previous_button)
    Button PreviousButton;
    @BindView(R.id.video_short_description)
    TextView ShortDescription;
    @BindView(R.id.video_description)
    TextView Description;
    @BindView(R.id.step_number)
    TextView StepNumber;
    @BindView(R.id.cardView)
    CardView CardView;
    @BindView(R.id.horizontalHalf)
    Guideline HorizontalHalf;

    public VideoFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        if (savedInstanceState != null){
            mStepsArray = savedInstanceState.getParcelableArrayList("steps_array");
            mCurrentStep = savedInstanceState.getInt("current_step");
        }

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);
        ButterKnife.bind(this, rootView);

        PlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video_poster));

        if(mStepsArray != null){
            Steps selectedStep = initializeFields(ShortDescription, Description, StepNumber);

            initializeButtons(NextButton, PreviousButton);

            if (mExoPlayer == null) {
                initializePlayer(PlayerView, selectedStep);
            }

        }

        return rootView;
    }

    private Steps initializeFields(TextView shortDescription, TextView description, TextView stepNumber) {
        allSteps = mStepsArray.size() - 1;

        Steps selectedStep = mStepsArray.get(mCurrentStep);

        shortDescription.setText(selectedStep.getShortDescription());
        description.setText(selectedStep.getDescription());
        stepNumber.setText(new StringBuilder().append(selectedStep.getId()).append("/").append(allSteps).toString());
        return selectedStep;
    }

    private void initializeButtons(Button nextButton, Button previousButton) {
        if(!mIsLargeScreen){
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    buttonClick(true);
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    buttonClick(false);
                }
            });
        }else{
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.INVISIBLE);
        }

    }

    private void initializePlayer(SimpleExoPlayerView playerView, Steps selectedStep) {
        if (Uri.parse(selectedStep.getVideoURL()) == null){
            Toast.makeText(getContext(), getContext().getString(R.string.toast_no_video_step), Toast.LENGTH_SHORT).show();
        }

        TrackSelector trackSelector = new DefaultTrackSelector();
        LoadControl loadControl = new DefaultLoadControl();

        mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
        playerView.setPlayer(mExoPlayer);

        String userAgent = Util.getUserAgent(getContext(), "BakingVideo");

        MediaSource mediaSource = new ExtractorMediaSource(
                Uri.parse(selectedStep.getVideoURL()),
                new DefaultDataSourceFactory(getContext(), userAgent),
                new DefaultExtractorsFactory(),
                null,
                null);

        mExoPlayer.prepare(mediaSource);
        mExoPlayer.setPlayWhenReady(true);
    }

    public void callVideoFragment(ArrayList<Steps> stepsArray, int currentStep, boolean isLargeScreen) {

        mStepsArray = stepsArray;
        mCurrentStep = currentStep;
        mIsLargeScreen = isLargeScreen;

    }

    public void buttonClick(boolean isNextButton){
        int step;
        if(isNextButton){
            if (mCurrentStep == allSteps){
                Toast.makeText(getContext(), getContext().getString(R.string.toast_last_step), Toast.LENGTH_SHORT).show();
                return;
            }else{
               step = ++mCurrentStep;
            }
        }else{
            if (mCurrentStep == 0){
                Toast.makeText(getContext(), getContext().getString(R.string.toast_first_step), Toast.LENGTH_SHORT).show();
                return;
            }else{
                step = --mCurrentStep;
            }
        }
        mExoPlayer.stop();
        Intent nextStepIntent = new Intent(getActivity(), VideoActivity.class);
        nextStepIntent.putExtra("StepNumber", String.valueOf(step));
        nextStepIntent.putExtra("StepsArray", mStepsArray);
        getActivity().finish();
        getActivity().overridePendingTransition(0, 0);
        startActivity(nextStepIntent);
        getActivity().overridePendingTransition(0, 0);
    }

    @Override
    public void onConfigurationChanged(Configuration newConfig) {
        super.onConfigurationChanged(newConfig);

        // This method is used to show video in fullscreen when device is rotated

        ConstraintLayout.LayoutParams params = (ConstraintLayout.LayoutParams) PlayerView.getLayoutParams();
        int visibility = View.VISIBLE;

        if (newConfig.orientation == Configuration.ORIENTATION_LANDSCAPE) {
            visibility = View.GONE;
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height= ViewGroup.LayoutParams.MATCH_PARENT;
            PlayerView.setLayoutParams(params);
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().hide();
            }
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_FULLSCREEN | View.SYSTEM_UI_FLAG_IMMERSIVE);

        } else if (newConfig.orientation == Configuration.ORIENTATION_PORTRAIT){
            params.width= ViewGroup.LayoutParams.MATCH_PARENT;
            params.height=600;
            PlayerView.setLayoutParams(params);
            if(((AppCompatActivity)getActivity()).getSupportActionBar()!=null) {
                ((AppCompatActivity)getActivity()).getSupportActionBar().show();
            }
            getActivity().getWindow().getDecorView().setSystemUiVisibility(View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN);
        }
        NextButton.setVisibility(visibility);
        PreviousButton.setVisibility(visibility);
        ShortDescription.setVisibility(visibility);
        Description.setVisibility(visibility);
        StepNumber.setVisibility(visibility);
        CardView.setVisibility(visibility);
        HorizontalHalf.setVisibility(visibility);
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }
    @Override
    public void onTimelineChanged(Timeline timeline, Object manifest) {

    }

    @Override
    public void onTracksChanged(TrackGroupArray trackGroups, TrackSelectionArray trackSelections) {

    }

    @Override
    public void onLoadingChanged(boolean isLoading) {

    }

    @Override
    public void onPlayerStateChanged(boolean playWhenReady, int playbackState) {

    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getContext(), getContext().getString(R.string.toast_error_loading_video), Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onPositionDiscontinuity() {

    }

    @Override
    public void onPause() {
        super.onPause();
        if (mExoPlayer != null) {
            mExoPlayer.setPlayWhenReady(false);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
        mExoPlayer.setPlayWhenReady(true);
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        releasePlayer();
    }

    @Override
    public void onSaveInstanceState(Bundle currentState) {
        currentState.putParcelableArrayList("steps_array", mStepsArray);
        currentState.putInt("current_step", mCurrentStep);
    }
}
