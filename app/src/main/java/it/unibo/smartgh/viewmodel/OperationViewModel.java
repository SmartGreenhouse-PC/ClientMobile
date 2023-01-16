package it.unibo.smartgh.viewmodel;

public interface OperationViewModel {
    /**
     * Send a new operation to server.
     * @param parameter on which is performed the new operation.
     * @param action performed.
     */
    void sendNewOperation(String parameter, String action);
}
