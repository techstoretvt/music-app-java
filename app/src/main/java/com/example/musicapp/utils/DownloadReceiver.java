package com.example.musicapp.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.media.MediaPlayer;
import android.os.Environment;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.TestActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.modal.anhxajson.ChiTietDanhSachPhat;

import java.io.File;
import java.io.IOException;

public class DownloadReceiver extends BroadcastReceiver {

    public static Boolean isDownload = false;

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
                    Toast.makeText(context, "Tải xuống hoàn thành", Toast.LENGTH_SHORT).show();

                    //logic xu ly
                    if (BsBaiHat.iconDownLoad != null) {
                        BsBaiHat.iconDownLoad.setVisibility(View.VISIBLE);
                        Log.e("icon", "tim thay");
                        if (ChiTietThuVienFragment.isChiTietDS) {
                            ChiTietThuVienFragment.adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("icon", "ko tim thay");
                    }
                    if (BsBaiHat.currentBaiHat.getId().equals(BaiHatAdapter.currentBaiHat.getId()))
                        BaiHatAdapter.md.dismiss();

                    //luu vao sqlite

                    MusicAppHelper musicAppHelper = new MusicAppHelper(context.getApplicationContext(),
                            "MusicApp.sqlite", null, 1);

                    Cursor dataBaiHat = musicAppHelper.GetData(String.format(
                            "SELECT * FROM BaiHat where id = '%s'",
                            BsBaiHat.currentBaiHat.getId()
                    ));

                    Boolean checkExists = false;
                    while (dataBaiHat.moveToNext()) {
                        checkExists = true;
                    }

                    if (!checkExists) {
                        String id = BsBaiHat.currentBaiHat.getId();
                        String tenBh = BsBaiHat.currentBaiHat.getTenBaiHat();
                        String tenCS = BsBaiHat.currentBaiHat.getCasi().getTenCaSi();
                        String linkAnh = BsBaiHat.currentBaiHat.getAnhBia();
                        String loiBH = BsBaiHat.currentBaiHat.getLoiBaiHat();

                        musicAppHelper.QueryData(String.format("INSERT INTO BaiHat VALUES ('%s','%s','%s','%s','%s','%s')",
                                id, tenBh, tenCS, path, linkAnh, loiBH));
                    }

                    DownloadReceiver.isDownload = false;

                } else {
                    DownloadReceiver.isDownload = false;
                    // Tệp không tồn tại
                    Toast.makeText(context, "Tải xuống thất bại", Toast.LENGTH_SHORT).show();
                }

            } else {
                DownloadReceiver.isDownload = false;
                Toast.makeText(context, "Tải xuống that bai", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
