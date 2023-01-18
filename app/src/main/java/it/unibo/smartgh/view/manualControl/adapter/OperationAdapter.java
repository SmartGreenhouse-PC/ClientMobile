package it.unibo.smartgh.view.manualControl.adapter;


import android.app.Activity;
import android.graphics.Color;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
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
import kotlin.Triple;

/**
 * A class that represents the operation adapter.
 */
public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Triple<ParameterType, ParameterValue, String>> {

    public static final String ATTIVA_VENTILAZIONE = "attiva ventilazione";
    public static final String DISATTIVA_VENTILAZIONE = "disattiva ventilazione";
    public static final String ATTIVA_IRRIGAZIONE = "attiva irrigazione";
    public static final String DISATTIVA_IRRIGAZIONE = "disattiva irrigazione";
    public static final String LUMINOSITY = "LUMINOSITY ";
    public static final String HUMIDITY = "HUMIDITY ";
    public static final String IRRIGATION = "IRRIGATION ";
    public static final String TEMPERATURE = "TEMPERATURE ";
    private final Activity activity;

    private List<Triple<ParameterType, ParameterValue, String>> parameterList;
    private OperationViewModel viewModel;
    private Plant plant;
    private final BrightnessHolderManager brightnessHolderManager;
    private final TemperatureHolderManager temperatureHolderManager;
    private final HumidityHolderManager humidityHolderManager;
    private final SoilMoistureHolderManager soilMoistureHolderManager;

    /**
     * Constructor of {@link OperationAdapter}.
     * @param activity the current activity
     */
    public OperationAdapter(Activity activity) {
        this.parameterList = new ArrayList<>();
        this.brightnessHolderManager = new BrightnessHolderManager(activity, this);
        this.temperatureHolderManager = new TemperatureHolderManager(activity, this);
        this.humidityHolderManager = new HumidityHolderManager(activity, this);
        this.soilMoistureHolderManager = new SoilMoistureHolderManager(activity ,this);
        this.activity = activity;
    }

    @Override
    public void setData(List<Triple<ParameterType, ParameterValue, String>> data) {
        this.parameterList = data;
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
        final Triple<ParameterType, ParameterValue, String> element = parameterList.get(position);
        final String currentValue = element.component2().getValue() + " " + element.component2().getUnit();
        if(element.component2().getStatus() != null){
            if(element.component2().getStatus().equals("alarm")){
                holder.getCurrentValue().setTextColor(ContextCompat.getColor(activity, R.color.alarm));
            } else {
                holder.getCurrentValue().setTextColor(ContextCompat.getColor(activity, R.color.normal));
            }
        }
        holder.getParameterName().setText(element.component1().getTitle());
        holder.getCurrentValue().setText(currentValue);
        if(this.plant != null && (!this.brightnessHolderManager.isHolderAlreadySet()
                || !this.temperatureHolderManager.isHolderAlreadySet()
                || !this.humidityHolderManager.isHolderAlreadySet()
                || !this.soilMoistureHolderManager.isHolderAlreadySet())) {
            this.setElement(element.component1(), holder);
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
