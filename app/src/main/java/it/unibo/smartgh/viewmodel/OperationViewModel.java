package it.unibo.smartgh.viewmodel;

import androidx.lifecycle.LiveData;

import java.util.Map;

import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;

/**
 * An interface that represents the operation view model.
 */
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

    /**
     * Gets all last operations parameter.
     * @return all last operations for parameter
     */
    LiveData<Map<ParameterType, Operation>> getAllLastOperationsParameter();

    /**
     * Update the parameter operation.
     * @param parameter the type of parameter
     * @param operation the parameter operation
     */
    void updateParameterOperation(ParameterType parameter, Operation operation);

}
