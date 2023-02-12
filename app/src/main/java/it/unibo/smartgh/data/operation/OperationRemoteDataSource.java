package it.unibo.smartgh.data.operation;

import it.unibo.smartgh.entity.operation.Operation;

/**
 * Interface that represents the remote data source of operations.
 */
public interface OperationRemoteDataSource {

    /**
     * Initialize the socket.
     */
    void initialize();

    /**
     * Set the greenhouse id.
     * @param greenhouseId the greenhouse id.
     */
    void setGreenhouseId(String greenhouseId);

    /**
     * Send a new operation to server.
     * @param operation the new operation performed.
     */
    void sendNewOperation(Operation operation);

    /**
     * Get the last operation performed on a parameter.
     * @param parameter on which the last operation is performed
     * @param id greenhouse id
     */
    void getLastParameterOperation(String parameter, String id);

    /**
     * Close the operation socket.
     */
    void closeSocket();
}
