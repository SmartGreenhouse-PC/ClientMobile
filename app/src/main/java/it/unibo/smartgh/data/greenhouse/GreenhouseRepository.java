package it.unibo.smartgh.data.greenhouse;

import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;

public interface GreenhouseRepository {
    void initializeData();

    void updatePlantInformation(Plant plant);

    void updateParameterOptimalValues(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit);

    void updateParameterValue(ParameterType parameter, ParameterValue parameterValue);

    //todo add metodo put modality
}
