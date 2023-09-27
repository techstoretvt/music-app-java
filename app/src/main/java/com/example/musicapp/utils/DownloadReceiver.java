package com.example.musicapp.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.database.MusicAppHelper;

import java.io.File;
import java.io.IOException;

public class DownloadReceiver extends BroadcastReceiver {
    @Override
    public void onReceive(Context context, Intent intent) {
        // Xử lý sự kiện hoàn thành tải xuống ở đây
        String action = intent.getAction();
        if (DownloadManager.ACTION_DOWNLOAD_COMPLETE.equals(action)) {
            long downloadId = intent.getLongExtra(DownloadManager.EXTRA_DOWNLOAD_ID, -1);

            if (downloadId != -1) {

                // Xử lý tệp âm thanh sau khi tải xuống hoàn thành
                String path = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                        .getPath() + "/" + BaiHatAdapter.idBaiHat + ".mp3";

                Log.e("path", path);

                File file = new File(path);

                if (file.exists()) {
                    // Tệp tồn tại
                    Log.e("kkk", "ton tai");
                } else {
                    // Tệp không tồn tại
                    Log.e("kkk", "ko ton tai");
                }

                BaiHatAdapter.iconDownLoad.setImageResource(R.drawable.baseline_favorite_24);
                BaiHatAdapter.md.dismiss();

                //luu vao sqlite
                MusicAppHelper musicAppHelper = new MusicAppHelper(context.getApplicationContext(),
                        "MusicApp.sqlite", null, 1);

                String id = BaiHatAdapter.currentBaiHat.getId();
                String tenBh = BaiHatAdapter.currentBaiHat.getTenBaiHat();
                String tenCS = BaiHatAdapter.currentBaiHat.getCasi().getTenCaSi();
                String linkAnh = BaiHatAdapter.currentBaiHat.getAnhBia();
                String loiBH = BaiHatAdapter.currentBaiHat.getLoiBaiHat();

                musicAppHelper.QueryData(String.format("INSERT INTO BaiHat VALUES ('%s','%s','%s','%s','%s','%s')",
                        id, tenBh, tenCS, path, linkAnh, loiBH));


                // Ví dụ: Hiển thị thông báo
                Toast.makeText(context, "Tải xuống hoàn thành", Toast.LENGTH_SHORT).show();

//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(path);
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            } else {
                Toast.makeText(context, "Tải xuống that bai", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
