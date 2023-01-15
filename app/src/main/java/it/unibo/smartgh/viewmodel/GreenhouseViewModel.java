package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.Map;

import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;

public interface GreenhouseViewModel {
    void updatePlantInformation(String name, String description, String plantImg);

    void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit);

    void updateParameterValue(ParameterType parameter, ParameterValue parameterValue);

    LiveData<Map<ParameterType, ParameterValue>> getParameterValueLiveData();
}
