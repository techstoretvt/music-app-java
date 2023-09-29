package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicapp.R;
import com.example.musicapp.fragments.ThuVienFragment;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;

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


                SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("accessToken");
                editor.remove("refreshToken");
                editor.apply();

                //dang xuat google
                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(SettingActivity.this,
                        GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(SettingActivity.this,
//                                        "Dang xuat", Toast.LENGTH_SHORT)
//                                .show();
                    }
                });

                finish();
            }
        });

    }
}