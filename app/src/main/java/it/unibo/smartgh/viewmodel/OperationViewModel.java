package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.Map;

import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;

public interface OperationViewModel {
    /**
     * Send a new operation to server.
     * @param parameter on which is performed the new operation.
     * @param action performed.
     */
    void sendNewOperation(String parameter, String action);

    /**
     * Get the last operation performed on a parameter.
     * @param parameter on which the last operation is performed
     */
    void getLastParameterOperation(String parameter);

    LiveData<Map<ParameterType, Operation>> getAllLastOperationsParameter();

    void updateParameterOperation(ParameterType parameter, Operation operation);
}
