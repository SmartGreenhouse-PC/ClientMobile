package it.unibo.smartgh.data.homepage;

import android.util.Log;

import com.google.gson.Gson;

import java.util.Arrays;
import java.util.Date;
import java.util.Map;
import java.util.Optional;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpServer;
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

    private static final int PORT = 8890;
    private final static int SOCKET_PORT = 1234;
    private static final String HOST = "192.168.0.108";
    private final static String SOCKET_HOST = "localhost";
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
    private HttpServer server;

    public GreenhouseRemoteDataSourceImpl(GreenhouseRepository repository, String id) {
        this.vertx =  Vertx.vertx();
        this.repository = repository;
        this.id = id;
        this.gson = GsonUtils.createGson();
    }

    @Override
    public void initializeData() {
        Log.e("DS", "initializeData");
        this.updateView();
        this.setSocket();
    }

    @Override
    public void closeSocket() {
        this.server.close();
    }

    private void setSocket() {
        Log.e("DS", "setSocket");
        server = vertx.createHttpServer();
        server.webSocketHandler(ctx -> ctx.textMessageHandler(msg -> {
            JsonObject json = new JsonObject(msg);
            if (json.getValue("greenhouseId").equals(this.id)) {
                Log.e("GreenhouseRemoteDataSourceImpl", "Received " + msg);
                Optional<ParameterType> parameter = ParameterType.parameterOf(json.getString("parameterName"));
                parameter.ifPresent(parameterType -> this.repository.updateParameterValue(parameterType,
                        new ParameterValueImpl(this.id, new Date(), json.getDouble("value"))));
            }
        })).listen(SOCKET_PORT, SOCKET_HOST)
                .onSuccess(res -> Log.e("DS", "Connected"))
                .onFailure(e -> {
                    Log.e("DS", "FAILURE " + e.getMessage());
            e.printStackTrace();
        });
    }

    private void updateView() {
        Log.e("DS", "updateView");
        WebClient client = WebClient.create(vertx);
        client.get(PORT, HOST, GREENHOUSE_PATH)
                .addQueryParam("id", id)
                .as(BodyCodec.string())
                .send()
                .onSuccess(res -> {
                    Log.e("GR", "res: " + res.body());
                    this.greenhouse = gson.fromJson(res.body(), GreenhouseImpl.class);
                    this.greenhouse.setId(this.id);
                    this.plant = greenhouse.getPlant();
                    this.unit = plant.getUnitMap();
                    this.repository.updatePlantInformation(plant.getName(), plant.getDescription(), plant.getImg());
                    this.repository.updateParameterOptimalValues(ParameterType.BRIGHTNESS, plant.getMinBrightness(),
                            plant.getMaxBrightness(), this.unit.get(ParameterType.BRIGHTNESS.getName()));
                    this.repository.updateParameterOptimalValues(ParameterType.SOIL_MOISTURE, plant.getMinSoilMoisture(),
                            plant.getMaxSoilMoisture(), this.unit.get(ParameterType.SOIL_MOISTURE.getName()));
                    this.repository.updateParameterOptimalValues(ParameterType.HUMIDITY, plant.getMinHumidity(), plant.getMaxHumidity(),
                            this.unit.get(ParameterType.HUMIDITY.getName()));
                    this.repository.updateParameterOptimalValues(ParameterType.TEMPERATURE, plant.getMinTemperature(),
                            plant.getMaxTemperature(), this.unit.get(ParameterType.TEMPERATURE.getName()));
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
                                            boolean inRange = true;
                                            switch (p) {
                                                case BRIGHTNESS: {
                                                    inRange = value.getValue() < this.plant.getMaxBrightness() && value.getValue() > this.plant.getMinBrightness();
                                                    break;
                                                }
                                                case SOIL_MOISTURE: {
                                                    inRange = value.getValue() < this.plant.getMaxSoilMoisture() && value.getValue() > this.plant.getMinSoilMoisture();
                                                    break;
                                                }
                                                case HUMIDITY: {
                                                    inRange = value.getValue() < this.plant.getMaxHumidity() && value.getValue() > this.plant.getMinHumidity();
                                                    break;
                                                }
                                                case TEMPERATURE: {
                                                    inRange = value.getValue() < this.plant.getMaxTemperature() && value.getValue() > this.plant.getMinTemperature();
                                                    break;
                                                }
                                            }
                                            this.repository.updateParameterValue(p, value);
                                        })));
    }
}