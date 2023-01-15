package it.unibo.smartgh.viewmodel;

import android.app.Application;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;

import it.unibo.smartgh.data.homepage.GreenhouseRepository;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;

public class ManualControlViewModel extends GreenhouseViewModelImpl {

//    private final OperationRepository
    public ManualControlViewModel(@NonNull Application application) {
        super(application);
    }

}
