package it.unibo.smartgh.data.greenhouse;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;

public class GreenhouseRepositoryImpl implements GreenhouseRepository {

    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";
    private final GreenhouseRemoteDataSource greenhouseRemoteDataSource;
    private final GreenhouseViewModel viewModel;

    public GreenhouseRepositoryImpl(GreenhouseViewModel viewModel) {
        this.viewModel = viewModel;
        this.greenhouseRemoteDataSource = new GreenhouseRemoteDataSourceImpl(this, GREENHOUSE_ID);
    }

    @Override
    public void initializeData() {
        this.greenhouseRemoteDataSource.initializeData();
    }

    @Override
    public void updatePlantInformation(Plant plant) {
        this.viewModel.updatePlantInformation(plant);
    }

    @Override
    public void updateParameterOptimalValues(ParameterType parameterType, Double min, Double max, String unit) {
        this.viewModel.updateParameterInfo(parameterType, min, max, unit);
    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        this.viewModel.updateParameterValue(parameter, parameterValue);
    }

    @Override
    public void changeModality(Modality modality) {
        this.greenhouseRemoteDataSource.putModality(GREENHOUSE_ID, modality);
    }

    @Override
    public void updateModality(Modality actualModality) {
        this.viewModel.updateModality(actualModality);
    }
}
