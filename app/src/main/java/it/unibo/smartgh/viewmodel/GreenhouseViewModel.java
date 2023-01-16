package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.Map;

import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;

public interface GreenhouseViewModel {
    void updatePlantInformation(Plant plant);

    void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit);

    void updateParameterValue(ParameterType parameter, ParameterValue parameterValue);

    LiveData<Map<ParameterType, ParameterValue>> getParameterValueLiveData();

    LiveData<Map<ParameterType, String>> getOptimalParameterLiveData();

    LiveData<Plant> getPlantLiveData();

    //todo add metod put modality
}
