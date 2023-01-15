package it.unibo.smartgh.data.homepage;

public interface GreenhouseRemoteDataSource {

    void initializeData();

    void closeSocket();
}
