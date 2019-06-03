package com.zaf.bakingapp.adapters;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.zaf.bakingapp.R;
import com.zaf.bakingapp.models.Cake;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class CakesAdapter extends RecyclerView.Adapter<CakesAdapter.CakesViewHolder>  {

    final private CakesAdapterListItemClickListener mOnClickListener;
    private List<Cake> cakeList;

    public CakesAdapter(CakesAdapterListItemClickListener mOnClickListener, List<Cake> cakeList) {
        this.mOnClickListener = mOnClickListener;
        this.cakeList = cakeList;
    }

    @NonNull
    @Override
    public CakesViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cake_item, viewGroup, false);
        return new CakesViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull CakesViewHolder cakesViewHolder, int position) {

        cakesViewHolder.mCakeName.setText(cakeList.get(position).getName());

        switch (cakeList.get(position).getName()){
            case "Nutella Pie":
                cakesViewHolder.mCakePoster.setImageResource(R.drawable.nutellapie_poster);
                break;
            case "Brownies":
                cakesViewHolder.mCakePoster.setImageResource(R.drawable.brownies_poster);
                break;
            case "Yellow Cake":
                cakesViewHolder.mCakePoster.setImageResource(R.drawable.yellowcake_poster);
                break;
            default:
                cakesViewHolder.mCakePoster.setImageResource(R.drawable.cheesecake_poster);
        }
    }

    @Override
    public int getItemCount() {
        if (null == cakeList) return 0;
        return cakeList.size();
    }

    public interface CakesAdapterListItemClickListener {
        void onListItemClick(int item);
    }

    public class CakesViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

        @BindView(R.id.cakeCardImage)
        ImageView mCakePoster;
        @BindView(R.id.cakeCardName)
        TextView mCakeName;

        private CakesViewHolder(View itemView) {
            super(itemView);

            ButterKnife.bind(this, itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View v) {
            int adapterPosition = getAdapterPosition();
            mOnClickListener.onListItemClick(adapterPosition);
        }
    }



}
