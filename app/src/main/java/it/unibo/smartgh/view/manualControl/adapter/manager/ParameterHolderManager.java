package it.unibo.smartgh.view.manualControl.adapter.manager;

import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.view.manualControl.adapter.OperationViewHolder;

public interface ParameterHolderManager {

    void setHolder(OperationViewHolder holder);

    void setPlant(Plant plant);

    void setElement();

    void setManualModality(Boolean modality);

    Boolean isHolderAlreadySet();
}
