package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import it.unibo.smartgh.data.operation.OperationRepository;
import it.unibo.smartgh.data.operation.OperationRepositoryImpl;

public class OperationViewModelImpl extends AndroidViewModel implements OperationViewModel{
    private final OperationRepository operationRepository;

    public OperationViewModelImpl(@NonNull Application application) {
        super(application);
        this.operationRepository = new OperationRepositoryImpl(this);
    }

    @Override
    public void sendNewOperation(String parameter, String action) {
        this.operationRepository.sendNewOperation(parameter,action);
    }

    @Override
    public void getLastParameterOperation(String parameter) {
        this.operationRepository.getLastParameterOperation(parameter).onSuccess(operation -> {
            //Update view
        });
    }
}
