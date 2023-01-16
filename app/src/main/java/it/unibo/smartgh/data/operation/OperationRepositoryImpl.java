package it.unibo.smartgh.data.operation;

import java.util.Date;

import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;

public class OperationRepositoryImpl implements OperationRepository{
    private static final String GREENHOUSE_ID = "63af0ae025d55e9840cbc1fa";
    private final OperationRemoteDataSource operationRemoteDataSource;

    public OperationRepositoryImpl(OperationRemoteDataSource operationRemoteDataSource) {
        this.operationRemoteDataSource = operationRemoteDataSource;
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
}
