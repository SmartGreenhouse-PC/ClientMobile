package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import it.unibo.smartgh.data.homepage.GreenhouseRepository;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;

public class HomepageViewModel extends AndroidViewModel {

    private final GreenhouseRepository greenhouseRepository;
//    private final MutableLiveData<Map<ParameterType, ParameterValue>> parameterValueLiveData;
    private final MutableLiveData<ParameterValue> parameterValueLiveData;

    public HomepageViewModel(@NonNull Application application) {
        super(application);
        parameterValueLiveData = new MutableLiveData<>();
        greenhouseRepository = new GreenhouseRepository(this);
    }

    public void updatePlantInformation(String name, String description, String plantImg) {
    }

    public void updateParameterInfo(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {

    }

    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        parameterValueLiveData.setValue(parameterValue);
    }
}
