package it.unibo.smartgh.view.homepage.adapter;

import android.app.Activity;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.recyclerview.Adapter;
import kotlin.Triple;

public class HomepageParameterAdapter extends RecyclerView.Adapter<HomepageParameterViewHolder> implements Adapter<Triple<ParameterType, String, String>> {

    private final Activity activity;
    private List<Triple<ParameterType, String, String>> parameterList;

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
        final Triple<ParameterType, String, String> param = parameterList.get(position);
        holder.getParameterName().setText(param.component1().getTitle());
        holder.getParameterImage().setImageDrawable(ContextCompat.getDrawable(activity, param.component1().getImagePath()));
        holder.getParameterCurrentValue().setText(param.component2());
        holder.getParameterOptimalValue().setText(param.component3());
    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
    }

    @Override
    public void setData(List<Triple<ParameterType, String, String>> data) {
        this.parameterList = data;
        notifyDataSetChanged();
    }
}
