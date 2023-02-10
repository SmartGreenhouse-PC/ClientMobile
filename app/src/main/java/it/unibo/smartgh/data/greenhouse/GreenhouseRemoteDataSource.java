package it.unibo.smartgh.data.greenhouse;

import it.unibo.smartgh.entity.greenhouse.Modality;

/**
 * This interface represents the remote data source for the greenhouse.
 */
public interface GreenhouseRemoteDataSource {

    /**
     * Retrieve the greenhouse data and initialize the view.
     */
    void initializeData();

    void setGreenhouseId(String greenhouseId);

    /**
     * Initialize the modality socket.
     */
    void initializeModalitySocket();

    /**
     * Close the socket.
     */
    void closeSocket();

    /**
     * Update the greenhouse management modality.
     * @param modality new modality.
     */
    void putModality(Modality modality);

    /**
     * Close the modality socket.
     */
    void closeModalitySocket();

    void getAllGreenhousesName();
}
