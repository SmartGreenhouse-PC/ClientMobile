package it.unibo.smartgh.data.greenhouse;

public interface GreenhouseRemoteDataSource {

    void initializeData();

    //todo add method put modality

    void closeSocket();
}
