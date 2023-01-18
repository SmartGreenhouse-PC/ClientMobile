package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.List;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import kotlin.Triple;

/**
 * An interface that represents the greenhouse view model.
 */
public interface GreenhouseViewModel {

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
     * Get the plant live data.
     * @return the plant live data.
     */
    LiveData<Plant> getPlantLiveData();

    /**
     * Get the parameters live data.
     * @return the parameters live data
     */
    LiveData<List<Triple<ParameterType, ParameterValue, String>>> getParametersLiveData();

    /**
     * Get the status live data.
     * @return the status live data
     */
    LiveData<String> getStatusLiveData();

    /**
     * Update greenhouse management modality.
     * @param modality the new modality
     */
    void changeModality(Modality modality);

    /**
     * Update the view modality.
     * @param actualModality the actual modality.
     */
    void updateModality(Modality actualModality);

    /**
     * Get the modality live data.
     * @return the modality live data.
     */
    LiveData<Modality> getModalityLiveData();

}
