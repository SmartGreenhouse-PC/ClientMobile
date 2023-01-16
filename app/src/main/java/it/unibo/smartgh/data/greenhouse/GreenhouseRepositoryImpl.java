package it.unibo.smartgh.data.greenhouse;

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
    public void updateParameterOptimalValues(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {
        this.viewModel.updateParameterInfo(parameterType, minBrightness, maxBrightness, unit);
    }

    @Override
    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        this.viewModel.updateParameterValue(parameter, parameterValue);
    }

    //todo add metodo put modality che richiama RemoteDataSource
}
