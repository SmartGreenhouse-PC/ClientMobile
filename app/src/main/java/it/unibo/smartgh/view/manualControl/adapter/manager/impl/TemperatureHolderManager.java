package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.SeekBar;

import androidx.core.content.ContextCompat;

import java.util.ArrayList;
import java.util.Arrays;
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

    private OperationViewHolder holder;
    private final List<Button> controlsList;
    private final Button increaseButton;
    private final Button decreaseButton;
    private final Button systemOnOff;

    public TemperatureHolderManager(Activity activity, OperationAdapter adapter){
        super(activity, adapter);
        this.increaseButton = new Button(this.activity.getApplicationContext());
        this.decreaseButton = new Button(this.activity.getApplicationContext());
        this.systemOnOff = new Button(this.activity.getApplicationContext());
        this.controlsList = Arrays.asList(this.increaseButton, this.decreaseButton, this.systemOnOff);
    }

    @Override
    public void setHolder(OperationViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void setManualModality(Boolean modality) {
        super.setManualModality(modality);
        this.controlsList.forEach(b -> b.setEnabled(this.modality));
    }

    @Override
    public void setElement() {
        Drawable drawable = ContextCompat.getDrawable(this.activity, ParameterType.TEMPERATURE.getImagePath());
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinTemperature() + " - " + plant.getMaxTemperature() + plant.getUnitMap().get("temperature"));
        this.setTemperatureOperationElement();
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

        View.OnClickListener listener = (v) -> {
            Button b = (Button) v;
            if(b.isEnabled()){
                String message = "";
                switch(b.getText().toString()){
                    case AUMENTA:
                        message = INCREASE;
                        break;
                    case DIMINUISCI:
                        message = DECREASE;
                        break;
                    case SPEGNI_SISTEMI:
                        message = TURN_OFF;
                        break;
                    default:
                        break;
                }
                adapter.sendOperation(ParameterType.TEMPERATURE.getName(), TEMPERATURE + message);
                setEnableTemperatureButton(b.getText().toString());
            }
        };

        this.controlsList.forEach(b -> b.setEnabled(this.modality));

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

        this.set = true;
    }

}
