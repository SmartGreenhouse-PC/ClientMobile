package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.Map;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;

public interface GreenhouseViewModel {
    /**
     * Update the view plant information.
     * @param plant containing the information.
     */
    void updatePlantInformation(Plant plant);

    /**
     * Update the view parameter information.
     * @param parameterType type of the parameter
     * @param min value of the parameter.
     * @param max value of the parameter.
     * @param unit of measurement of the parameter.
     */
    void updateParameterInfo(ParameterType parameterType, Double min, Double max, String unit);

    /**
     * Update the view parameter value.
     * @param parameter type of the parameter.
     * @param parameterValue new value.
     */
    void updateParameterValue(ParameterType parameter, ParameterValue parameterValue);

    /**
     * Get the parameter value live data.
     * @return the parameter value live data.
     */
    LiveData<Map<ParameterType, ParameterValue>> getParameterValueLiveData();

    /**
     * Get the optimal parameter live data.
     * @return the optimal parameter live data.
     */
    LiveData<Map<ParameterType, String>> getOptimalParameterLiveData();

    /**
     * Get the plant live data.
     * @return the plant live data.
     */
    LiveData<Plant> getPlantLiveData();

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
