package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyXacNhan;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XacNhanTaiKhoanActivity extends AppCompatActivity {
    TextView txtEmail;
    Button btnXacNhan;

    TextInputLayout layoutMa;
    TextInputEditText ipMa;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_xac_nhan_tai_khoan);

        //ánh xạ view
        anhXa();

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");
        txtEmail.setText(email);

        //set click
        btnXacNhan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String ma = String.valueOf(ipMa.getText());

                layoutMa.setError("");
                layoutMa.setErrorEnabled(false);

                if (ma.isEmpty()) {
                    layoutMa.setError("Vui lòng nhập mã xác nhận");
                    layoutMa.setErrorEnabled(true);
                } else {
                    BodyXacNhan bodyXacNhan = new BodyXacNhan(email, ma);
                    ApiServiceV1.apiServiceV1.xacNhanEmail(bodyXacNhan).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                            ResponseDefault responseDefault = response.body();
                            if (responseDefault != null) {
                                if (responseDefault.getErrCode() == 0) {
                                    Intent intent2 = new Intent(XacNhanTaiKhoanActivity.this, LoginActivity.class);
                                    startActivity(intent2);
                                    finish();
                                } else {
                                    layoutMa.setErrorEnabled(true);
                                    layoutMa.setError(responseDefault.getErrMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                            layoutMa.setErrorEnabled(true);
                            layoutMa.setError("Có lỗi phía server vui lòng thử lại sau");
                        }
                    });
                }
            }
        });

    }

    private void anhXa() {
        txtEmail = findViewById(R.id.txtEmail);
        btnXacNhan = findViewById(R.id.btnXacNhan);
        layoutMa = findViewById(R.id.layoutMa);
        ipMa = findViewById(R.id.ma);
    }
}