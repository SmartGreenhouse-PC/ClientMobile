package it.unibo.smartgh.utility;

/**
 * Interface to manage the sever configuration.
 */
public interface Config {

    /**
     * Returns the server host.
     * @return the server host
     */
    String getHost();

    /**
     * Returns the server port.
     * @return the server port
     */
    int getPort();

    /**
     * Returns the socket port.
     * @return the socket port
     */
    int getSocketPort();

    /**
     * Returns the socket operation port.
     * @return the socket operation port
     */
    int getSocketOperationPort();

    /**
     * Returns the socket modality port.
     * @return the socket modality port
     */
    int getSocketModalityPort();

}