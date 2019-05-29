package com.zaf.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaf.bakingapp.R;
import com.zaf.bakingapp.models.Ingredients;

import java.util.List;

public class IngredientsAdapter extends RecyclerView.Adapter<IngredientsAdapter.IngredientsViewHolder> {

    private List<Ingredients> ingredientsList;

    public IngredientsAdapter(List<Ingredients> ingredientsList) {
        this.ingredientsList = ingredientsList;
    }

    @NonNull
    @Override
    public IngredientsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.ingredients_item, viewGroup, false);
        return new IngredientsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull IngredientsViewHolder ingredientsViewHolder, int position) {
        ingredientsViewHolder.mIngredientQuantity.setText(ingredientsList.get(position).getQuantity());
        ingredientsViewHolder.mIngredientMeasure.setText(ingredientsList.get(position).getMeasure());
        ingredientsViewHolder.mIngredientType.setText(ingredientsList.get(position).getIngredient());
    }

    @Override
    public int getItemCount() {
        if (null == ingredientsList) return 0;
        return ingredientsList.size();
    }

    public class IngredientsViewHolder extends RecyclerView.ViewHolder{

        private final TextView mIngredientQuantity;
        private final TextView mIngredientMeasure;
        private final TextView mIngredientType;

        private IngredientsViewHolder(View itemView) {
            super(itemView);

            mIngredientQuantity = itemView.findViewById(R.id.ingredient_quantity);
            mIngredientMeasure = itemView.findViewById(R.id.ingredient_measure);
            mIngredientType = itemView.findViewById(R.id.ingredient_type);
        }

    }
}


