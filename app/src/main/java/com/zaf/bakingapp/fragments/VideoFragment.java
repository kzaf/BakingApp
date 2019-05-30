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
    private SimpleExoPlayerView mPlayerView;
    private TextView mShortDescription;
    private TextView mDescription;
    private TextView mStepNumber;
    private Dialog mFullScreenDialog;
    private Button nextButton;
    private Button previousButton;
    private ArrayList<Steps> stepsArray;
    private int currentStep;
    private int allSteps;
    private boolean isLandscape;

    public VideoFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_video, container, false);

        mPlayerView = rootView.findViewById(R.id.playerView);
        mPlayerView.setDefaultArtwork(BitmapFactory.decodeResource(getResources(), R.drawable.no_video_poster));

        mShortDescription = rootView.findViewById(R.id.video_short_description);
        mDescription = rootView.findViewById(R.id.video_description);
        mStepNumber = rootView.findViewById(R.id.step_number);

        nextButton = rootView.findViewById(R.id.next_button);
        previousButton = rootView.findViewById(R.id.previous_button);

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

        isLandscape = getResources().getConfiguration().orientation == Configuration.ORIENTATION_LANDSCAPE;

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        stepsArray = intent.getParcelableArrayListExtra("StepsArray");
        currentStep = Integer.parseInt(intent.getStringExtra("StepNumber"));

        initializeFields(stepsArray, currentStep);
    }

    public void initializeFields(ArrayList<Steps> stepsArray, int currentStep) {

        allSteps = stepsArray.size() - 1;

        Steps selectedStep = stepsArray.get(currentStep);

        mShortDescription.setText(selectedStep.getShortDescription());
        mDescription.setText(selectedStep.getDescription());
        mStepNumber.setText(selectedStep.getId() + "/" + allSteps);

        initializePlayer(Uri.parse(selectedStep.getVideoURL()));
    }

    private void initializePlayer(Uri mediaUri) {
        if (mExoPlayer == null) {

            if (mediaUri == null){
                Toast.makeText(getContext(), "This step has no video", Toast.LENGTH_SHORT).show();
            }

            TrackSelector trackSelector = new DefaultTrackSelector();
            LoadControl loadControl = new DefaultLoadControl();

            mExoPlayer = ExoPlayerFactory.newSimpleInstance(getContext(), trackSelector, loadControl);
            mPlayerView.setPlayer(mExoPlayer);

            String userAgent = Util.getUserAgent(getContext(), "BakingVideo");

            MediaSource mediaSource = new ExtractorMediaSource(
                    mediaUri,
                    new DefaultDataSourceFactory(getContext(), userAgent),
                    new DefaultExtractorsFactory(),
                    null,
                    null);

            mExoPlayer.prepare(mediaSource);
            mExoPlayer.setPlayWhenReady(true);

            if (isLandscape){
                mPlayerView.setResizeMode(AspectRatioFrameLayout.RESIZE_MODE_FILL);
            }
        }
    }

    private void releasePlayer() {
        mExoPlayer.stop();
        mExoPlayer.release();
        mExoPlayer = null;
    }

    private void setPlayerFullHeight() {
        DisplayMetrics displayMetrics = new DisplayMetrics();
        getActivity().getWindowManager().getDefaultDisplay().getMetrics(displayMetrics);
        int height = displayMetrics.heightPixels;
        mPlayerView.setLayoutParams(new LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, height));
    }

    public void nextButton(){
        if (currentStep == allSteps){
            Toast.makeText(getContext(), "This is the last step!", Toast.LENGTH_SHORT).show();
        }else{
            mExoPlayer.stop();
            Intent nextStepIntent = new Intent(getActivity(), VideoActivity.class);
            nextStepIntent.putExtra("StepNumber", String.valueOf(++currentStep));
            nextStepIntent.putExtra("StepsArray", stepsArray);
            getActivity().finish();
            getActivity().overridePendingTransition(0, 0);
            startActivity(nextStepIntent);
            getActivity().overridePendingTransition(0, 0);        }
    }

    public void previousButton(){
        if (currentStep == 0){
            Toast.makeText(getContext(), "This is the first step!", Toast.LENGTH_SHORT).show();
        }else{
            mExoPlayer.stop();
            Intent nextStepIntent = new Intent(getActivity(), VideoActivity.class);
            nextStepIntent.putExtra("StepNumber", String.valueOf(--currentStep));
            nextStepIntent.putExtra("StepsArray", stepsArray);
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
