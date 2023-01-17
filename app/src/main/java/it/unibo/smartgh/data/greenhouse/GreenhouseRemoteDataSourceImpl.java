package it.unibo.smartgh.data.greenhouse;

import android.util.Log;

import com.google.gson.Gson;

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
import it.unibo.smartgh.entity.greenhouse.Modality;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.entity.parameter.ParameterValue;
import it.unibo.smartgh.entity.parameter.ParameterValueImpl;
import it.unibo.smartgh.entity.plant.Plant;
import it.unibo.smartgh.presentation.GsonUtils;

public class GreenhouseRemoteDataSourceImpl implements GreenhouseRemoteDataSource {

    private static final String TAG = GreenhouseRemoteDataSourceImpl.class.getSimpleName();
    private static final int PORT = 8890;
    private final static int SOCKET_PORT = 1234;
    private static final String HOST = "192.168.3.232";
    private final static String SOCKET_HOST = "192.168.3.232";
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

    @Override
    public void putModality(String greenhouseId, Modality modality) {
        WebClient client = WebClient.create(vertx);
        client.post(PORT, HOST, GREENHOUSE_PATH + "/modality")
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject()
                        .put("id", greenhouseId)
                        .put("modality", modality.name())
                );
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
                    this.unit = plant.getUnitMap();
                    this.repository.updateModality(this.greenhouse.getActualModality());
                    this.repository.updatePlantInformation(this.plant);
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
                                            value.setStatus(inRange ? "normal" : "alarm");
                                            this.repository.updateParameterValue(p, value);
                                        })));
    }
}