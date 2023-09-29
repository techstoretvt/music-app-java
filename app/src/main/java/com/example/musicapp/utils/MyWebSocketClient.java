package com.example.musicapp.utils;

import android.util.Log;

import java.net.URISyntaxException;

import io.socket.client.IO;
import io.socket.client.Socket;

public class MyWebSocketClient {

    private static final String WS_URL = "htpps://techstoretvtserver2.onrender.com";

    public Socket socket;

    public MyWebSocketClient() {
        try {
            socket = IO.socket("https://techstoretvtserver2.onrender.com");
            socket.connect();

//            socket.on("server_res", new Emitter.Listener() {
//                @Override
//                public void call(final Object... args) {
//                    // Xử lý dữ liệu từ máy chủ ở đây
//                    String data = args[0].toString();
//                    Log.e("data", data);
//                    // Hiển thị hoặc xử lý dữ liệu theo ý muốn
//                }
//            });
//
//            // Gửi dữ liệu lên máy chủ
//            socket.emit("demo_event", "Hello from Android");

        } catch (URISyntaxException e) {
            Log.e("Loi ket nối", "");
            e.printStackTrace();
        }
    }

}
