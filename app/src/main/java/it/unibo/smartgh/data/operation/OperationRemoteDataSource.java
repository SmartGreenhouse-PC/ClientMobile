package it.unibo.smartgh.data.operation;


import io.vertx.core.Future;
import it.unibo.smartgh.entity.operation.Operation;

public interface OperationRemoteDataSource {
    /**
     * Send a new operation to server.
     * @param operation the new operation performed.
     */
    void sendNewOperation(Operation operation);

    /**
     * Get the last operation performed on a parameter.
     * @param parameter on which the last operation is performed
     * @param id greenhouse id
     * @return the operation
     */
    Future<Operation> getLastParameterOperation(String parameter, String id);
}
