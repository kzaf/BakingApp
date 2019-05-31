package com.zaf.bakingapp;

import android.content.Intent;
import android.support.v4.app.FragmentManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.zaf.bakingapp.adapters.IngredientsAdapter;
import com.zaf.bakingapp.adapters.StepsAdapter;
import com.zaf.bakingapp.fragments.VideoFragment;
import com.zaf.bakingapp.models.Cake;
import com.zaf.bakingapp.models.Ingredients;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class DetailsScreenActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterListItemClickListener{

    private ArrayList<Ingredients> ingredientsArrayList;
    private ArrayList<Steps> stepsArrayList;
    private RecyclerView recyclerViewIngredients;
    private RecyclerView recyclerViewSteps;
    private TextView mShortDescription;
    private TextView mDescription;
    private TextView mStepNumber;
    private Button nextButton;
    private Button previousButton;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        recyclerViewIngredients = findViewById(R.id.ingredients_recycler_view);
        recyclerViewSteps = findViewById(R.id.steps_recycler_view);

        Intent intent = getIntent();
        Cake selectedCake = intent.getParcelableExtra("Cake");

        ingredientsArrayList = new ArrayList<>(selectedCake.getIngredients());
        stepsArrayList = new ArrayList<>(selectedCake.getSteps());

        if (findViewById(R.id.videos_fragment_layout) == null){ // Phone

            isTablet = false;

            populateRecyclerViews();

        }else{ // Tablet

            isTablet = true;

            populateRecyclerViews();
            populateVideoFragment(stepsArrayList, String.valueOf(stepsArrayList.get(0).getId()));

        }
    }

    private void populateVideoFragment(ArrayList<Steps> stepsArrayList, String stepIndex) {

        hideButtonsWhenLargeScreen();

        getIntent().putExtra("StepsArray", stepsArrayList);
        getIntent().putExtra("StepNumber", stepIndex);

        mShortDescription = findViewById(R.id.video_short_description);
        mDescription = findViewById(R.id.video_description);
        mStepNumber = findViewById(R.id.step_number);

        Steps selectedStep = stepsArrayList.get(stepsArrayList.get(Integer.parseInt(stepIndex)).getId());

        mShortDescription.setText(selectedStep.getShortDescription());
        mDescription.setText(selectedStep.getDescription());
        mStepNumber.setText(selectedStep.getId() + "/" + (stepsArrayList.size() - 1));

    }

    private void populateRecyclerViews() {
        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredients.setAdapter(new IngredientsAdapter(ingredientsArrayList));

        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSteps.setAdapter(new StepsAdapter(this, stepsArrayList));
    }

    private void hideButtonsWhenLargeScreen() {
        nextButton = findViewById(R.id.next_button);
        previousButton = findViewById(R.id.previous_button);

        nextButton.setVisibility(View.INVISIBLE);
        previousButton.setVisibility(View.INVISIBLE);
    }

    @Override
    public void onListItemClick(int item) {
        if(!isTablet){// Phone

            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("StepsArray", stepsArrayList);
            intent.putExtra("StepNumber", String.valueOf(item));

            startActivity(intent);

        }else{// Tablet

            populateVideoFragment(stepsArrayList, String.valueOf(item));

            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.videos_fragment_layout, new VideoFragment())
                    .commit();
        }

    }
}
