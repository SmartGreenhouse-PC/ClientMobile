package it.unibo.smartgh.data.greenhouse;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.LinkedList;
import java.util.List;
import java.util.Optional;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import it.unibo.smartgh.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.entity.plant.PlantParameter;
import it.unibo.smartgh.presentation.GsonUtils;

/**
 * Implementation of a greenhouse remote data source.
 */
public class GreenhouseRemoteDataSourceImpl implements GreenhouseRemoteDataSource {

    private static final String TAG = GreenhouseRemoteDataSourceImpl.class.getSimpleName();
    private final static String BASE_PATH = "/clientCommunication";
    private static final String GREENHOUSE_PATH = BASE_PATH + "/greenhouse";
    private static final String PARAMETER_PATH = BASE_PATH + "/parameter";
    private final int port;
    private final int socketPort;
    private final String host;
    private final GreenhouseRepository repository;
    private final Vertx vertx;
    private String id;
    private final Gson gson;
    private final int socketModalityPort;
    private GreenhouseImpl greenhouse;
    private Plant plant;
    private HttpClient server;
    private HttpClient modalitySocket;

    /**
     * Constructor of a {@link GreenhouseRemoteDataSource}.
     * @param host the host of the server
     * @param port the port of the server
     * @param socketPort the socket port
     * @param repository the greenhouse repository
     * @param socketModalityPort the modality socket port
     */
    public GreenhouseRemoteDataSourceImpl(String host, int port, int socketPort, GreenhouseRepository repository, int socketModalityPort) {
        this.host = host;
        this.port = port;
        this.socketPort = socketPort;
        this.vertx =  Vertx.vertx();
        this.repository = repository;
        this.gson = GsonUtils.createGson();
        this.socketModalityPort = socketModalityPort;
    }

    @Override
    public void initializeData() {
        this.updateView();
        this.setSocket();
    }

    @Override
    public void setGreenhouseId(String greenhouseId) {
        this.id = greenhouseId;
    }

    @Override
    public void initializeModalitySocket(){
        this.modalitySocket = vertx.createHttpClient();
        this.modalitySocket.webSocket(this.socketModalityPort, this.host, "/",
                wsC -> {
                    WebSocket ctx = wsC.result();
                    Log.i(TAG, "Connected to socket");
                    ctx.textMessageHandler(msg -> {
                        JsonObject json = new JsonObject(msg);
                        Log.i(TAG, msg);
                        if (json.getValue("greenhouseId").equals(this.id)) {
                            this.repository.updateModality(
                                    Modality.valueOf(json.getString("modality"))
                            );
                        }});
                });
    }

    @Override
    public void closeSocket() {
        this.server.close();
    }

    @Override
    public void putModality(Modality modality) {
        WebClient client = WebClient.create(vertx);
        client.post(port, host, GREENHOUSE_PATH + "/modality")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject()
                        .put("id", this.id)
                        .put("modality", modality.name())
                ).onSuccess(r -> this.repository.updateModality(modality));
    }

    @Override
    public void closeModalitySocket() {
        this.modalitySocket.close();
    }

    @Override
    public void getAllGreenhousesName() {
        WebClient client = WebClient.create(vertx);
        client.get(port, host, GREENHOUSE_PATH + "/all")
                .putHeader("Content-Type", "application/json")
                .send()
                .onSuccess(r -> {
                    JsonArray jsonArray = new JsonArray(r.body());
                    if (!jsonArray.isEmpty()) {
                       List<String> greenhousesName = new LinkedList<String>();
                       jsonArray.forEach(s -> greenhousesName.add(s.toString()));
                       System.out.println("GREENHOUSE DATA SOURCE: " + greenhousesName);
                       this.repository.updateGreenhousesName(greenhousesName);
                    }
                });
    }

    private void setSocket() {
        server = vertx.createHttpClient();
        server.webSocket(socketPort, this.host, "/",
                wsC -> {
                WebSocket ctx = wsC.result();
                Log.i(TAG, "Connected to socket");
                ctx.textMessageHandler(msg -> {
                JsonObject json = new JsonObject(msg);
                Log.i(TAG, msg);
                if (json.getValue("greenhouseId").equals(this.id)) {
                    Optional<ParameterType> parameter = ParameterType.parameterOf(json.getString("parameterName"));
                    parameter.ifPresent(parameterType -> {
                        final ParameterValue parameterValue = gson.fromJson(msg, ParameterValueImpl.class);
                        parameterValue.setUnit(plant.getParameters().get(ParameterType.parameterOf(parameter.get().getName()).get()).getUnit());
                        this.repository.updateParameterValue(parameterType, parameterValue);
                    });
            }});
        });
    }

    private void updateView() {
        WebClient client = WebClient.create(vertx);
        client.get(port, host, GREENHOUSE_PATH)
                .addQueryParam("id", id)
                .as(BodyCodec.string())
                .send()
                .onSuccess(res -> {
                    this.greenhouse = gson.fromJson(res.body(), GreenhouseImpl.class);
                    this.greenhouse.setId(this.id);
                    this.plant = greenhouse.getPlant();
                    this.repository.updatePlantInformation(this.plant);
                })
                .onFailure(Throwable::printStackTrace)
                .andThen(res ->
                        Arrays.stream(ParameterType.values()).forEach(p ->
                                client.get(port, host, PARAMETER_PATH)
                                        .addQueryParam("id", id)
                                        .addQueryParam("parameterName", p.getName())
                                        .as(BodyCodec.string())
                                        .send()
                                        .onSuccess(r -> {
                                            final ParameterValue value = gson.fromJson(r.body(), ParameterValueImpl.class);
                                            PlantParameter parameter = plant.getParameters().get(ParameterType.parameterOf(p.getName()).get());
                                            double min = parameter.getMin();
                                            double max = parameter.getMax();
                                            value.setStatus(value.getValue() < max && value.getValue() > min ? "normal" : "alarm");
                                            value.setUnit(plant.getParameters().get(ParameterType.parameterOf(p.getName()).get()).getUnit());
                                            this.repository.updateParameterValue(p, value);
                                            this.repository.updateModality(this.greenhouse.getActualModality());
                                        }).onFailure(Throwable::printStackTrace)));
    }
}