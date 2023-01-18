package it.unibo.smartgh.data.greenhouse;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Optional;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import it.unibo.smartgh.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
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
    private final String id;
    private final Gson gson;
    private GreenhouseImpl greenhouse;
    private Plant plant;
    private HttpClient server;

    /**
     * Constructor of a {@link GreenhouseRemoteDataSource}.
     * @param host the host of the server
     * @param port the port of the server
     * @param socketPort the socket port
     * @param id the greenhouse id
     * @param repository the greenhouse repository
     */
    public GreenhouseRemoteDataSourceImpl(String host, int port, int socketPort, String id, GreenhouseRepository repository) {
        this.host = host;
        this.port = port;
        this.socketPort = socketPort;
        this.vertx =  Vertx.vertx();
        this.repository = repository;
        this.id = id;
        this.gson = GsonUtils.createGson();
    }

    @Override
    public void initializeData() {
        this.updateView();
        this.setSocket();
    }

    @Override
    public void closeSocket() {
        this.server.close();
    }

    @Override
    public void putModality(String greenhouseId, Modality modality) {
        WebClient client = WebClient.create(vertx);
        client.post(port, host, GREENHOUSE_PATH + "/modality")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject()
                        .put("id", greenhouseId)
                        .put("modality", modality.name())
                ).onSuccess(r -> this.repository.updateModality(modality));
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
                        parameterValue.setUnit(plant.getUnitMap().get(parameter.get().getName()));
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
                                            double min = this.paramOptimalValue("Min", p.getName(), plant);
                                            double max = this.paramOptimalValue("Max", p.getName(), plant);
                                            value.setStatus(value.getValue() < max && value.getValue() > min ? "normal" : "alarm");
                                            value.setUnit(plant.getUnitMap().get(p.getName()));
                                            this.repository.updateParameterValue(p, value);
                                            this.repository.updateModality(this.greenhouse.getActualModality());
                                        }).onFailure(Throwable::printStackTrace)));
    }

    private Double paramOptimalValue(String type, String param, Plant plant){
        String paramName = param.substring(0, 1).toUpperCase() + param.substring(1);
        try {
            Class<?> c = Class.forName(Plant.class.getName());
            return (Double) c.getDeclaredMethod("get"+type+paramName).invoke(plant);
        } catch (ClassNotFoundException | NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
            throw new RuntimeException(e);
        }
    }
}