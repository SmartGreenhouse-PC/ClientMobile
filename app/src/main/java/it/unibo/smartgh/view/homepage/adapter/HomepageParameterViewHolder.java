package it.unibo.smartgh.view.homepage.adapter;

import android.view.View;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.AppCompatImageView;
import androidx.core.widget.ImageViewCompat;
import androidx.recyclerview.widget.RecyclerView;

import it.unibo.smartgh.R;

public class HomepageParameterViewHolder extends RecyclerView.ViewHolder {

    private final TextView parameterName;
    private final TextView parameterCurrentValue;
    private final TextView parameterOptimalValue;
    private final AppCompatImageView parameterImage;


    public HomepageParameterViewHolder(@NonNull View itemView) {
        super(itemView);
        parameterName = itemView.findViewById(R.id.homepage_parameter_name);
        parameterCurrentValue = itemView.findViewById(R.id.homepage_parameter_current_value);
        parameterOptimalValue = itemView.findViewById(R.id.homepage_parameter_optimal_value);
        parameterImage = itemView.findViewById(R.id.homepage_parameter_image);
    }

    public TextView getParameterName() {
        return parameterName;
    }

    public TextView getParameterCurrentValue() {
        return parameterCurrentValue;
    }

    public TextView getParameterOptimalValue() {
        return parameterOptimalValue;
    }

    public AppCompatImageView getParameterImage() {
        return parameterImage;
    }
}
