package it.unibo.smartgh.data.greenhouse;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.viewmodel.GreenhouseViewModel;

/**
 * Implementation of {@link GreenhouseRepository} interface.
 */
public class GreenhouseRepositoryImpl implements GreenhouseRepository {

    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fc";
    private final GreenhouseRemoteDataSource greenhouseRemoteDataSource;
    private final GreenhouseViewModel viewModel;

    /**
     * Constructor of {@link GreenhouseRepositoryImpl}.
     * @param viewModel the view model instance
     * @param host the host of the server
     * @param port the port of the server
     * @param socketPort the socket port
     */
    public GreenhouseRepositoryImpl(GreenhouseViewModel viewModel, String host, int port, int socketPort, int socketModalityPort) {
        this.viewModel = viewModel;
        this.greenhouseRemoteDataSource = new GreenhouseRemoteDataSourceImpl(host, port, socketPort, GREENHOUSE_ID, this, socketModalityPort);
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

    @Override
    public void closeModalitySocket() {
        this.greenhouseRemoteDataSource.closeModalitySocket();
    }

    @Override
    public void initializeModalitySocket() {
        this.greenhouseRemoteDataSource.initializeModalitySocket();
    }
}
