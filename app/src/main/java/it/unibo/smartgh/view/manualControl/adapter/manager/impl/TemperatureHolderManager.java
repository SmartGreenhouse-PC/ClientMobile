package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.List;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;
import it.unibo.smartgh.view.manualControl.adapter.manager.ParameterHolderManager;

public class TemperatureHolderManager extends AbstractParameterHolderManager {
    public static final String INCREASE = "increase";
    public static final String DECREASE = "decrease";
    public static final String TURN_OFF = "turn-off";
    public static final String AUMENTA = "aumenta";
    public static final String DIMINUISCI = "diminuisci";
    public static final String SPEGNI_SISTEMI = "spegni sistemi";
    public static final String TEMPERATURE = "TEMPERATURE ";
    private Boolean set;
    private List<Button> controlsList;

    public TemperatureHolderManager(Activity activity, OperationAdapter adapter){
        super(activity, adapter);
        this.controlsList = new ArrayList<>();
    }

    @Override
    public void setElement() {
        Drawable drawable = ContextCompat.getDrawable(this.activity, ParameterType.TEMPERATURE.getImagePath());
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinTemperature() + " - " + plant.getMaxTemperature() + plant.getUnitMap().get("temperature"));
        this.setTemperatureOperationElement();
    }

    @Override
    public void setManualModality(Boolean modality) {
        this.controlsList.forEach(b -> b.setEnabled(modality));
    }

    public void setEnableTemperatureButton(final String temperature) {
        this.controlsList.forEach(b -> {
            String message = "";
            switch(temperature){
                case INCREASE:
                    message = AUMENTA;
                    break;
                case DECREASE:
                    message = DIMINUISCI;
                    break;
                case TURN_OFF:
                    message = SPEGNI_SISTEMI;
                    break;
                default:
                    message = temperature;
                break;
            }
            b.setEnabled(!b.getText().toString().equals(message));
        });
    }

    private void setTemperatureOperationElement() {
        Button increaseButton = new Button(this.activity.getApplicationContext());
        Button decreaseButton = new Button(this.activity.getApplicationContext());
        Button systemOnOff = new  Button(this.activity.getApplicationContext());
        View.OnClickListener listener = (v) -> {
            Button b = (Button) v;
            if(b.isEnabled()){
                adapter.sendOperation(ParameterType.TEMPERATURE.getName(), TEMPERATURE + b.getText().toString());
                setEnableTemperatureButton(b.getText().toString());
            }
        };

        increaseButton.setEnabled(false);
        decreaseButton.setEnabled(false);
        systemOnOff.setEnabled(false);

        increaseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        increaseButton.setText(AUMENTA);
        increaseButton.setOnClickListener(listener);

        decreaseButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        decreaseButton.setText(DIMINUISCI);
        decreaseButton.setOnClickListener(listener);

        systemOnOff.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        systemOnOff.setText(SPEGNI_SISTEMI);
        systemOnOff.setOnClickListener(listener);

        holder.getOperationsLayout().addView(increaseButton);
        holder.getOperationsLayout().addView(decreaseButton);
        holder.getOperationsLayout().addView(systemOnOff);

        this.controlsList.add(increaseButton);
        this.controlsList.add(decreaseButton);
        this.controlsList.add(systemOnOff);
        this.set = true;
    }

}
