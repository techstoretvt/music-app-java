package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.util.Log;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.modal.anhxajson.ResponseDefault;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class StartActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);


        MusicAppHelper musicAppHelper = new MusicAppHelper(this,
                "MusicApp.sqlite", null, 1);

        musicAppHelper.QueryData("CREATE TABLE IF NOT EXISTS BaiHat(" +
                "id VARCHAR(200) PRIMARY KEY," +
                "tenBaiHat VARCHAR(200)," +
                "tenCaSi VARCHAR(200), " +
                "linkbaiHat VARCHAR(200)," +
                "linkAnh VARCHAR(200)," +
                "loiBaiHat VARCHAR(1000)" +
                ")"
        );


        //kiem tra mạng
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        NetworkInfo networkInfo = connectivityManager.getActiveNetworkInfo();
        if (networkInfo != null && networkInfo.isConnected()) {
            // Có kết nối mạng
            checkStartServer();

//            Intent intent = new Intent(StartActivity.this, MainActivity.class);
//            intent.putExtra("isNetwork", "true");
//            startActivity(intent);

        } else {
            // Không có kết nối mạng
            Toast.makeText(StartActivity.this, "khong co mang", Toast.LENGTH_SHORT)
                    .show();
            Intent intent = new Intent(StartActivity.this, MainActivity.class);
            intent.putExtra("isNetwork", "false");
            startActivity(intent);
        }


        //set mau gradient
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c49515c"), Color.BLACK}
        );
        LinearLayout linearLayout = findViewById(R.id.layoutLoading);
        linearLayout.setBackground(gradientDrawable);

    }

    private void checkStartServer() {
        ApiServiceV1.apiServiceV1.checkStartServer().enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault responseDefault = response.body();
                Log.e("log", String.valueOf(responseDefault));
                if (responseDefault != null && responseDefault.getErrCode() == 0) {
                    System.out.println("vao err code == 0");
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
                            intent.putExtra("isNetwork", "true");
                            startActivity(intent);
                        }


                    }
                    finish();
                } else {
                    System.out.println("vao err 1");
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                TextView textView = findViewById(R.id.txtText);
                textView.setText("Server đang gặp sự cố vui lòng thử lại sau!");
                textView.setTextColor(Color.RED);
            }
        });
    }
}