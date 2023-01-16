package it.unibo.smartgh.view.manualControl.adapter;


import android.app.Activity;
import android.content.res.ColorStateList;
import android.graphics.Color;
import android.graphics.drawable.Drawable;
import android.util.Pair;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.recyclerview.Adapter;
import it.unibo.smartgh.viewmodel.OperationViewModel;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<ParameterType, ParameterValue>> {

    private static final int TOTAL_PARAMETER = 4;
    public static final String ATTIVA_VENTILAZIONE = "attiva ventilazione";
    public static final String DISATTIVA_VENTILAZIONE = "disattiva ventilazione";
    public static final String ATTIVA_IRRIGAZIONE = "attiva irrigazione";
    public static final String DISATTIVA_IRRIGAZIONE = "disattiva irrigazione";
    public static final String HUMIDITY = "HUMIDITY ";
    public static final String IRRIGATION = "IRRIGATION ";
    public static final String INCREASE = "increase";
    public static final String DECREASE = "decrease";
    public static final String TURN_OFF = "turn-off";
    public static final String LUMINOSITY = "LUMINOSITY ";
    public static final String TEMPERATURE = "TEMPERATURE ";

    private List<Pair<ParameterType, ParameterValue>> parameterList;
    private Plant plant;
    private final Activity activity;
    private int countParameterSet;
    private final SeekBar seekbar;
    private final Map<ParameterType, List<Button>> controlsParameter;
    private OperationViewModel viewModel;

    public OperationAdapter(Activity activity) {
        this.parameterList = new ArrayList<>();
        this.activity = activity;
        this.countParameterSet = 0;
        this.seekbar = new SeekBar(this.activity.getApplicationContext());
        this.seekbar.setEnabled(false);
        this.controlsParameter = new HashMap(){{
            put(ParameterType.BRIGHTNESS, new ArrayList<>());
            put(ParameterType.HUMIDITY, new ArrayList<>());
            put(ParameterType.SOIL_MOISTURE, new ArrayList<>());
            put(ParameterType.TEMPERATURE, new ArrayList<>());
        }};
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
        if(this.plant != null && this.countParameterSet < TOTAL_PARAMETER) {
            this.setElement(element.first, holder);
        }
    }

    public void updateModality(boolean manualModalityEnabled){
        if(!manualModalityEnabled){
            this.seekbar.setEnabled(false);
            this.controlsParameter.forEach((k,v) -> v.forEach(b -> b.setEnabled(false)));
        }else{
            this.seekbar.setEnabled(true);
            this.controlsParameter.forEach((k,v) -> v.forEach(b -> b.setEnabled(true)));
        }
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
    }

    public void setLastOperation(Map<ParameterType, Operation> map) {
        map.forEach((k, v) -> {
            if(v.getAction() != null) {
                switch (k) {
                    case BRIGHTNESS:
                        this.setLampBrightnessLevel(Integer.parseInt(v.getAction().replace(LUMINOSITY, "")));
                        break;
                    case TEMPERATURE:
                        this.setEnableTemperatureButton(v.getAction().replace(TEMPERATURE, ""));
                        break;
                    case HUMIDITY:
                        this.setButtonTextHumidity(v.getAction().replace(HUMIDITY, "").equals("on") ? ATTIVA_VENTILAZIONE : DISATTIVA_VENTILAZIONE);
                        break;
                    case SOIL_MOISTURE:
                       this.setButtonTextSoilMoisture(v.getAction().replace(IRRIGATION, "").equals("on") ? ATTIVA_IRRIGAZIONE : DISATTIVA_IRRIGAZIONE);
                        break;
                    default:
                        break;
                }
            }
        });

    }

/*---------------------------------Operation-----------------*/
    private void setEnableTemperatureButton(String temperature) {
        this.controlsParameter.get(ParameterType.TEMPERATURE).forEach(b -> b.setEnabled(!b.getText().toString().equals(temperature)));
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

    private void setBrightnessElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_brightness);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinBrightness() + " - " + plant.getMaxBrightness() + plant.getUnitMap().get("brightness"));
        this.setBrightnessOperationElement(holder);
    }

    private void setBrightnessOperationElement(OperationViewHolder holder) {
        int max = 255;
        int[][] states = new int[][] {
                new int[] { android.R.attr.state_enabled}, // enabled
                new int[] {-android.R.attr.state_enabled}, // disabled
        };

        int[] colors = new int[] {
                Color.parseColor("#fcec03"),
                Color.LTGRAY,
        };
        this.seekbar.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.seekbar.setProgressTintList(new ColorStateList(states, colors));
        this.seekbar.setMax(max);
        this.seekbar.setProgress((int) max/2);
        this.seekbar.setOnSeekBarChangeListener(new SeekBar.OnSeekBarChangeListener() {
            private int seekbarValue;
            @Override
            public void onProgressChanged(SeekBar seekBar, int i, boolean b) {
                this.seekbarValue = i;
                //todo metodo chiamato quando il livello della progress bar si modifica
            }

            @Override
            public void onStartTrackingTouch(SeekBar seekBar) {
                //todo metodo chiamato quando l'utente tocca la progress bar
            }

            @Override
            public void onStopTrackingTouch(SeekBar seekBar) {
                setLampBrightnessLevel(seekbarValue);
                sendOperation(ParameterType.BRIGHTNESS.getName(), LUMINOSITY + seekbarValue);
                //metodo chiamato quando utente finisce di toccare la progress bar
            }
        });
        holder.getOperationsLayout().addView(this.seekbar);
        this.countParameterSet  = this.countParameterSet + 1;
    }

    private void setTemperatureElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_temperature);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinTemperature() + " - " + plant.getMaxTemperature() + plant.getUnitMap().get("temperature"));
        this.setTemperatureOperationElement(holder);
    }

    private void setTemperatureOperationElement(OperationViewHolder holder) {
        Button increaseButton = new Button(this.activity.getApplicationContext());
        Button decreaseButton = new Button(this.activity.getApplicationContext());
        Button systemOnOff = new  Button(this.activity.getApplicationContext());
        View.OnClickListener listener = (v) -> {
            Button b = (Button) v;
            if(b.isEnabled()){
                sendOperation(ParameterType.TEMPERATURE.getName(), TEMPERATURE + b.getText().toString());
                setEnableTemperatureButton(b.getText().toString());
            }
        };

        increaseButton.setEnabled(false);
        decreaseButton.setEnabled(false);
        systemOnOff.setEnabled(false);

        increaseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        increaseButton.setText(INCREASE);
        increaseButton.setOnClickListener(listener);

        decreaseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        decreaseButton.setText(DECREASE);
        decreaseButton.setOnClickListener(listener);

        systemOnOff.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        systemOnOff.setText(TURN_OFF);
        systemOnOff.setOnClickListener(listener);

        holder.getOperationsLayout().addView(increaseButton);
        holder.getOperationsLayout().addView(decreaseButton);
        holder.getOperationsLayout().addView(systemOnOff);

        this.controlsParameter.get(ParameterType.TEMPERATURE).add(increaseButton);
        this.controlsParameter.get(ParameterType.TEMPERATURE).add(decreaseButton);
        this.controlsParameter.get(ParameterType.TEMPERATURE).add(systemOnOff);
        this.countParameterSet  = this.countParameterSet + 1;
    }

    private void setHumidityElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_humidity);
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinHumidity() + " - " + plant.getMaxHumidity() + plant.getUnitMap().get("humidity"));
        this.setHumidityOperationElement(holder);
    }

    private void setHumidityOperationElement(OperationViewHolder holder) {
        Button button = new  Button(this.activity.getApplicationContext());
        button.setEnabled(false);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(ATTIVA_VENTILAZIONE);
        button.setOnClickListener(v -> {
            Button b = (Button) v;
            String message = b.getText().toString();
            setButtonTextHumidity(message);
            sendOperation(ParameterType.HUMIDITY.getName(), HUMIDITY + (message.equals(ATTIVA_VENTILAZIONE) ? "on" : "off"));
        });
        holder.getOperationsLayout().addView(button);
        this.controlsParameter.get(ParameterType.HUMIDITY).add(button);
        this.countParameterSet  = this.countParameterSet + 1;
    }

    private void setSoilMoistureElement(OperationViewHolder holder){
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_soil_moisture);
        holder.getOptimalRange().setText(plant.getMinSoilMoisture() + " - " + plant.getMaxSoilMoisture() + plant.getUnitMap().get("soilMoisture"));
        holder.getParameterImage().setImageDrawable(drawable);
        this.setSoilMoistureOperationElement(holder);
    }

    private void setSoilMoistureOperationElement(OperationViewHolder holder) {
        Button button = new  Button(this.activity.getApplicationContext());
        button.setEnabled(false);
        button.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        button.setText(ATTIVA_IRRIGAZIONE);
        button.setOnClickListener(v -> {
            Button b = (Button) v;
            String message = b.getText().toString();
            this.setButtonTextSoilMoisture(message);
            this.sendOperation(ParameterType.SOIL_MOISTURE.getName(), IRRIGATION + (message.equals(ATTIVA_IRRIGAZIONE) ? "on" : "off"));
        });
        holder.getOperationsLayout().addView(button);
        this.controlsParameter.get(ParameterType.SOIL_MOISTURE).add(button);
        this.countParameterSet  = this.countParameterSet + 1;
    }

    private void sendOperation(String parameter, String action){
        this.viewModel.sendNewOperation(parameter, action);
    }


    private void setButtonTextSoilMoisture(String soilMoisture) {
        System.out.println(this.controlsParameter.get(ParameterType.SOIL_MOISTURE).toString());
        this.controlsParameter.get(ParameterType.SOIL_MOISTURE).get(0).setText(soilMoisture.equals(ATTIVA_IRRIGAZIONE) ? DISATTIVA_IRRIGAZIONE : ATTIVA_IRRIGAZIONE);
    }

    private void setButtonTextHumidity(String humidity) {
        this.controlsParameter.get(ParameterType.HUMIDITY).get(0).setText(humidity.equals(ATTIVA_VENTILAZIONE) ? DISATTIVA_VENTILAZIONE : ATTIVA_VENTILAZIONE);
    }

    private void setLampBrightnessLevel(int progress) {
        this.seekbar.setProgress(progress);
    }
}
