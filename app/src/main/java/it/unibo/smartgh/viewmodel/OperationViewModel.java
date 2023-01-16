package it.unibo.smartgh.viewmodel;

import io.vertx.core.Future;
import it.unibo.smartgh.entity.operation.Operation;

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
     *                  TODO change return
     */
    void getLastParameterOperation(String parameter);
}
