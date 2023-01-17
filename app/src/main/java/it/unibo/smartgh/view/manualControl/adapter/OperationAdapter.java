package it.unibo.smartgh.view.manualControl.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.manager.impl.BrightnessHolderManager;
import it.unibo.smartgh.view.manualControl.adapter.manager.impl.HumidityHolderManager;
import it.unibo.smartgh.view.manualControl.adapter.manager.impl.SoilMoistureHolderManager;
import it.unibo.smartgh.view.manualControl.adapter.manager.impl.TemperatureHolderManager;
import it.unibo.smartgh.view.recyclerview.Adapter;
import it.unibo.smartgh.viewmodel.OperationViewModel;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<ParameterType, ParameterValue>> {

    public static final String ATTIVA_VENTILAZIONE = "attiva ventilazione";
    public static final String DISATTIVA_VENTILAZIONE = "disattiva ventilazione";
    public static final String ATTIVA_IRRIGAZIONE = "attiva irrigazione";
    public static final String DISATTIVA_IRRIGAZIONE = "disattiva irrigazione";
    public static final String LUMINOSITY = "LUMINOSITY ";
    public static final String HUMIDITY = "HUMIDITY ";
    public static final String IRRIGATION = "IRRIGATION ";
    public static final String TEMPERATURE = "TEMPERATURE ";

    private List<Pair<ParameterType, ParameterValue>> parameterList;
    private OperationViewModel viewModel;
    private Plant plant;
    private final BrightnessHolderManager brightnessHolderManager;
    private final TemperatureHolderManager temperatureHolderManager;
    private final HumidityHolderManager humidityHolderManager;
    private final SoilMoistureHolderManager soilMoistureHolderManager;

    public OperationAdapter(Activity activity) {
        this.parameterList = new ArrayList<>();
        this.brightnessHolderManager = new BrightnessHolderManager(activity, this);
        this.temperatureHolderManager = new TemperatureHolderManager(activity, this);
        this.humidityHolderManager = new HumidityHolderManager(activity, this);
        this.soilMoistureHolderManager = new SoilMoistureHolderManager(activity ,this);
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
        if(element.second.getStatus() != null){
            System.out.println("STATUS:" + element.second.getStatus());
            if(element.second.getStatus().equals("alarm")){
                holder.getCurrentValue().setTextColor(Color.parseColor("#842029"));
            }else{
                holder.getCurrentValue().setTextColor(Color.parseColor("#04B700"));
            }
        }
        holder.getParameterName().setText(element.first.getTitle());
        holder.getCurrentValue().setText(String.valueOf(element.second.getValue()));
        if(this.plant != null && (!this.brightnessHolderManager.isHolderAlreadySet()
                || !this.temperatureHolderManager.isHolderAlreadySet()
                || !this.humidityHolderManager.isHolderAlreadySet()
                || !this.soilMoistureHolderManager.isHolderAlreadySet())) {
            this.setElement(element.first, holder);
        }
    }

    public void updateModality(boolean manualModalityEnabled){
        this.brightnessHolderManager.setManualModality(manualModalityEnabled);
        this.temperatureHolderManager.setManualModality(manualModalityEnabled);
        this.humidityHolderManager.setManualModality(manualModalityEnabled);
        this.soilMoistureHolderManager.setManualModality(manualModalityEnabled);
    }

    public void setOperationViewModel(OperationViewModel viewModel){
        this.viewModel = viewModel;
    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
        this.brightnessHolderManager.setPlant(plant);
        this.temperatureHolderManager.setPlant(plant);
        this.humidityHolderManager.setPlant(plant);
        this.soilMoistureHolderManager.setPlant(plant);
    }

    private void setElement(ParameterType parameter, OperationViewHolder holder) {
        switch (parameter) {
            case BRIGHTNESS:
                this.brightnessHolderManager.setHolder(holder);
                this.brightnessHolderManager.setElement();
                break;
            case TEMPERATURE:
                this.temperatureHolderManager.setHolder(holder);
                this.temperatureHolderManager.setElement();
                break;
            case HUMIDITY:
                this.humidityHolderManager.setHolder(holder);
                this.humidityHolderManager.setElement();
                break;
            case SOIL_MOISTURE:
                this.soilMoistureHolderManager.setHolder(holder);
                this.soilMoistureHolderManager.setElement();
                break;
            default:
                break;
        }

    }

    public void setLastOperation(Map<ParameterType, Operation> map) {
        map.forEach((k, v) -> {
            if(v.getAction() != null) {
                switch (k) {
                    case BRIGHTNESS:
                        this.brightnessHolderManager.setLampBrightnessLevel(Integer.parseInt(v.getAction().replace(LUMINOSITY, "")));
                        break;
                    case TEMPERATURE:
                        this.temperatureHolderManager.setEnableTemperatureButton(v.getAction().replace(TEMPERATURE, ""));
                        break;
                    case HUMIDITY:
                        this.humidityHolderManager.setButtonTextHumidity(v.getAction().replace(HUMIDITY, "").equals("on") ? ATTIVA_VENTILAZIONE : DISATTIVA_VENTILAZIONE);
                        break;
                    case SOIL_MOISTURE:
                        this.soilMoistureHolderManager.setButtonTextSoilMoisture(v.getAction().replace(IRRIGATION, "").equals("on") ? ATTIVA_IRRIGAZIONE : DISATTIVA_IRRIGAZIONE);
                        break;
                    default:
                        break;
                }
            }
        });

    }



    public void sendOperation(String parameter, String action){
        this.viewModel.sendNewOperation(parameter, action);
    }




}
