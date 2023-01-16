package it.unibo.smartgh.data.operation;

import java.util.Date;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.viewmodel.OperationViewModel;

public class OperationRepositoryImpl implements OperationRepository{
    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";
    private final OperationRemoteDataSource operationRemoteDataSource;
    private final OperationViewModel viewModel;

    public OperationRepositoryImpl(OperationViewModel viewModel) {
        this.operationRemoteDataSource = new OperationRemoteDataSourceImpl(this);
        this.viewModel = viewModel;
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
}
