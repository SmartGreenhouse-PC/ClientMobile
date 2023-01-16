package it.unibo.smartgh.data.operation;

import android.annotation.SuppressLint;

import com.google.gson.Gson;

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import io.vertx.core.Vertx;
import io.vertx.core.json.JsonObject;
import io.vertx.ext.web.client.WebClient;
import it.unibo.smartgh.entity.operation.Operation;
import it.unibo.smartgh.presentation.GsonUtils;

public class OperationRemoteDataSourceImpl implements OperationRemoteDataSource{
    private static final String TAG = OperationRemoteDataSourceImpl.class.getSimpleName();
    private static final int PORT = 8890;
    private static final String HOST = "192.168.3.232";
    private final static String BASE_PATH = "/clientCommunication/operations";
    private final Vertx vertx;
    private final Gson gson;

    public OperationRemoteDataSourceImpl() {
        this.vertx = Vertx.vertx(); //vertx; TODO
        this.gson = GsonUtils.createGson(); //gson;
    }

    @Override
    public void sendNewOperation(Operation operation) {
        WebClient client = WebClient.create(vertx);
        @SuppressLint("SimpleDateFormat") DateFormat formatter = new SimpleDateFormat("dd/MM/yyyy - HH:mm:ss");
        client.post(PORT, HOST, BASE_PATH)
                .putHeader("Content-Type", "application/json")
                .sendJsonObject(new JsonObject()
                        .put("greenhouseId", operation.getGreenhouseId())
                        .put("modality", operation.getModality().name())
                        .put("date", formatter.format(operation.getDate()))
                        .put("parameter", operation.getParameter())
                        .put("action", operation.getAction())
                );
    }
}
