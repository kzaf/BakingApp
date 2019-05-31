package com.zaf.bakingapp.fragments;

import android.app.Dialog;
import android.content.Intent;
import android.content.res.Configuration;
import android.graphics.BitmapFactory;
import android.net.Uri;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
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
import com.google.android.exoplayer2.ui.AspectRatioFrameLayout;
import com.google.android.exoplayer2.ui.SimpleExoPlayerView;
import com.google.android.exoplayer2.upstream.DefaultDataSourceFactory;
import com.google.android.exoplayer2.util.Util;
import com.zaf.bakingapp.R;
import com.zaf.bakingapp.VideoActivity;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class VideoFragment extends Fragment implements ExoPlayer.EventListener {

    private SimpleExoPlayer mExoPlayer;
    private ArrayList<Steps> mStepsArray;
    private int mCurrentStep;
    private int allSteps;
    private boolean isLandscape;
    private boolean mIsLargeScreen;

    public VideoFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        final SimpleExoPlayerView PlayerView = rootView.findViewById(R.id.playerView);
        PlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video_poster));

        final TextView ShortDescription = rootView.findViewById(R.id.video_short_description);
        final TextView Description = rootView.findViewById(R.id.video_description);
        final TextView StepNumber = rootView.findViewById(R.id.step_number);

        final Button NextButton = rootView.findViewById(R.id.next_button);
        final Button PreviousButton = rootView.findViewById(R.id.previous_button);

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

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
        stepNumber.setText(selectedStep.getId() + "/" + allSteps);
        return selectedStep;
    }

    private void initializeButtons(Button nextButton, Button previousButton) {
        if(!mIsLargeScreen){
            nextButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    nextButton();
                }
            });

            previousButton.setOnClickListener(new View.OnClickListener(){
                @Override
                public void onClick(View v) {
                    previousButton();
                }
            });
        }else{
            nextButton.setVisibility(View.INVISIBLE);
            previousButton.setVisibility(View.INVISIBLE);
        }

    }

    private void initializePlayer(SimpleExoPlayerView playerView, Steps selectedStep) {
        if (Uri.parse(selectedStep.getVideoURL()) == null){
            Toast.makeText(getContext(), "This step has no video", Toast.LENGTH_SHORT).show();
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

        if (isLandscape){
            playerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
        }
    }

    public void callVideoFragment(ArrayList<Steps> stepsArray, int currentStep, boolean isLargeScreen) {

        mStepsArray = stepsArray;
        mCurrentStep = currentStep;
        mIsLargeScreen = isLargeScreen;

    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    public void nextButton(){
        if (mCurrentStep == allSteps){
            Toast.makeText(getContext(), "This is the last step!", Toast.LENGTH_SHORT).show();
        }else{
            mExoPlayer.stop();
            Intent nextStepIntent = new Intent(getActivity(), VideoActivity.class);
            nextStepIntent.putExtra("StepNumber", String.valueOf(++mCurrentStep));
            nextStepIntent.putExtra("StepsArray", mStepsArray);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(nextStepIntent);
            getActivity().overridePendingTransition(0, 0);
        }
    }

    public void previousButton(){
        if (mCurrentStep == 0){
            Toast.makeText(getContext(), "This is the first step!", Toast.LENGTH_SHORT).show();
        }else{
            mExoPlayer.stop();
            Intent nextStepIntent = new Intent(getActivity(), VideoActivity.class);
            nextStepIntent.putExtra("StepNumber", String.valueOf(--mCurrentStep));
            nextStepIntent.putExtra("StepsArray", mStepsArray);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(nextStepIntent);
            getActivity().overridePendingTransition(0, 0);
        }
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
        if ((playbackState == ExoPlayer.STATE_READY) && playWhenReady){
            Log.d("LOG", "onPlayerStateChanged: play");
        }else if ((playbackState == ExoPlayer.STATE_READY) && !playWhenReady){
            Log.d("LOG", "onPlayerStateChanged: paused");
        }
    }

    @Override
    public void onPlayerError(ExoPlaybackException error) {
        Toast.makeText(getContext(), "There was an error loading the video, please try again", Toast.LENGTH_SHORT).show();
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
}
