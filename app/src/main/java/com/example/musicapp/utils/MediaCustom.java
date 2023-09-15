package com.example.musicapp.utils;

import android.media.MediaPlayer;
import android.util.Log;

import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class MediaCustom {

    public static MediaPlayer mediaPlayer = new MediaPlayer();

    public static Boolean isData = false;

    public static String strTotalTime = "";
    public static int totalTime = 0;

    public static void phatNhac(String url) {
        try {
            mediaPlayer.reset();
            mediaPlayer.setDataSource(url);
            mediaPlayer.prepare();
            mediaPlayer.start();

            //su kiện kết thúc
            mediaPlayer.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    MainActivity.isPlay = false;
                    if (MainActivity.dungNhac != null) {
                        MainActivity.dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
                    }

                    if (ChiTietNhacActivity.btnPausePlay != null) {
                        ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
                    }

                }
            });

            //lay tg nhac
            int duration = mediaPlayer.getDuration() / 1000;
            totalTime = duration;
            int phut = duration / 60;
            duration = duration - (60 * phut);
            strTotalTime = String.valueOf(phut) + ":";
            if (duration < 10) strTotalTime += "0" + String.valueOf(duration);
            else strTotalTime += String.valueOf(duration);

            isData = true;

            MainActivity.isPlay = true;

            MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

        } catch (IOException e) {
            Log.e("error phat am thanh", e.getMessage());

        }
    }

    public static void play() {
        if (isData) {
            mediaPlayer.start();
            MainActivity.isPlay = true;
        }
    }

    public static void pause() {
        if (mediaPlayer.isPlaying()) {
            mediaPlayer.pause();
            MainActivity.isPlay = false;
        }
    }

    public static String getStrCurrentTime() {
        int currentTime = mediaPlayer.getCurrentPosition() / 1000;
        int phut = currentTime / 60;
        currentTime = currentTime - (60 * phut);
        String strCurrentTime = String.valueOf(phut) + ":";
        if (currentTime < 10)
            strCurrentTime += "0" + String.valueOf(currentTime);
        else strCurrentTime += String.valueOf(currentTime);

        return strCurrentTime;
    }

    public static float getFloatCurrentTime() {
        float currentTime = mediaPlayer.getCurrentPosition() / 1000;
        return currentTime;
    }

    public static void setCurrentTime(int newTime) {
        if (isData) {
            newTime = newTime * 1000;
            mediaPlayer.seekTo(newTime);
        }
    }
}
