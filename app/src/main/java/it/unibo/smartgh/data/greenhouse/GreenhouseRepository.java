package it.unibo.smartgh.data.greenhouse;

import java.util.List;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;

/**
 * This interface represents the greenhouse repository.
 */
public interface GreenhouseRepository {

    /**
     * Initialize the view with the greenhouse data.
     */
    void initializeData();

    /**
     * Sets all the name of the different greenhouses.
     */
    void setAllGreenhousesName();

    /**
     * Set the id of the current greenhouse selected.
     * @param greenhouseId the id of the greenhouse.
     */
    void setGreenhouseId(String greenhouseId);

    void updateGreenhousesName(List<String> greenhousesName);

    /**
     * Update the view plant information.
     * @param plant containing the information.
     */
    void updatePlantInformation(Plant plant);

    /**
     * Update the view parameter value.
     * @param parameter type of the parameter.
     * @param parameterValue new value.
     */
    void updateParameterValue(ParameterType parameter, ParameterValue parameterValue);

    /**
     * Update greenhouse management modality.
     * @param modality the new modality
     */
    void changeModality(Modality modality);

    /**
     * Update the view modality.
     * @param actualModality actual modality.
     */
    void updateModality(Modality actualModality);

    /**
     * Close the modality socket.
     */
    void closeModalitySocket();

    /**
     * Initialize the modality socket.
     */
    void initializeModalitySocket();
}
