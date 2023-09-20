package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.ResponseDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        checkStartServer();
    }

    private void checkStartServer() {
        ApiServiceV1.apiServiceV1.checkStartServer().enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault responseDefault = response.body();
                Log.e("log", String.valueOf(responseDefault));
                if (responseDefault != null && responseDefault.getErrCode() == 0) {
                    SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);

                    String accessToken = sharedPreferences.getString("accessToken", null);
                    String refreshToken = sharedPreferences.getString("refreshToken", null);
                    String timeToken = sharedPreferences.getString("timeToken", null);


                    Intent intent;
                    if (accessToken == null || accessToken.isEmpty() ||
                            refreshToken == null || refreshToken.isEmpty() ||
                            timeToken == null || timeToken.isEmpty()
                    ) {
                        intent = new Intent(StartActivity.this, LoginActivity.class);
                        startActivity(intent);
                    } else {
                        Log.e("accessToken", accessToken);
                        Log.e("refreshToken", refreshToken);
                        Log.e("timeToken", timeToken);
                        long time = System.currentTimeMillis();
                        Log.e("timeCurrent", String.valueOf(time));
                        if (Long.parseLong(timeToken) < time) {
                            intent = new Intent(StartActivity.this, LoginActivity.class);
                            startActivity(intent);
                        } else {
                            intent = new Intent(StartActivity.this, MainActivity.class);
                            startActivity(intent);
                        }


                    }


                    finish();
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                TextView textView = findViewById(R.id.txtText);
                textView.setText("Server đang gặp sự cố vui lòng thử lại sau!");
                textView.setTextColor(Color.RED);
                Toast.makeText(StartActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
            }
        });
    }
}