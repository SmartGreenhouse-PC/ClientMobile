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
     * Build the sever url.
     * @return the url of the server
     */
    String buildUrl();
}