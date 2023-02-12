package it.unibo.smartgh.data.operation;

import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.parameter.ParameterType;

/**
 * This interface represents the repository of operation.
 */
public interface OperationRepository {
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
     * Update the parameter operation.
     * @param parameter the parameter type
     * @param operation the parameter operation
     */
    void updateParameterOperation(ParameterType parameter, Operation operation);

    /**
     * Close the operation socket.
     */
    void closeSocket();
}
