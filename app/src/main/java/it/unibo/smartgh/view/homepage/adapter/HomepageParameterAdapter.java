package it.unibo.smartgh.view.homepage.adapter;

import android.app.Activity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.view.recyclerview.Adapter;
import kotlin.Triple;

/**
 * This class represents the adapter of homepage parameters.
 */
public class HomepageParameterAdapter extends RecyclerView.Adapter<HomepageParameterViewHolder> implements Adapter<Triple<ParameterType, ParameterValue, String>> {

    private final Activity activity;
    private List<Triple<ParameterType, ParameterValue, String>> parameterList;

    /**
     * Constructor of {@link HomepageParameterAdapter}.
     * @param activity the current instance of activity
     */
    public HomepageParameterAdapter(Activity activity) {
        this.activity = activity;
        this.parameterList = new LinkedList<>();
    }

    @NonNull
    @Override
    public HomepageParameterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.homepage_parameter, parent, false);
        return new HomepageParameterViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageParameterViewHolder holder, int position) {
        final Triple<ParameterType, ParameterValue, String> param = parameterList.get(position);
        final String value = param.component2().getValue() + " " + param.component2().getUnit();
        holder.getParameterName().setText(param.component1().getTitle());
        holder.getParameterImage().setImageDrawable(ContextCompat.getDrawable(activity, param.component1().getImagePath()));
        holder.getParameterOptimalValue().setText(param.component3());
        holder.getParameterCurrentValue().setText(value);
        if (param.component2().getStatus() != null) {
            holder.getParameterCurrentValue().setTextColor(ContextCompat.getColor(activity,
                    param.component2().getStatus().equals("alarm") ? R.color.alarm :
                            R.color.normal));
        }
    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
    }

    @Override
    public void setData(List<Triple<ParameterType, ParameterValue, String>> data) {
        this.parameterList = data;
        notifyDataSetChanged();
    }
}
