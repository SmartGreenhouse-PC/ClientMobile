package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import java.util.HashMap;
import java.util.Map;

import it.unibo.smartgh.data.operation.OperationRepository;
import it.unibo.smartgh.data.operation.OperationRepositoryImpl;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.utility.ActivityUtilities;
import it.unibo.smartgh.utility.Config;

public class OperationViewModelImpl extends AndroidViewModel implements OperationViewModel{
    private final OperationRepository operationRepository;
    private final MutableLiveData<Map<ParameterType, Operation>> operationsLiveData;

    public OperationViewModelImpl(@NonNull Application application) {
        super(application);
        Config config = ActivityUtilities.getConfig(application);
        this.operationRepository = new OperationRepositoryImpl(this, config.getHost(), config.getPort());
        this.operationsLiveData = new MutableLiveData<>(this.initializeMap(new OperationImpl()));
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
    public void sendNewOperation(String parameter, String action) {
        this.operationRepository.sendNewOperation(parameter,action);
    }

    @Override
    public void getLastParameterOperation(String parameter) {
        this.operationRepository.getLastParameterOperation(parameter);
    }

    @Override
    public LiveData<Map<ParameterType, Operation>> getAllLastOperationsParameter() {
        return this.operationsLiveData;
    }

    @Override
    public void updateParameterOperation(ParameterType parameter, Operation operation) {
        System.out.println("update operations  value");
        Map<ParameterType, Operation> map = operationsLiveData.getValue();
        if (map != null) {
            map.put(parameter, operation);
        }
        operationsLiveData.postValue(map);
    }
}
