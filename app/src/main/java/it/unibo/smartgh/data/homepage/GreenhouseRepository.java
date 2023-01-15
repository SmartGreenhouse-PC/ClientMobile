package it.unibo.smartgh.data.homepage;

import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;

public class GreenhouseRepository {

    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";
    private final GreenhouseRemoteDataSource greenhouseRemoteDataSource;
    private final GreenhouseViewModel viewModel;

    public GreenhouseRepository(GreenhouseViewModel viewModel) {
        this.viewModel = viewModel;
        this.greenhouseRemoteDataSource = new GreenhouseRemoteDataSourceImpl(this, GREENHOUSE_ID);
    }

    public void initializeData() {
        this.greenhouseRemoteDataSource.initializeData();
    }

    public void updatePlantInformation(String name, String description, String plantImg) {
        this.viewModel.updatePlantInformation(name, description, plantImg);
    }

    public void updateParameterOptimalValues(ParameterType parameterType, Double minBrightness, Double maxBrightness, String unit) {
        this.viewModel.updateParameterInfo(parameterType, minBrightness, maxBrightness, unit);
    }

    public void updateParameterValue(ParameterType parameter, ParameterValue parameterValue) {
        this.viewModel.updateParameterValue(parameter, parameterValue);
    }
}