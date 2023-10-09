package com.example.musicapp.utils;

import android.app.DownloadManager;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Environment;
import android.os.Handler;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;

import java.io.File;

public class DownloadReceiver extends BroadcastReceiver {

    public static Boolean isDownload = false;

    public static Boolean isNhacChuong = false;

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


                    //logic xu ly
                    if (BsBaiHat.iconDownLoad != null && !ChiTietNhacActivity.isChiTietNhac) {
                        BsBaiHat.iconDownLoad.setVisibility(View.VISIBLE);
                        Log.e("icon", "tim thay");
                        if (ChiTietThuVienFragment.isChiTietDS) {
                            ChiTietThuVienFragment.adapter.notifyDataSetChanged();
                        }
                    } else {
                        Log.e("icon", "ko tim thay");
                    }
                    if (BsBaiHat.currentBaiHat.getId().equals(BaiHatAdapter.currentBaiHat.getId()) &&
                            !ChiTietNhacActivity.isChiTietNhac
                    )
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


                        String strTenCaSi = "";
                        for (int i = 0; i < BsBaiHat.currentBaiHat.getBaiHat_caSis().size(); i++) {
                            if (i == 0)
                                strTenCaSi = BsBaiHat.currentBaiHat.getBaiHat_caSis().
                                        get(i).getCasi().getTenCaSi();
                            else
                                strTenCaSi += ", " + BsBaiHat.currentBaiHat.getBaiHat_caSis().
                                        get(i).getCasi().getTenCaSi();
                        }
                        String tenCS = strTenCaSi;

                        String linkAnh = BsBaiHat.currentBaiHat.getAnhBia();
                        String loiBH = BsBaiHat.currentBaiHat.getLoiBaiHat();

                        musicAppHelper.QueryData(String.format("INSERT INTO BaiHat VALUES ('%s','%s','%s','%s','%s','%s')",
                                id, tenBh, tenCS, path, linkAnh, loiBH));
                    }

                    if (isNhacChuong == true) {

                        Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {
                                Log.e("Nhac chuong", "Vao nhac chuong");
                                Common.setRingtone(context, path);

                            }
                        }, 1000);
                    } else {
                        Toast.makeText(context, "Tải xuống hoàn thành", Toast.LENGTH_SHORT).show();
                        Log.e("Nhac chuong", "khong cai nhac chuong");
                    }
                    DownloadReceiver.isDownload = false;


                } else {
                    DownloadReceiver.isDownload = false;
                    isNhacChuong = false;
                    // Tệp không tồn tại
                    Toast.makeText(context, "Tải xuống thất bại", Toast.LENGTH_SHORT).show();
                }

            } else {
                DownloadReceiver.isDownload = false;
                isNhacChuong = false;
                Toast.makeText(context, "Tải xuống that bai", Toast.LENGTH_SHORT).show();
            }
        }
    }
}
