package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.app.Activity;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicapp.R;
import com.example.musicapp.fragments.ThuVienFragment;

public class SettingActivity extends AppCompatActivity {

    ImageView btnBack;
    LinearLayout btnDangXuat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_setting);
        btnBack = findViewById(R.id.btnBack);
        btnDangXuat = findViewById(R.id.btnDangXuat);


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });

        btnDangXuat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                //reset data
                ThuVienFragment.danhSachPhats = null;

                Intent intent = new Intent(SettingActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });

    }
}