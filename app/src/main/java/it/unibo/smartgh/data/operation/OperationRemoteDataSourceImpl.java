package it.unibo.smartgh.data.operation;

import android.annotation.SuppressLint;
import android.util.Log;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.vertx.core.Vertx;
import io.vertx.core.http.HttpClient;
import io.vertx.core.http.WebSocket;
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
    private static final String TAG = OperationRemoteDataSourceImpl.class.getSimpleName();
    private final int port;
    private final String host;
    private final static String BASE_PATH = "/clientCommunication/operations";
    private final Vertx vertx;
    private final Gson gson;
    private final OperationRepository operationRepository;
    private final String id;
    private final int socketOperationPort;

    public OperationRemoteDataSourceImpl(OperationRepositoryImpl operationRepository,
                                         String id,
                                         String host,
                                         int port,
                                         int socketOperationPort) {
        this.host = host;
        this.port = port;
        this.id = id;
        this.vertx = Vertx.vertx();
        this.gson = GsonUtils.createGson();
        this.operationRepository = operationRepository;
        this.socketOperationPort = socketOperationPort;
    }
    public void initialize(){
        HttpClient clientOperation = vertx.createHttpClient();
        clientOperation.webSocket(socketOperationPort, this.host, "/",
                wsC -> {
                    WebSocket ctx = wsC.result();
                    Log.i(TAG, "Connected to socket");
                    ctx.textMessageHandler(msg -> {
                        JsonObject json = new JsonObject(msg);
                        Log.i(TAG, msg);
                        if (json.getValue("greenhouseId").equals(this.id)) {
                            getLastParameterOperation(json.getString("parameterName"), id);
                        }});
                });
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
