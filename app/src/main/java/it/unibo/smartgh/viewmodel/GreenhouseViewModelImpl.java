package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;

import it.unibo.smartgh.data.homepage.GreenhouseRepository;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;

public class GreenhouseViewModelImpl extends AndroidViewModel implements GreenhouseViewModel {

    protected final GreenhouseRepository greenhouseRepository;
    protected final MutableLiveData<Map<ParameterType, ParameterValue>> parameterValueLiveData;

    public GreenhouseViewModelImpl(@NonNull Application application) {
        super(application);
        parameterValueLiveData = new MutableLiveData<>(initializeMap());
        greenhouseRepository = new GreenhouseRepository(this);
        greenhouseRepository.initializeData();
    }

    private Map<ParameterType, ParameterValue> initializeMap() {
        Map<ParameterType, ParameterValue> map = new HashMap<>();
        map.put(ParameterType.BRIGHTNESS, new ParameterValueImpl());
        map.put(ParameterType.HUMIDITY, new ParameterValueImpl());
        map.put(ParameterType.SOIL_MOISTURE, new ParameterValueImpl());
        map.put(ParameterType.TEMPERATURE, new ParameterValueImpl("63af0ae025d55e9840cbc1fa", new Date(), 0.0));
        return map;
    }

    @Override
    public void updatePlantInformation(String name, String description, String plantImg) {
    }

    @Override
    public void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {

    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        Map<ParameterType, ParameterValue> map = parameterValueLiveData.getValue();
        map.put(parameter, parameterValue);
        parameterValueLiveData.postValue(map);
    }

    @Override
    public LiveData<Map<ParameterType, ParameterValue>> getParameterValueLiveData() {
        return parameterValueLiveData;
    }
}
