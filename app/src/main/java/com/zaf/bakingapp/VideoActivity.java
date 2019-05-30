package com.zaf.bakingapp;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class VideoActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_video);

        getIntent().putExtra("StepsArray", getIntent().getParcelableArrayListExtra("StepsArray"));
        getIntent().putExtra("StepNumber", getIntent().getStringExtra("StepNumber"));
    }
}
