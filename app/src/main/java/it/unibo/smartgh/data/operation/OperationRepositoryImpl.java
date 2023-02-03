package it.unibo.smartgh.data.operation;

import java.util.Date;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.viewmodel.OperationViewModel;

/**
 * Implementation of {@link OperationRepository} interface.
 */
public class OperationRepositoryImpl implements OperationRepository{
    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fc";
    private final OperationRemoteDataSource operationRemoteDataSource;
    private final OperationViewModel viewModel;

    /**
     * Constructor of {@link OperationRepositoryImpl}.
     * @param viewModel the operation view model
     * @param host the host of the server
     * @param port the port of the server
     * @param socketOperationPort the port of the operation socket
     */
    public OperationRepositoryImpl(OperationViewModel viewModel, String host, int port, int socketOperationPort) {
        this.operationRemoteDataSource = new OperationRemoteDataSourceImpl(this, GREENHOUSE_ID, host, port, socketOperationPort);
        this.viewModel = viewModel;
    }

    @Override
    public void initialize() {
        this.operationRemoteDataSource.initialize();
    }

    @Override
    public void sendNewOperation(String parameter, String action) {
        Operation operation = new OperationImpl(
                GREENHOUSE_ID,
                Modality.MANUAL,
                new Date(),
                parameter,
                action
        );
        this.operationRemoteDataSource.sendNewOperation(operation);
    }

    @Override
    public void getLastParameterOperation(String parameter) {
        this.operationRemoteDataSource.getLastParameterOperation(parameter, GREENHOUSE_ID);
    }

    @Override
    public void updateParameterOperation(ParameterType parameter, Operation operation) {
        this.viewModel.updateParameterOperation(parameter, operation);
    }

    @Override
    public void closeSocket() {
        this.operationRemoteDataSource.closeSocket();
    }
}
