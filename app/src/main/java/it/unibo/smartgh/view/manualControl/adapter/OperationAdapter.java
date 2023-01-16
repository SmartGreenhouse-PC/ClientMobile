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
import android.widget.LinearLayout;
import android.widget.SeekBar;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.core.content.ContextCompat;
import androidx.recyclerview.widget.RecyclerView;

import com.google.android.material.button.MaterialButton;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.recyclerview.Adapter;

public class OperationAdapter extends RecyclerView.Adapter<OperationViewHolder> implements Adapter<Pair<ParameterType, ParameterValue>> {

    private static final int TOTAL_PARAMETER = 4;

    private List<Pair<ParameterType, ParameterValue>> parameterList;
    private Plant plant;
    private final Activity activity;
    private int countParameterSet;
    private final SeekBar seekbar;
    private final List<Button> buttonList;

    public OperationAdapter(Activity activity) {
        this.parameterList = new ArrayList<>();
        this.activity = activity;
        this.countParameterSet = 0;
        this.seekbar = new SeekBar(this.activity.getApplicationContext());
        this.seekbar.setEnabled(false);
        this.buttonList = new ArrayList<>();
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
            this.buttonList.forEach(b -> b.setEnabled(false));
        }else{
            this.seekbar.setEnabled(true);
            this.buttonList.forEach(b -> b.setEnabled(true));
        }
    }

    @Override
    public int getItemCount() {
        return this.parameterList.size();
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
                System.out.println("Brightness seekBar value: " + this.seekbarValue);
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
        Button headLampButton = new Button(this.activity.getApplicationContext());
        Button ventilationButton = new  Button(this.activity.getApplicationContext());
        headLampButton.setEnabled(false);
        ventilationButton.setEnabled(false);
        headLampButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        headLampButton.setText("Attiva lampada termica");
        headLampButton.setOnClickListener(v -> {
            //To do action
        });
        ventilationButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        ventilationButton.setText("Attiva la ventilazione");
        ventilationButton.setOnClickListener(v -> {
            //To do action
        });
        holder.getOperationsLayout().addView(headLampButton);
        holder.getOperationsLayout().addView(ventilationButton);
        this.buttonList.add(headLampButton);
        this.buttonList.add(ventilationButton);
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
        button.setText("Attiva la ventilazione");
        button.setOnClickListener(v -> {
            //Todo action
        });
        holder.getOperationsLayout().addView(button);
        this.buttonList.add(button);
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
        button.setText("Attiva irrigazione");
        button.setOnClickListener(v -> {
            //Todo action
        });
        holder.getOperationsLayout().addView(button);
        this.buttonList.add(button);
        this.countParameterSet  = this.countParameterSet + 1;
    }

    public void setPlant(Plant plant) {
        this.plant = plant;
    }
}
