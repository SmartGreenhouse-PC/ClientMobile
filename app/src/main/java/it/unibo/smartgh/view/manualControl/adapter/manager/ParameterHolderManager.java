package it.unibo.smartgh.view.manualControl.adapter.manager;

import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;

/**
 * An interface that represents a generic parameter holder.
 */
public interface ParameterHolderManager {

    /**
     * Sets the operation holder.
     * @param holder the operation holder
     */
    void setHolder(OperationViewHolder holder);

    /**
     * Set the current plant.
     * @param plant the current plant
     */
    void setPlant(Plant plant);

    /**
     * Sets the elements.
     */
    void setElement();

    /**
     * Sets the modality.
     * @param modality the operation modality
     */
    void setManualModality(Boolean modality);

    /**
     * Returns true if the holder is already set, false otherwise.
     * @return true, if is already set, false otherwise
     */
    Boolean isHolderAlreadySet();
}
