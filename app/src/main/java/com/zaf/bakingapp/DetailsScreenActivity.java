package com.zaf.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zaf.bakingapp.fragments.IngredientsAndStepsFragment;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Ingredients;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class DetailsScreenActivity extends AppCompatActivity {

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        Intent intent = getIntent();
        Cake selectedCake = intent.getParcelableExtra("Cake");

        ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>(selectedCake.getIngredients());
        getIntent().putExtra("ingredients", ingredientsArrayList);

        ArrayList<Steps> stepsArrayList = new ArrayList<>(selectedCake.getSteps());
        getIntent().putExtra("steps", stepsArrayList);

        FragmentManager fragmentManager = getSupportFragmentManager();
        IngredientsAndStepsFragment ingredientsAndStepsFragment= new IngredientsAndStepsFragment();
    }
}
