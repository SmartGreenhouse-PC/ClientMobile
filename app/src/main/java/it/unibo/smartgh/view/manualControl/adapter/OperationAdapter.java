package it.unibo.smartgh.view.manualControl.adapter;


import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.recyclerview.Adapter;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<ParameterType, ParameterValue>> {

    private List<Pair<ParameterType, ParameterValue>> parameterList;
    private Plant plant;
    private final Activity activity;

    public OperationAdapter(Activity activity) {
        this.parameterList = new ArrayList<>();
        this.activity = activity;
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
        holder.getParameterName().setText(element.first.getTitle());
        holder.getCurrentValue().setText(String.valueOf(element.second.getValue()));
        if(this.plant != null) {
            this.setElement(element.first, holder);
        }
    }

    private void setElement(ParameterType parameter, OperationViewHolder holder) {
        switch (parameter) {
            case BRIGHTNESS:
                this.setBrightnessElement(holder);
                break;
            case TEMPERATURE:
               this.setTemperatureElement(holder);
                break;
            case HUMIDITY:
               this.setHumidityElement(holder);
                break;
            case SOIL_MOISTURE:
                this.setSoilMoistureElement(holder);
                break;
            default:
                break;
        }

    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
    }

    private void setBrightnessElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_brightness);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinBrightness() + " - " + plant.getMaxBrightness());
    }

    private void setTemperatureElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_temperature);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinTemperature() + " - " + plant.getMaxTemperature());
    }

    private void setHumidityElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_humidity);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinHumidity() + " - " + plant.getMaxHumidity());
    }

    private void setSoilMoistureElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_soil_moisture);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinSoilMoisture() + " - " + plant.getMaxSoilMoisture());
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
