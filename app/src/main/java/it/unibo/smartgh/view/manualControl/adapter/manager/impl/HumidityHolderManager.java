package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;
import android.graphics.drawable.Drawable;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.core.content.ContextCompat;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;
import it.unibo.smartgh.view.manualControl.adapter.manager.ParameterHolderManager;

public class HumidityHolderManager extends AbstractParameterHolderManager {
    public static final String ATTIVA_VENTILAZIONE = "attiva ventilazione";
    public static final String DISATTIVA_VENTILAZIONE = "disattiva ventilazione";
    public static final String HUMIDITY = "HUMIDITY ";

    private Button humidityButton;

    public HumidityHolderManager(Activity activity, OperationAdapter adapter){
       super(activity, adapter);
    }


    @Override
    public void setElement() {
        Drawable drawable = ContextCompat.getDrawable(this.activity, ParameterType.HUMIDITY.getImagePath());
        holder.getParameterImage().setImageDrawable(drawable);
        holder.getOptimalRange().setText(plant.getMinHumidity() + " - " + plant.getMaxHumidity() + plant.getUnitMap().get("humidity"));
        this.setHumidityOperationElement();
    }

    @Override
    public void setManualModality(Boolean modality) {
        this.humidityButton.setEnabled(modality);
    }

    public void setButtonTextHumidity(String humidity) {
        this.humidityButton.setText(humidity.equals(ATTIVA_VENTILAZIONE) ? DISATTIVA_VENTILAZIONE : ATTIVA_VENTILAZIONE);
    }

    private void setHumidityOperationElement() {
        this.humidityButton= new  Button(this.activity.getApplicationContext());
        this.humidityButton.setEnabled(false);
        this.humidityButton.setLayoutParams(new ViewGroup.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT));
        this.humidityButton.setText(ATTIVA_VENTILAZIONE);
        this.humidityButton.setOnClickListener(v -> {
            Button b = (Button) v;
            String message = b.getText().toString();
            setButtonTextHumidity(message);
            adapter.sendOperation(ParameterType.HUMIDITY.getName(), HUMIDITY + (message.equals(ATTIVA_VENTILAZIONE) ? "on" : "off"));
        });
        holder.getOperationsLayout().addView(this.humidityButton);
        this.set = true;
    }

}
