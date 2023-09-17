package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.utils.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp, btnQuenMK;
    TextView txtErrMess;
    TextInputEditText ipEmail, ipPassword;
    TextInputLayout layoutEmail, layoutPassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ánh xạ view
        anhXaView();

        //onchange input
        setChangeInput();

        //set click btn login
        btnLogin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //Toast.makeText(LoginActivity.this, "Vao", Toast.LENGTH_SHORT).show();
                if (handleCheckValue()) {
                    Log.e("Dữ liệu đúng", "Tiến hành xử lý");
                    txtErrMess.setText("");
                    handleLogin();
                }
            }
        });

        btnSignUp.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, DangKyActivity.class);
                startActivity(intent);
            }
        });

        btnQuenMK.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(LoginActivity.this, QuenMKActivity.class);
                startActivity(intent);
            }
        });


    }

    private void handleLogin() {
        String strEmail = String.valueOf(ipEmail.getText());
        String strPassword = String.valueOf(ipPassword.getText());

        BodyLogin bodyLogin = new BodyLogin(strEmail, strPassword);

        ApiServiceV1.apiServiceV1.login(bodyLogin).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();
                if (login != null) {
                    if (login.getErrCode() == 0) {
                        String accessToken = login.getAccessToken();
                        String refreshToken = login.getRefreshToken();

                        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("accessToken", accessToken);
                        editor.putString("refreshToken", refreshToken);
                        long time = System.currentTimeMillis() + 60000 * 10;
                        editor.putString("timeToken", String.valueOf(time));
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        startActivity(intent);

                        finish();

                    } else {
                        txtErrMess.setText(login.getErrMessage());
                    }

                }
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lối api login", Toast.LENGTH_SHORT).show();
            }
        });
    }

    private void setChangeInput() {
        ipEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutEmail.setErrorEnabled(false);
                layoutEmail.setError("");
                txtErrMess.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ipPassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutPassword.setErrorEnabled(false);
                layoutPassword.setError("");
                txtErrMess.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Boolean handleCheckValue() {
        String strEmail = String.valueOf(ipEmail.getText());
        String strPassword = String.valueOf(ipPassword.getText());
        Log.e("email", strEmail);
        Log.e("password", strPassword);

        if (strEmail.isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Vui lòng nhập email");
            return false;
        }

        if (strPassword.isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Vui lòng nhập mật khẩu");
            return false;
        }

        if (!Common.isValidEmail(strEmail)) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email không đúng định dạng");
            return false;
        }

        if (strPassword.length() < 6) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu phải ít nhất 6 ký tự");
            return false;
        }

        return true;
    }

    private void anhXaView() {
        btnLogin = findViewById(R.id.btnLogin);
        btnSignUp = findViewById(R.id.btnSignUp);
        btnQuenMK = findViewById(R.id.btnQuenMatKhau);

        ipEmail = findViewById(R.id.email);
        ipPassword = findViewById(R.id.password);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);

        txtErrMess = findViewById(R.id.errMessage);

    }


}