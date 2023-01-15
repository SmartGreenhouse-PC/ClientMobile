package it.unibo.smartgh.view.manualControl.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;

public class OperationViewHolder extends RecyclerView.ViewHolder{

    private final ImageView parameterImage;
    private final TextView parameterName;
    private final TextView currentValue;
    private final TextView optimalRange;
    private final LinearLayout operationsLayout;

    public OperationViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parameterImage = itemView.findViewById(R.id.parameterImage);
        this.parameterName = itemView.findViewById(R.id.parameterName);
        this.currentValue = itemView.findViewById(R.id.currentValue);
        this.optimalRange = itemView.findViewById(R.id.optimalRange);
        this.operationsLayout = itemView.findViewById(R.id.operationsLayout);
    }

    public ImageView getParameterImage() {
        return parameterImage;
    }

    public TextView getParameterName() {
        return parameterName;
    }

    public TextView getCurrentValue() {
        return currentValue;
    }

    public TextView getOptimalRange() {
        return optimalRange;
    }

    public LinearLayout getOperationsLayout() {
        return operationsLayout;
    }
}
