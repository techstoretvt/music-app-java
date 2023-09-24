package com.example.musicapp.utils;

import android.util.Log;

import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;

public class MyWebSocketClient {

    private static final String WS_URL = "wss://techstoretvtserver2.onrender.com";

    private WebSocket webSocket;

    public MyWebSocketClient() {
        OkHttpClient client = new OkHttpClient.Builder()
                .readTimeout(0, TimeUnit.MILLISECONDS)
                .build();
        Request request = new Request.Builder()
                .url(WS_URL)
                .build();

        webSocket = client.newWebSocket(request, new WebSocketListener() {
            @Override
            public void onOpen(WebSocket webSocket, okhttp3.Response response) {
                // Khi kết nối WebSocket thành công
                Log.e("ket noi thanh cong", "");
            }

            @Override
            public void onMessage(WebSocket webSocket, String text) {
                // Xử lý tin nhắn được gửi từ máy chủ
            }

            @Override
            public void onClosed(WebSocket webSocket, int code, String reason) {
                // Khi kết nối WebSocket bị đóng
                Log.e("WebSocket bị đóng", "");
            }

            @Override
            public void onFailure(WebSocket webSocket, Throwable t, okhttp3.Response response) {
                // Xử lý lỗi khi kết nối WebSocket
                Log.e("lỗi khi kết nối WebSocket", "");
            }
        });

        client.dispatcher().executorService().shutdown();


    }

    public void sendMessage(String message) {
        webSocket.send(message);
    }

    public void closeWebSocket() {
        webSocket.close(1000, "Goodbye!");
    }

}
