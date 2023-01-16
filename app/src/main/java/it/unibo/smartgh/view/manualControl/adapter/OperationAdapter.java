package it.unibo.smartgh.view.manualControl.adapter;


import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.Parameter;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.recyclerview.Adapter;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<ParameterType, ParameterValue>> {

    private List<Pair<ParameterType, ParameterValue>> parameterList;
    private Plant plant;

    public OperationAdapter() {
        this.parameterList = new ArrayList<>();
    }

    @Override
    public void setData(List<Pair<ParameterType, ParameterValue>> data) {
        this.parameterList = data;
        System.out.println("Data setted");
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
        System.out.println("On bind view holder called");
        final Pair<ParameterType, ParameterValue> element = parameterList.get(position);
        holder.getParameterName().setText(element.first.getName());
        holder.getCurrentValue().setText(String.valueOf(element.second.getValue()));
        if(this.plant != null) {
            this.setOptimalValues(element.first, holder);
        }
        //todo impostare optimal range impostare operazioni
    }

    private void setOptimalValues(ParameterType parameter, OperationViewHolder holder) {
        switch (parameter) {
            case BRIGHTNESS:
                holder.getOptimalRange().setText(plant.getMinBrightness() + " - " + plant.getMaxBrightness());
                break;
            case TEMPERATURE:
                holder.getOptimalRange().setText(plant.getMinTemperature() + " - " + plant.getMaxTemperature());
                break;
            case HUMIDITY:
                holder.getOptimalRange().setText(plant.getMinHumidity() + " - " + plant.getMaxHumidity());
                break;
            case SOIL_MOISTURE:
                holder.getOptimalRange().setText(plant.getMinSoilMoisture() + " - " + plant.getMaxSoilMoisture());
                break;
            default:
                break;

        }

    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
    }


    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
