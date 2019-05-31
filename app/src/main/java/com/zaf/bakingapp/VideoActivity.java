package com.zaf.bakingapp;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getIntent().putExtra("StepsArray", getIntent().getParcelableArrayListExtra("StepsArray"));
        getIntent().putExtra("StepNumber", getIntent().getStringExtra("StepNumber"));


    }
}
