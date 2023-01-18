package it.unibo.smartgh.view.homepage.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;

/**
 * This class represents the homepage parameter view holder.
 */
public class HomepageParameterViewHolder extends RecyclerView.ViewHolder {

    private final TextView parameterName;
    private final TextView parameterCurrentValue;
    private final TextView parameterOptimalValue;
    private final AppCompatImageView parameterImage;

    /**
     * Constructor of {@link HomepageParameterViewHolder}.
     * @param itemView the view item
     */
    public HomepageParameterViewHolder(@NonNull View itemView) {
        super(itemView);
        parameterName = itemView.findViewById(R.id.homepage_parameter_name);
        parameterCurrentValue = itemView.findViewById(R.id.homepage_parameter_current_value);
        parameterOptimalValue = itemView.findViewById(R.id.homepage_parameter_optimal_value);
        parameterImage = itemView.findViewById(R.id.homepage_parameter_image);
    }

    /**
     * Return the parameter name TextView.
     * @return the parameter name view
     */
    public TextView getParameterName() {
        return parameterName;
    }

    /**
     * Returns the parameter current value TextView.
     * @return the parameter current value view
     */
    public TextView getParameterCurrentValue() {
        return parameterCurrentValue;
    }

    /**
     * Returns the parameter optimal value TextView.
     * @return the parameter optimal value view
     */
    public TextView getParameterOptimalValue() {
        return parameterOptimalValue;
    }

    /**
     * Returns the parameter ImageView.
     * @return the parameter image view
     */
    public AppCompatImageView getParameterImage() {
        return parameterImage;
    }
}
