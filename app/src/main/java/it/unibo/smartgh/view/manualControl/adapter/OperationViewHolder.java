package it.unibo.smartgh.view.manualControl.adapter;


import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;

/**
 * A class that represents the view holder of operation.
 */
public class OperationViewHolder extends RecyclerView.ViewHolder{

    private final ImageView parameterImage;
    private final TextView parameterName;
    private final TextView currentValue;
    private final TextView optimalRange;
    private final LinearLayout operationsLayout;

    /**
     * Constructor of {@link OperationViewHolder}.
     * @param itemView the item view
     */
    public OperationViewHolder(@NonNull View itemView) {
        super(itemView);
        this.parameterImage = itemView.findViewById(R.id.parameterImage);
        this.parameterName = itemView.findViewById(R.id.parameterName);
        this.currentValue = itemView.findViewById(R.id.currentValue);
        this.optimalRange = itemView.findViewById(R.id.optimalRange);
        this.operationsLayout = itemView.findViewById(R.id.operationsLayout);
    }

    /**
     * Gets the parameter image.
     * @return the parameter ImageView
     */
    public ImageView getParameterImage() {
        return parameterImage;
    }

    /**
     * Gets the parameter name.
     * @return the parameter name TextView
     */
    public TextView getParameterName() {
        return parameterName;
    }

    /**
     * Gets the parameter current value.
     * @return the current value TextView
     */
    public TextView getCurrentValue() {
        return currentValue;
    }

    /**
     * Gets the parameter optimal range.
     * @return the optimal range TextView
     */
    public TextView getOptimalRange() {
        return optimalRange;
    }

    /**
     * Gets the operations layout.
     * @return the operations LinearLayout
     */
    public LinearLayout getOperationsLayout() {
        return operationsLayout;
    }
}
