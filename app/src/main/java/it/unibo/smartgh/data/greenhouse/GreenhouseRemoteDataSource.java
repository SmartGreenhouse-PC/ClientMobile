package it.unibo.smartgh.data.greenhouse;

public interface GreenhouseRemoteDataSource {

    void initializeData();

    void closeSocket();
}
