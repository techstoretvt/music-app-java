package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;

import android.Manifest;
import android.app.DownloadManager;
import android.content.Context;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.utils.DownloadReceiver;


public class TestActivity extends AppCompatActivity {

    Button btnHandle;

    EditText url;

    String urlStr = "https://res.cloudinary.com/dultkpqjp/video/upload/v1694524855/music/yrt6fdrp2ylvwzo7tzsw.mp3";

    MusicAppHelper musicAppHelper;

    private DownloadReceiver downloadReceiver;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnHandle = findViewById(R.id.btnHandle);
        url = findViewById(R.id.url);

        btnHandle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo DownloadTask

//                DownloadManager.Request request = new DownloadManager.Request(Uri.parse(urlStr));
//                String title = URLUtil.guessFileName(urlStr, null, null);
//                request.setTitle(title);
//                request.setDescription("Downloading File please wait.....");
//                String cookie = CookieManager.getInstance().getCookie(urlStr);
//                request.addRequestHeader("cookie", cookie);
//                request.setNotificationVisibility(DownloadManager.Request.VISIBILITY_VISIBLE_NOTIFY_ONLY_COMPLETION);
//                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_DOWNLOADS, title);
//
//                DownloadManager downloadManager = (DownloadManager) getSystemService(DOWNLOAD_SERVICE);
//                downloadManager.enqueue(request);
//
//                Toast.makeText(TestActivity.this, "Downloading Started.", Toast.LENGTH_SHORT).show();

                // Kiểm tra xem người dùng đã cấp quyền chưa
                if (ActivityCompat.shouldShowRequestPermissionRationale(TestActivity.this,
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Giải thích lý do tại sao ứng dụng của bạn cần quyền
                    Toast.makeText(TestActivity.this,
                            "Ứng dụng cần quyền truy cập bộ nhớ để lưu trữ dữ liệu",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Yêu cầu quyền truy cập bộ nhớ
                    ActivityCompat.requestPermissions(TestActivity.this, new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }


            }
        });


        //sqlite
//        musicAppHelper = new MusicAppHelper(this, "MusicApp.sqlite", null, 1);

        //tao bang
//        musicAppHelper.QueryData("CREATE TABLE IF NOT EXISTS BaiHat(" +
//                "id VARCHAR(200) PRIMARY KEY," +
//                "tenBaiHat VARCHAR(200)," +
//                "tenCaSi VARCHAR(200), " +
//                "linkbaiHat VARCHAR(200)," +
//                "linkAnh VARCHAR(200)," +
//                "loiBaiHat VARCHAR(1000)" +
//                ")"
//        );

        //them du lieu
//        musicAppHelper.QueryData("INSERT INTO BaiHat VALUES (" +
//                "'ksdjfkj', 'hen mot mai', 'Chau Khai Phong', 'linkbaihat','linkanh','loi bai hat')"
//        );
//        musicAppHelper.QueryData("INSERT INTO BaiHat VALUES (" +
//                "'aaaa', 'nhac dao', 'Lee min ho', 'linkbaihat','linkanh','loi bai hat')"
//        );

        //hien thi
//        Cursor dataBaiHat = musicAppHelper.GetData("SELECT * FROM BaiHat");
//
//        while (dataBaiHat.moveToNext()) {
//            String tenBaiHat = dataBaiHat.getString(1);
//            Toast.makeText(TestActivity.this,
//                    tenBaiHat,
//                    Toast.LENGTH_SHORT).show();
//        }

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền
                // Thực hiện các tác vụ cần quyền truy cập bộ nhớ
                Toast.makeText(TestActivity.this,
                        "Da cap quyen",
                        Toast.LENGTH_SHORT).show();

                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(urlStr);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Tải nhạc");
                request.setDescription("Tải nhạc từ link");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC, "audio.mp3");
                downloadManager.enqueue(request);

                downloadReceiver = new DownloadReceiver();
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                registerReceiver(downloadReceiver, filter);


            } else {
                // Người dùng từ chối quyền
                // Cung cấp cho người dùng một giải pháp thay thế
                Toast.makeText(TestActivity.this,
                        "Tu choi cap quyen",
                        Toast.LENGTH_SHORT).show();
            }
        }
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        unregisterReceiver(downloadReceiver);
    }
}