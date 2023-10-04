package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.app.FragmentManager;
import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.AssetFileDescriptor;
import android.media.MediaPlayer;
import android.media.Ringtone;
import android.media.RingtoneManager;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.Toast;


import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;


public class TestActivity extends AppCompatActivity {

    Button btnTest;
    private static final int REQUEST_WRITE_SETTINGS_PERMISSION = 1;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTest = findViewById(R.id.btnTest);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                String path = "android.resource://com.example.musicapp/raw/lebaobinh";
                String path2 = Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)
                        .getPath() + "/baihat2.mp3";


//                MediaPlayer mediaPlayer = new MediaPlayer();
//                try {
//                    mediaPlayer.setDataSource(getApplicationContext(),
//                            Uri.parse(path2));
//                    mediaPlayer.prepare();
//                    mediaPlayer.start();
//                } catch (IOException e) {
//                    throw new RuntimeException(e);
//                }

                boolean permission;
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                    permission = Settings.System.canWrite(TestActivity.this);
                } else {
                    permission = ContextCompat.checkSelfPermission(TestActivity.this, Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
                }
                if (permission) {
                    //do your code
                } else {
                    if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                        intent.setData(Uri.parse("package:" + TestActivity.this.getPackageName()));
                        TestActivity.this.startActivityForResult(intent, 102);
                    } else {
                        ActivityCompat.requestPermissions(TestActivity.this,
                                new String[]{Manifest.permission.WRITE_SETTINGS},
                                REQUEST_WRITE_SETTINGS_PERMISSION);
                    }
                }

                if (ContextCompat.checkSelfPermission(TestActivity.this,
                        Manifest.permission.MODIFY_AUDIO_SETTINGS) != PackageManager.PERMISSION_GRANTED) {
                    // Yêu cầu quyền từ người dùng
                    ActivityCompat.requestPermissions(TestActivity.this,
                            new String[]{Manifest.permission.MODIFY_AUDIO_SETTINGS}, 1);
                    return;
                }


                ContentValues values = new ContentValues();
                values.put(MediaStore.MediaColumns.DATA, path);
                values.put(MediaStore.MediaColumns.TITLE, "tenBaiHat");
                values.put(MediaStore.MediaColumns.SIZE, 10485760);
                values.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
                values.put(MediaStore.Audio.Media.ARTIST, "tenCaSi");
                values.put(MediaStore.Audio.Media.IS_RINGTONE, true);

                Uri uri = Uri.parse(path2);

                ContentResolver mCr = getContentResolver();
                Uri newUri = mCr.insert(uri, values);
//                Uri newUri = getContentResolver().insert(uri, values);

//                RingtoneManager.setActualDefaultRingtoneUri(
//                        TestActivity.this,
//                        RingtoneManager.TYPE_RINGTONE,
//                        uri);

                Settings.System.putString(mCr, Settings.System.RINGTONE,
                        path2);
            }
        });


    }

    private void openAndroidPermissionsMenu() {
        Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
        intent.setData(Uri.parse("package:" + getPackageName()));
        startActivityForResult(intent, 102);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 102 && Settings.System.canWrite(this)) {
            Log.d("success", "MainActivity.CODE_WRITE_SETTINGS_PERMISSION success");
            //do your code
        }
    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == REQUEST_WRITE_SETTINGS_PERMISSION) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                // Người dùng đã cấp quyền WRITE_SETTINGS
            } else {
                // Người dùng đã từ chối cấp quyền WRITE_SETTINGS
            }
        }
    }


}