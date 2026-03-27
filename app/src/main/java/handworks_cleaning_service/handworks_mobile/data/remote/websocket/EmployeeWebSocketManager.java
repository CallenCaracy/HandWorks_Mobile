package handworks_cleaning_service.handworks_mobile.data.remote.websocket;

import android.os.Looper;
import android.util.Log;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

import org.json.JSONException;
import org.json.JSONObject;

import handworks_cleaning_service.handworks_mobile.BuildConfig;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class EmployeeWebSocketManager {
    private WebSocket webSocket;
    private final OkHttpClient client = new OkHttpClient();
    private String lastEmployeeId;
    private boolean shouldReconnect = true;

    private final MutableLiveData<JSONObject> events = new MutableLiveData<>();
    private final MutableLiveData<Boolean> connected = new MutableLiveData<>(false);

    public LiveData<JSONObject> getEvents() { return events; }
    public LiveData<Boolean> isConnected() { return connected; }

    public void connect(String employeeId) {
        shouldReconnect = true;
        lastEmployeeId = employeeId;

        if (webSocket != null) {
            webSocket.cancel();
        }

        String url = BuildConfig.WS_BASE_URL + "ws/employee?employeeID=" + employeeId;
        Log.d("WS", "Connecting to: " + url);

        Request request = new Request.Builder()
                .url(url)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
//            @Override
//            public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
//                super.onOpen(webSocket, response);
//                connected.postValue(true);
//            }

            @Override
            public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                try {
                    JSONObject json = new JSONObject(text);
                    String event = json.getString("event");

                    switch (event) {
                        case "booking_created":
                        case "booking_updated":
                            events.postValue(json);
                            break;

                        case "connected":
                            connected.postValue(true);
                            break;
                    }

                } catch (JSONException e) {
                    Log.e("WS", "Failure: " + e.getMessage());
                }
            }

//            @Override
//            public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
//                super.onMessage(webSocket, bytes);
//            }

            @Override
            public void onClosing(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosing(webSocket, code, reason);
                webSocket.close(1000, null);
            }

            @Override
            public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                super.onClosed(webSocket, code, reason);
                connected.postValue(false);
            }

            @Override
            public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, @Nullable Response response) {
                connected.postValue(false);

                if (!shouldReconnect) return;

                new android.os.Handler(Looper.getMainLooper()).postDelayed(() -> {
                    if (lastEmployeeId != null) {
                        connect(lastEmployeeId);
                    }
                }, 3000);
            }
        });
    }

    public void disconnect() {
        shouldReconnect = false;
        if (webSocket != null) webSocket.close(1000, null);
    }
}