package it.unibo.smartgh.data.operation;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonArray;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import io.vertx.ext.web.codec.BodyCodec;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.entity.operation.OperationImpl;
import it.unibo.smartgh.entity.parameter.ParameterType;
import it.unibo.smartgh.presentation.GsonUtils;

/**
 * Implementation of {@link OperationRemoteDataSource} interface.
 */
public class OperationRemoteDataSourceImpl implements OperationRemoteDataSource {
    private final int port;
    private final String host;
    private final static String BASE_PATH = "/clientCommunication/operations";
    private final Vertx vertx;
    private final Gson gson;
    private final OperationRepository operationRepository;

    public OperationRemoteDataSourceImpl(OperationRepositoryImpl operationRepository, String host, int port) {
        this.host = host;
        this.port = port;
        this.vertx = Vertx.vertx();
        this.gson = GsonUtils.createGson();
        this.operationRepository = operationRepository;
    }

    @Override
    public void sendNewOperation(Operation operation) {
        WebClient client = WebClient.create(vertx);
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        client.post(port, host, BASE_PATH)
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject()
                        .put("greenhouseId", operation.getGreenhouseId())
                        .put("modality", operation.getModality().name())
                        .put("date", formatter.format(operation.getDate()))
                        .put("parameter", operation.getParameter())
                        .put("action", operation.getAction())
                );
    }

    @Override
    public void getLastParameterOperation(String parameter, String id) {
        WebClient client = WebClient.create(vertx);
        client.get(port, host, BASE_PATH + "/parameter")
                .addQueryParam("id", id)
                .addQueryParam("parameterName", parameter)
                .addQueryParam("limit", String.valueOf(1))
                .as(BodyCodec.string())
                .send()
                .onSuccess(r -> {
                    JsonArray jsonArray = new JsonArray(r.body());
                    if (!jsonArray.isEmpty()) {
                        Operation operation = gson.fromJson(jsonArray.getJsonObject(0).toString(), OperationImpl.class);
                        operationRepository.updateParameterOperation(ParameterType.parameterOf(operation.getParameter()).get(), operation);
                    }
                });
    }
}
