package it.unibo.smartgh.data.operation;

import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;

public interface OperationRepository {
    /**
     * Send a new operation to server.
     * @param parameter on which is performed the new operation.
     * @param action performed.
     */
    void sendNewOperation(String parameter, String action);

    /**
     * Get the last operation performed on a parameter.
     * @param parameter on which the last operation is performed
     * @return the operation
     */
    void getLastParameterOperation(String parameter);

    void updateParameterOperation(ParameterType parameter, Operation operation);
}
