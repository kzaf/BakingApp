package com.zaf.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import com.zaf.bakingapp.fragments.IngredientsAndStepsFragment;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Ingredients;

import java.util.ArrayList;

public class DetailsScreenActivity extends AppCompatActivity {

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        if(savedInstanceState == null){
            Intent intent = getIntent();
            Cake selectedCake = intent.getParcelableExtra("Cake");

            // If the video fragment is not null, then the app is running on tablet
            if(findViewById(R.id.videos_fragment) != null){
                isTablet = true;


            }else{
                isTablet = false;

                ArrayList<Ingredients> ingredientsArrayList = new ArrayList<>(selectedCake.getIngredients());
                getIntent().putExtra("ingredients", ingredientsArrayList);

                FragmentManager fragmentManager = getSupportFragmentManager();
                IngredientsAndStepsFragment ingredientsAndStepsFragment= new IngredientsAndStepsFragment();
                fragmentManager.beginTransaction().replace(R.id.ingredients_fragment, ingredientsAndStepsFragment).commit();
            }
        }
    }
}
