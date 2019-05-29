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

import com.zaf.bakingapp.R;
import com.zaf.bakingapp.adapters.IngredientsAdapter;
import com.zaf.bakingapp.models.Ingredients;

import java.util.ArrayList;

public class IngredientsAndStepsFragment extends Fragment {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;
    ArrayList<Ingredients> ingredientsArrayList;

    public IngredientsAndStepsFragment() { }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {

        View rootView = inflater.inflate(R.layout.fragment_ingredients_and_steps, container, false);

        recyclerView = rootView.findViewById(R.id.ingredients_recycler_view);

        return rootView;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        Intent intent = getActivity().getIntent();
        ingredientsArrayList = intent.getParcelableArrayListExtra("ingredients");

        layoutManager = new LinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        recyclerView.setAdapter(new IngredientsAdapter(ingredientsArrayList));
    }
}
