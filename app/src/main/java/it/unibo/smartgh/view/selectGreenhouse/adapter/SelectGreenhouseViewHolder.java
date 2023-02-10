package it.unibo.smartgh.view.selectGreenhouse.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;
import it.unibo.smartgh.view.recyclerview.OnItemListener;

public class SelectGreenhouseViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener{

    private final TextView greenhouseName;
    private final OnItemListener itemListener;

    public SelectGreenhouseViewHolder(@NonNull View itemView, OnItemListener itemListener) {
        super(itemView);
        this.greenhouseName = itemView.findViewById(R.id.greenhouseName);
        this.itemListener = itemListener;

        itemView.setOnClickListener(this);
    }

    public TextView getGreenhouseName(){ return this.greenhouseName;}

    @Override
    public void onClick(View view) {
        this.itemListener.onItemClick(getAdapterPosition());
    }
}