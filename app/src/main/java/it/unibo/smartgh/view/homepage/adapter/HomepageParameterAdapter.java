package it.unibo.smartgh.view.homepage.adapter;

import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.smartgh.entity.parameter.Parameter;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.view.recyclerview.Adapter;

public class HomepageParameterAdapter extends RecyclerView.Adapter<HomepageParameterViewHolder> implements Adapter<Parameter> {

    private final Map<ParameterType, String> optimalValues;
    private final Map<ParameterType, ParameterValue> parameterValues;

    public HomepageParameterAdapter() {
        this.optimalValues = new HashMap<>();
        this.parameterValues = new HashMap<>();
    }

    @NonNull
    @Override
    public HomepageParameterViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        return null;
    }

    @Override
    public void onBindViewHolder(@NonNull HomepageParameterViewHolder holder, int position) {

    }

    @Override
    public int getItemCount() {
        return 0;
    }

    @Override
    public void setData(List<Parameter> data) {

    }
}
