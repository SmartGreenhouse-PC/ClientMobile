package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.HashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;

import it.unibo.smartgh.data.greenhouse.GreenhouseRepository;
import it.unibo.smartgh.data.greenhouse.GreenhouseRepositoryImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.utility.Config;
import kotlin.Triple;

public class GreenhouseViewModelImpl extends AndroidViewModel implements GreenhouseViewModel {

    protected final GreenhouseRepository greenhouseRepository;
    private final MutableLiveData<Plant> plantLiveData;
    private final List<Triple<ParameterType, ParameterValue, String>> parameterList;
    private final MutableLiveData<List<Triple<ParameterType, ParameterValue, String>>> parametersLiveData;
    private final MutableLiveData<String> statusLiveData;

    protected final MutableLiveData<Map<ParameterType, ParameterValue>> parameterValueLiveData;
    protected final MutableLiveData<Map<ParameterType, String>> optimalValuesLiveData;

    public GreenhouseViewModelImpl(@NonNull Application application) {
        super(application);
        parameterValueLiveData = new MutableLiveData<>(initializeMap(new ParameterValueImpl()));
        optimalValuesLiveData = new MutableLiveData<>(initializeMap(""));
        plantLiveData = new MutableLiveData<>();
        parameterList = initializeList();
        parametersLiveData = new MutableLiveData<>(parameterList);
        statusLiveData = new MutableLiveData<>();
        Config config = ActivityUtilities.getConfig(application);
        greenhouseRepository = new GreenhouseRepositoryImpl(this, config.getHost(), config.getPort(), config.getSocketPort());
        greenhouseRepository.initializeData();
    }

    private List<Triple<ParameterType, ParameterValue, String>> initializeList() {
        List<Triple<ParameterType, ParameterValue, String>> list = new LinkedList<>();
        Arrays.stream(ParameterType.values()).forEach(p -> list.add(new Triple<>(p, new ParameterValueImpl(), "")));
        return list;
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
        this.parameterList.replaceAll(p -> {
            final String unit = plant.getUnitMap().get(p.component1().getName());
            double min = this.paramOptimalValue("Min", p.component1().getName(), plant);
            double max = this.paramOptimalValue("Max", p.component1().getName(), plant);
            final String optimalValue = min + " - " + max + " " + unit;
            return new Triple<>(p.component1(), p.component2(), optimalValue);
        });
        this.parametersLiveData.postValue(this.parameterList);
    }

    @Override
    public void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {
        // todo: togliere
        final String optimalValue = minBrightness + " " + unit + " - " + maxBrightness + " " + unit;
        Map<ParameterType, String> map = optimalValuesLiveData.getValue();
        if (map != null) {
            map.put(parameterType, minBrightness + " " + unit + " - " + maxBrightness + " " + unit);
        }
        optimalValuesLiveData.postValue(map);
    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        Map<ParameterType, ParameterValue> map = parameterValueLiveData.getValue();
        if (map != null) {
            map.put(parameter, parameterValue);
        }
        parameterValueLiveData.postValue(map);

        parameterList.replaceAll(p -> p.component1().equals(parameter) ?
                new Triple<>(p.component1(), parameterValue, p.component3()) : p);
        if (parameterList.stream().noneMatch(p -> p.component2().getValue() == null)) {
            parametersLiveData.postValue(parameterList);
            this.statusLiveData.postValue(this.parameterList.stream().anyMatch(p -> p.component2().getStatus().equals("alarm")) ? "ALLARME" : "NORMALE");
        }
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

    @Override
    public LiveData<List<Triple<ParameterType, ParameterValue, String>>> getParametersLiveData() {
        return parametersLiveData;
    }

    @Override
    public LiveData<String> getStatusLiveData() {
        return statusLiveData;
    }

    private Double paramOptimalValue(String type, String param, Plant plant){
        String paramName = param.substring(0, 1).toUpperCase() + param.substring(1);
        try {
            Class<?> c = Class.forName(Plant.class.getName());
            return (Double) c.getDeclaredMethod("get"+type+paramName).invoke(plant);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}
