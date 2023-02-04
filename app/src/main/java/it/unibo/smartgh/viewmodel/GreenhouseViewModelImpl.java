package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;

import it.unibo.smartgh.data.greenhouse.GreenhouseRepository;
import it.unibo.smartgh.data.greenhouse.GreenhouseRepositoryImpl;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.entity.plant.PlantParameter;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.utility.Config;
import kotlin.Triple;

/**
 * An implementation of greenhouse view model.
 */
public class GreenhouseViewModelImpl extends AndroidViewModel implements GreenhouseViewModel {

    protected final GreenhouseRepository greenhouseRepository;
    private final MutableLiveData<Plant> plantLiveData;
    private final List<Triple<ParameterType, ParameterValue, String>> parameterList;
    private final MutableLiveData<List<Triple<ParameterType, ParameterValue, String>>> parametersLiveData;
    private final MutableLiveData<String> statusLiveData;
    private final MutableLiveData<Modality> modalityLiveData;

    /**
     * Constructor of {@link GreenhouseViewModelImpl}.
     * @param application the current application
     */
    public GreenhouseViewModelImpl(@NonNull Application application) {
        super(application);
        plantLiveData = new MutableLiveData<>();
        parameterList = initializeList();
        parametersLiveData = new MutableLiveData<>(parameterList);
        statusLiveData = new MutableLiveData<>();
        modalityLiveData = new MutableLiveData<>();
        Config config = ActivityUtilities.getConfig(application);
        greenhouseRepository = new GreenhouseRepositoryImpl(this, config.getHost(), config.getPort(), config.getSocketPort(), config.getSocketModalityPort());
        greenhouseRepository.initializeData();
    }

    private List<Triple<ParameterType, ParameterValue, String>> initializeList() {
        List<Triple<ParameterType, ParameterValue, String>> list = new LinkedList<>();
        Arrays.stream(ParameterType.values()).forEach(p -> list.add(new Triple<>(p, new ParameterValueImpl(), "")));
        return list;
    }

    @Override
    public void updatePlantInformation(Plant plant) {
        this.plantLiveData.postValue(plant);
        this.parameterList.replaceAll(p -> {
            final ParameterType type = ParameterType.parameterOf(p.component1().getName()).get();
            if (plant.getParameters().containsKey(type)) {
                final PlantParameter plantParameter = plant.getParameters().get(type);
                final String unit = plantParameter.getUnit();
                double min = plantParameter.getMin();
                double max = plantParameter.getMax();
                final String optimalValue = min + " - " + max + " " + unit;
                return new Triple<>(p.component1(), p.component2(), optimalValue);
            } else {
                return p;
            }
        });
        this.parametersLiveData.postValue(this.parameterList);
    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        parameterList.replaceAll(p -> p.component1().equals(parameter) ?
                new Triple<>(p.component1(), parameterValue, p.component3()) : p);
        if (parameterList.stream().noneMatch(p -> p.component2().getValue() == null)) {
            parametersLiveData.postValue(parameterList);
            this.statusLiveData.postValue(this.parameterList.stream().anyMatch(p -> p.component2().getStatus().equals("alarm")) ? "ALLARME" : "NORMALE");
        }
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

    @Override
    public void changeModality(Modality modality) {
        this.greenhouseRepository.changeModality(modality);
    }

    @Override
    public void updateModality(Modality actualModality) {
        this.modalityLiveData.postValue(actualModality);
    }

    @Override
    public LiveData<Modality> getModalityLiveData() {
        return this.modalityLiveData;
    }

    @Override
    public void closeModalitySocket() {
        this.greenhouseRepository.closeModalitySocket();
    }

    @Override
    public void initializeModalitySocket() {
        this.greenhouseRepository.initializeModalitySocket();
    }
}
