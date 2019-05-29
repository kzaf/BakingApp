package com.zaf.bakingapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.zaf.bakingapp.DetailsScreenActivity;
import com.zaf.bakingapp.R;
import com.zaf.bakingapp.VideoActivity;
import com.zaf.bakingapp.adapters.IngredientsAdapter;
import com.zaf.bakingapp.adapters.StepsAdapter;
import com.zaf.bakingapp.models.Ingredients;
import com.zaf.bakingapp.models.Steps;

import java.util.ArrayList;

public class IngredientsAndStepsFragment extends Fragment implements StepsAdapter.StepsAdapterListItemClickListener {

    RecyclerView recyclerViewIngredients;
    RecyclerView recyclerViewSteps;

    ArrayList<Ingredients> ingredientsArrayList;
    ArrayList<Steps> stepsArrayList;

    public IngredientsAndStepsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        recyclerViewIngredients = rootView.findViewById(R.id.ingredients_recycler_view);
        recyclerViewSteps = rootView.findViewById(R.id.steps_recycler_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        populateRecyclerViews();
    }

    @Override
    public void onListItemClick(int item) {
        Toast.makeText(getContext(), "Item " + item + " clicked", Toast.LENGTH_SHORT).show();

        Intent intent = new Intent(getActivity(), VideoActivity.class);
        intent.putExtra("Step", stepsArrayList.get(item));

        startActivity(intent);
    }

    private void populateRecyclerViews() {
        Intent intent = getActivity().getIntent();
        ingredientsArrayList = intent.getParcelableArrayListExtra("ingredients");
        stepsArrayList = intent.getParcelableArrayListExtra("steps");

        recyclerViewIngredients.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewIngredients.setAdapter(new IngredientsAdapter(ingredientsArrayList));

        recyclerViewSteps.setLayoutManager(new LinearLayoutManager(getActivity()));
        recyclerViewSteps.setAdapter(new StepsAdapter(this, stepsArrayList));
    }
}
