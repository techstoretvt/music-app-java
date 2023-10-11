package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.TestAdapter;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    Button btnTest;
    EditText edText;

    public static int positon = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTest = findViewById(R.id.btnTest);

        edText = findViewById(R.id.edText);


        YouTubePlayerView youTubePlayerView = findViewById(R.id.youtube_player_view);
        getLifecycle().addObserver(youTubePlayerView);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
//                    // do stuff with it
//                    String videoId = "tCuarGIaPec";
//                    youTubePlayer.loadVideo(videoId, 0);
//                });

                String linkVideo = "https://www.youtube.com/watch?v=QFB-DzDvlfY";
                String videoId = linkVideo.split("v=")[1].split("&")[0];

                Log.e("link", videoId);
            }
        });


    }

}