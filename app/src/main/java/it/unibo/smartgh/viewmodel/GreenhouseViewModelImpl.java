package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import it.unibo.smartgh.data.greenhouse.GreenhouseRepository;
import it.unibo.smartgh.data.greenhouse.GreenhouseRepositoryImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;

public class GreenhouseViewModelImpl extends AndroidViewModel implements GreenhouseViewModel {

    protected final GreenhouseRepository greenhouseRepository;
    protected final MutableLiveData<Map<ParameterType, ParameterValue>> parameterValueLiveData;
    protected final MutableLiveData<Map<ParameterType, String>> optimalValuesLiveData;
    private final MutableLiveData<Plant> plantLiveData;

    public GreenhouseViewModelImpl(@NonNull Application application) {
        super(application);
        parameterValueLiveData = new MutableLiveData<>(initializeMap(new ParameterValueImpl()));
        optimalValuesLiveData = new MutableLiveData<>(initializeMap(""));
        greenhouseRepository = new GreenhouseRepositoryImpl(this);
        plantLiveData = new MutableLiveData<>();
        greenhouseRepository.initializeData();
    }

    private <V> Map<ParameterType, V> initializeMap(V value) {
        Map<ParameterType, V> map = new HashMap<>();
        map.put(ParameterType.BRIGHTNESS, value);
        map.put(ParameterType.HUMIDITY, value);
        map.put(ParameterType.SOIL_MOISTURE, value);
        map.put(ParameterType.TEMPERATURE, value);
        return map;
    }

    @Override
    public void updatePlantInformation(Plant plant) {
        this.plantLiveData.postValue(plant);
    }

    @Override
    public void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {
        Map<ParameterType, String> map = optimalValuesLiveData.getValue();
        if (map != null) {
            map.put(parameterType, minBrightness + " " + unit + " - " + maxBrightness + " " + unit);
        }
        optimalValuesLiveData.postValue(map);
    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        System.out.println("update parameter value");
        Map<ParameterType, ParameterValue> map = parameterValueLiveData.getValue();
        if (map != null) {
            map.put(parameter, parameterValue);
        }
        parameterValueLiveData.postValue(map);
    }

    @Override
    public LiveData<Map<ParameterType, ParameterValue>> getParameterValueLiveData() {
        return parameterValueLiveData;
    }

    @Override
    public LiveData<Map<ParameterType, String>> getOptimalParameterLiveData() {
        return optimalValuesLiveData;
    }

    @Override
    public LiveData<Plant> getPlantLiveData() {
        return plantLiveData;
    }
}
