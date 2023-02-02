package it.unibo.smartgh.utility;

/**
 * This class implements the public interface {@link Config}.
 */
public class ConfigImpl implements Config {

    private final String host;
    private final int port;
    private final int socketPort;
    private final int socketOperationPort;
    private final int socketModalityPort;

    /**
     * Default constructor for ConfigImpl.
     *  @param host       the host of the server
     * @param port       the port of the server
     * @param socketPort the port of the socket connection
     * @param socketOperationPort the port of the operation socket connection
     * @param socketModalityPort the port of the modality socket connection
     */
    public ConfigImpl(String host, int port, int socketPort, int socketOperationPort, int socketModalityPort) {
        this.host = host;
        this.port = port;
        this.socketPort = socketPort;
        this.socketOperationPort = socketOperationPort;
        this.socketModalityPort = socketModalityPort;
    }

    @Override
    public String getHost() {
        return host;
    }

    @Override
    public int getPort() {
        return port;
    }

    @Override
    public int getSocketPort() {
        return socketPort;
    }

    @Override
    public int getSocketOperationPort() {
        return socketOperationPort;
    }

    @Override
    public int getSocketModalityPort() {
        return socketModalityPort;
    }

}
