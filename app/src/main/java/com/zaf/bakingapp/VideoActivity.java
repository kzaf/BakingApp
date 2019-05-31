package com.zaf.bakingapp;

import android.os.Bundle;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;

import com.zaf.bakingapp.fragments.VideoFragment;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    private ArrayList<Steps> stepsArrayList;
    private int stepNumber;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        stepsArrayList = getIntent().getParcelableArrayListExtra("StepsArray");
        stepNumber = Integer.parseInt(getIntent().getStringExtra("StepNumber"));

        VideoFragment videoFragment = new VideoFragment();

        videoFragment.callVideoFragment(stepsArrayList, stepNumber, false);

        FragmentManager fragmentManager = getSupportFragmentManager();

        fragmentManager.beginTransaction()
                .add(R.id.activity_video_frame, videoFragment)
                .commit();
    }
}
