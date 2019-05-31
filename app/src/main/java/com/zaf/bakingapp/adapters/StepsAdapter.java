package com.zaf.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaf.bakingapp.R;
import com.zaf.bakingapp.models.Steps;

import java.util.List;

public class StepsAdapter extends RecyclerView.Adapter<StepsAdapter.StepsViewHolder>  {

    final private StepsAdapterListItemClickListener mOnClickListener;
    private List<Steps> stepsList;

    public StepsAdapter(StepsAdapterListItemClickListener mOnClickListener, List<Steps> stepsList) {
        this.mOnClickListener = mOnClickListener;
        this.stepsList = stepsList;
    }

    @NonNull
    @Override
    public StepsViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.steps_item, viewGroup, false);
        return new StepsAdapter.StepsViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull StepsViewHolder stepsViewHolder, int position) {
        stepsViewHolder.mStepNumber.setText(String.valueOf(stepsList.get(position).getId()));
        stepsViewHolder.mStepDescriptionShort.setText(stepsList.get(position).getShortDescription());
        if(stepsList.get(position).getVideoURL().equals("")){
            stepsViewHolder.mStepHasVideo.setVisibility(View.INVISIBLE);
        }
    }

    @Override
    public int getItemCount() {
        if (null == stepsList) return 0;
        return stepsList.size();
    }

    public interface StepsAdapterListItemClickListener {
        void onListItemClick(int item);
    }

    public class StepsViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        private final TextView mStepNumber;
        private final TextView mStepDescriptionShort;
        private final ImageView mStepHasVideo;


        private StepsViewHolder(View itemView) {
            super(itemView);

            mStepNumber = itemView.findViewById(R.id.step_number);
            mStepDescriptionShort = itemView.findViewById(R.id.step_descrpition_short);
            mStepHasVideo = itemView.findViewById(R.id.arrow_image_step);

            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(adapterPosition);
        }
    }
}
