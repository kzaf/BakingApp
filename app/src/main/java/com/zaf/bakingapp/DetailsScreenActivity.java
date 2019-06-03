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
import com.zaf.bakingapp.widget.AppWidgetProvider;

import java.util.ArrayList;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailsScreenActivity extends AppCompatActivity implements StepsAdapter.StepsAdapterListItemClickListener{

    private ArrayList<Ingredients> ingredientsArrayList;
    private ArrayList<Steps> stepsArrayList;
    private Cake selectedCake;
    private String cakeName;
    @BindView(R.id.ingredients_recycler_view)
    RecyclerView recyclerViewIngredients;
    @BindView(R.id.steps_recycler_view)
    RecyclerView recyclerViewSteps;

    private boolean isTablet;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_details_screen);

        ButterKnife.bind(this);

        Intent intent = getIntent();
        selectedCake = intent.getParcelableExtra("Cake");

        ingredientsArrayList = new ArrayList<>(selectedCake.getIngredients());
        stepsArrayList = new ArrayList<>(selectedCake.getSteps());

        cakeName = selectedCake.getName();

        populateViews();

        sendRecipeToWidget();

        if (findViewById(R.id.videos_fragment_layout) == null){ // Phone
            isTablet = false;
        }else{ // Tablet
            isTablet = true;
            if (savedInstanceState == null){
                populateVideoFragment(stepsArrayList, String.valueOf(stepsArrayList.get(0).getId()), true);
            }
        }
    }

    private void sendRecipeToWidget() {
        Intent intent = new Intent(this, AppWidgetProvider.class);
        intent.putExtra("WidgetSelectedCake", selectedCake);
        intent.setAction("android.appwidget.action.APPWIDGET_UPDATE");
        sendBroadcast(intent);
    }


    private void populateVideoFragment(ArrayList<Steps> stepsArrayList, String stepIndex, boolean isFirstTime) {

        VideoFragment videoFragment = new VideoFragment();
        videoFragment.callVideoFragment(stepsArrayList, Integer.parseInt(stepIndex), true);

        if(isFirstTime){
            getSupportFragmentManager().beginTransaction()
                    .add(R.id.videos_fragment_layout, videoFragment)
                    .commit();
        }else{
            getSupportFragmentManager().beginTransaction()
                    .replace(R.id.videos_fragment_layout, videoFragment)
                    .commit();
        }

    }

    private void populateViews() {

        setTitle(cakeName);

        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewIngredients.setAdapter(new IngredientsAdapter(ingredientsArrayList));

        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(this));
        recyclerViewSteps.setAdapter(new StepsAdapter(this, stepsArrayList));
    }

    @Override
    public void onListItemClick(int item) {
        if(!isTablet){// Phone

            Intent intent = new Intent(this, VideoActivity.class);
            intent.putExtra("StepsArray", stepsArrayList);
            intent.putExtra("StepNumber", String.valueOf(item));

            startActivity(intent);

        }else{// Tablet

            populateVideoFragment(stepsArrayList, String.valueOf(item), false);

        }

    }
}
