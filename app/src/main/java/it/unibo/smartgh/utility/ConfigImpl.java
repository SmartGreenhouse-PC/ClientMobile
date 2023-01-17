package it.unibo.smartgh.utility;

/**
 * This class implements the public interface {@link Config}.
 */
public class ConfigImpl implements Config {

    private final String host;
    private final int port;
    private final int socketPort;

    /**
     * Default constructor for ConfigImpl.
     *
     * @param host       the host of the server
     * @param port       the port of the server
     * @param socketPort the port of the socket connection
     */
    public ConfigImpl(String host, int port, int socketPort) {
        this.host = host;
        this.port = port;
        this.socketPort = socketPort;
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
    public String buildUrl() {
        return "http://" + this.host + ":" + this.port + "/";
    }
}
