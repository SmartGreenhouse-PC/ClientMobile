package it.unibo.smartgh.data.operation;

import io.vertx.core.Future;
import it.unibo.smartgh.entity.operation.Operation;

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
    Future<Operation> getLastParameterOperation(String parameter);
}
