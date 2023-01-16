package it.unibo.smartgh.data.operation;

import it.unibo.smartgh.entity.operation.Operation;

public interface OperationRemoteDataSource {
    /**
     * Send a new operation to server.
     * @param operation the new operation performed.
     */
    void sendNewOperation(Operation operation);
}
