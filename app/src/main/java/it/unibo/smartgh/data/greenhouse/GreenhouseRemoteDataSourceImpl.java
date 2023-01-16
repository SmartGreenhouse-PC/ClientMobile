package it.unibo.smartgh.data.greenhouse;

import android.util.Log;

import com.google.gson.Gson;

import java.lang.reflect.InvocationTargetException;
import java.util.Arrays;
import java.util.Map;
import java.util.Optional;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import it.unibo.smartgh.entity.greenhouse.GreenhouseImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.presentation.GsonUtils;

public class GreenhouseRemoteDataSourceImpl implements GreenhouseRemoteDataSource {

    private static final String TAG = GreenhouseRemoteDataSourceImpl.class.getSimpleName();
    private static final int PORT = 8890;
    private final static int SOCKET_PORT = 1234;
    private static final String HOST = "192.168.0.108";
    private final static String SOCKET_HOST = "192.168.0.108";
    private final static String BASE_PATH = "/clientCommunication";
    private static final String GREENHOUSE_PATH = BASE_PATH + "/greenhouse";
    private static final String PARAMETER_PATH = BASE_PATH + "/parameter";
    private final GreenhouseRepository repository;
    private final Vertx vertx;
    private final String id;
    private final Gson gson;
    private GreenhouseImpl greenhouse;
    private Plant plant;
    private Map<String, String> unit;
    private HttpClient server;

    public GreenhouseRemoteDataSourceImpl(GreenhouseRepository repository, String id) {
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

    private void setSocket() {
        server = vertx.createHttpClient();
        server.webSocket(SOCKET_PORT, SOCKET_HOST, "/",
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
                        this.repository.updateParameterValue(parameterType, parameterValue);
                    });
            }});
        });
    }

    private void updateView() {
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, GREENHOUSE_PATH)
                .addQueryParam("id", id)
                .as(BodyCodec.string())
                .send()
                .onSuccess(res -> {
                    this.greenhouse = gson.fromJson(res.body(), GreenhouseImpl.class);
                    this.greenhouse.setId(this.id);
                    this.plant = greenhouse.getPlant();
                    this.repository.updatePlantInformation(this.plant);
//                    this.unit = plant.getUnitMap();
//                    this.repository.updateParameterOptimalValues(ParameterType.BRIGHTNESS, plant.getMinBrightness(),
//                            plant.getMaxBrightness(), this.unit.get(ParameterType.BRIGHTNESS.getName()));
//                    this.repository.updateParameterOptimalValues(ParameterType.SOIL_MOISTURE, plant.getMinSoilMoisture(),
//                            plant.getMaxSoilMoisture(), this.unit.get(ParameterType.SOIL_MOISTURE.getName()));
//                    this.repository.updateParameterOptimalValues(ParameterType.HUMIDITY, plant.getMinHumidity(), plant.getMaxHumidity(),
//                            this.unit.get(ParameterType.HUMIDITY.getName()));
//                    this.repository.updateParameterOptimalValues(ParameterType.TEMPERATURE, plant.getMinTemperature(),
//                            plant.getMaxTemperature(), this.unit.get(ParameterType.TEMPERATURE.getName()));
                })
                .onFailure(Throwable::printStackTrace)
                .andThen(res ->
                        Arrays.stream(ParameterType.values()).forEach(p ->
                                client.get(PORT, HOST, PARAMETER_PATH)
                                        .addQueryParam("id", id)
                                        .addQueryParam("parameterName", p.getName())
                                        .as(BodyCodec.string())
                                        .send()
                                        .onSuccess(r -> {
                                            final ParameterValue value = gson.fromJson(r.body(), ParameterValueImpl.class);
                                            double min = this.paramOptimalValue("Min", p.getName(), plant);
                                            double max = this.paramOptimalValue("Max", p.getName(), plant);
                                            value.setStatus(value.getValue() < max && value.getValue() > min ? "normal" : "alarm");
                                            this.repository.updateParameterValue(p, value);
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