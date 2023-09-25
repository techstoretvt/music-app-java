package com.example.musicapp.activities;


import androidx.activity.result.ActivityResult;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.os.Bundle;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.utils.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

import androidx.transition.Transition;

public class QuenMKActivity extends AppCompatActivity {
    Button btnLayMa;

    TextInputLayout layoutEmail;
    TextInputEditText ipEmail;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_quen_mkactivity);

//        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);
        anhXa();

        btnLayMa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleLayMa();

            }
        });

        ipEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                handleLayMa();
                return false;
            }
        });

    }

    private void handleLayMa() {
        String strEmail = String.valueOf(ipEmail.getText());

        layoutEmail.setErrorEnabled(false);
        layoutEmail.setError("");
        if (strEmail.isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("*Bắt buộc");
        } else if (!Common.isValidEmail(strEmail)) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email không đúng định dạng");
        } else {
            //call api
            BodyLogin bodyLogin = new BodyLogin();
            bodyLogin.setEmail(strEmail);

            ProgressDialog progressDialog = new ProgressDialog(QuenMKActivity.this);
            progressDialog.setTitle("Đang tạo mã...");
            progressDialog.show();
            ApiServiceV1.apiServiceV1.getCodeQuenMK(bodyLogin).enqueue(new Callback<ResponseDefault>() {
                @Override
                public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                    ResponseDefault res = response.body();
                    if (res != null) {
                        if (res.getErrCode() == 0) {
                            Intent intent = new Intent(QuenMKActivity.this, ChangePassForgetActivity.class);
                            intent.putExtra("email", strEmail);
                            startActivityForResult(intent, 100);

                            progressDialog.dismiss();

                        } else {
                            layoutEmail.setErrorEnabled(true);
                            layoutEmail.setError(res.getErrMessage());
                        }
                    }
                }

                @Override
                public void onFailure(Call<ResponseDefault> call, Throwable t) {
                    Toast.makeText(QuenMKActivity.this, "Error api Quen mat khau", Toast.LENGTH_SHORT)
                            .show();
                }
            });
        }
    }


    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 100 && resultCode == 101) {
            String status = data.getStringExtra("status");
            if (status.equals("success")) {
                finish();
            }
        }
    }

    private void anhXa() {
        btnLayMa = findViewById(R.id.btnLayMa);
        layoutEmail = findViewById(R.id.layoutEmail);
        ipEmail = findViewById(R.id.email);
    }
}