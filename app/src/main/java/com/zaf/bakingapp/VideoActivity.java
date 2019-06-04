package com.zaf.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

import com.zaf.bakingapp.fragments.VideoFragment;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        ArrayList<Steps> stepsArrayList = getIntent().getParcelableArrayListExtra("StepsArray");
        int stepNumber = Integer.parseInt(getIntent().getStringExtra("StepNumber"));

        if (savedInstanceState == null){

            VideoFragment videoFragment = new VideoFragment();
            videoFragment.callVideoFragment(stepsArrayList, stepNumber, false);

            getSupportFragmentManager().beginTransaction()
                    .add(R.id.activity_video_frame, videoFragment)
                    .commit();
        }
    }
}
