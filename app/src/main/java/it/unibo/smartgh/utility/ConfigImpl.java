package it.unibo.smartgh.utility;

/**
 * This class implements the public interface {@link Config}.
 */
public class ConfigImpl implements Config {

    private final String host;
    private final int port;

    /**
     * Default constructor for ConfigImpl.
     * @param host the host of the server
     * @param port the port of the server
     */
    public ConfigImpl(String host, int port) {
        this.host = host;
        this.port = port;
    }

    @Override
    public String buildUrl() {
        return "http://" + this.host + ":" + this.port + "/";
    }
}
