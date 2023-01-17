package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;

import it.unibo.smartgh.R;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;

public class SoilMoistureHolderManager extends AbstractParameterHolderManager {
    public static final String ATTIVA_IRRIGAZIONE = "attiva irrigazione";
    public static final String DISATTIVA_IRRIGAZIONE = "disattiva irrigazione";
    public static final String IRRIGATION = "IRRIGATION ";

    private Button soilMoistureButton;
    private OperationViewHolder holder;

    public SoilMoistureHolderManager(Activity activity, OperationAdapter adapter){
        super(activity, adapter);
        this.soilMoistureButton = new  Button(this.activity.getApplicationContext());
    }

    @Override
    public void setHolder(OperationViewHolder holder) {
        this.holder = holder;
    }

    @Override
    public void setManualModality(Boolean modality) {
        super.setManualModality(modality);
        this.soilMoistureButton.setEnabled(this.modality);
    }

    @Override
    public void setElement() {
        Drawable drawable = ContextCompat.getDrawable(this.activity, R.drawable.ic_soil_moisture);
        holder.getOptimalRange().setText(plant.getMinSoilMoisture() + " - " + plant.getMaxSoilMoisture() + plant.getUnitMap().get("soilMoisture"));
        holder.getParameterImage().setImageDrawable(drawable);
        this.setSoilMoistureOperationElement();
    }

    public void setButtonTextSoilMoisture(String soilMoisture) {
        this.soilMoistureButton.setText(soilMoisture.equals(ATTIVA_IRRIGAZIONE) ? DISATTIVA_IRRIGAZIONE : ATTIVA_IRRIGAZIONE);
    }

    private void setSoilMoistureOperationElement() {
        this.soilMoistureButton.setEnabled(this.modality);
        this.soilMoistureButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.soilMoistureButton.setText(ATTIVA_IRRIGAZIONE);
        this.soilMoistureButton.setOnClickListener(v -> {
            Button b = (Button) v;
            String message = b.getText().toString();
            setButtonTextSoilMoisture(message);
            adapter.sendOperation(ParameterType.SOIL_MOISTURE.getName(), IRRIGATION + (message.equals(ATTIVA_IRRIGAZIONE) ? "on" : "off"));
        });
        holder.getOperationsLayout().addView(this.soilMoistureButton);
        this.set = true;
    }
}
