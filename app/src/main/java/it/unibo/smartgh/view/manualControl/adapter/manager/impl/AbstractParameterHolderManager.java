package it.unibo.smartgh.view.manualControl.adapter.manager.impl;

import android.app.Activity;

import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationAdapter;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;
import it.unibo.smartgh.view.manualControl.adapter.manager.ParameterHolderManager;

/**
 * Abstract class for manage the parameter holder.
 */
public abstract class AbstractParameterHolderManager implements ParameterHolderManager {

    protected final Activity activity;
    protected final OperationAdapter adapter;
    protected Plant plant;
    protected Boolean set;
    protected Boolean modality;

    /**
     * Constructor of {@link AbstractParameterHolderManager}.
     * @param activity the current activity
     * @param adapter the current operation adapter
     */
    public AbstractParameterHolderManager(Activity activity, OperationAdapter adapter){
        this.activity = activity;
        this.adapter = adapter;
        this.modality = false;
        this.set = false;
    }

    @Override
    public void setPlant(Plant plant){
        this.plant = plant;
    }

    @Override
    public void setManualModality(Boolean modality) {
        this.modality = modality;
    }

    @Override
    public Boolean isHolderAlreadySet(){
        return this.set;
    }

    public abstract void setHolder(OperationViewHolder holder);

    public abstract void setElement();

}
