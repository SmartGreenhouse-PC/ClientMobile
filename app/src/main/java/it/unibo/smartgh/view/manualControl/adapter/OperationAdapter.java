package it.unibo.smartgh.view.manualControl.adapter;


import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.LinkedList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.Parameter;
import it.unibo.smartgh.view.recyclerview.Adapter;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<Parameter, Operation>> {

    private List<Pair<Parameter, Operation>> parameterOperationsList;

    public OperationAdapter() {
        this.parameterOperationsList = new LinkedList<>();
    }

    @Override
    public void setData(List<Pair<Parameter, Operation>> data) {
        this.parameterOperationsList = data;
        notifyDataSetChanged();
    }
    
    @NonNull
    @Override
    public OperationViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType){
        final View layoutView = LayoutInflater.from(parent.getContext()).inflate(R.layout.manual_control_parameter_card_layout, parent, false);
        return new OperationViewHolder(layoutView);
    }

    @Override
    public void onBindViewHolder(@NonNull OperationViewHolder holder, int position) {
        final Pair<Parameter, Operation> element = parameterOperationsList.get(position);
        holder.getParameterName().setText(element.first.getName());
        holder.getCurrentValue().setText(String.valueOf(element.first.getCurrentValue()));
        //todo impostare optimal range impostare operazioni
    }

    @Override
    public int getItemCount() {
        return this.parameterOperationsList.size();
    }
}
