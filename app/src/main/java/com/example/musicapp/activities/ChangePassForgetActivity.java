package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyChangePass;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChangePassForgetActivity extends AppCompatActivity {
    Button btnXong;
    TextView txtErr;
    TextInputLayout layoutMa, layoutPassword, layoutRePassword;
    TextInputEditText ipMa, ipPassword, ipRePassword;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_change_pass_forget);

        Intent intent = getIntent();
        String email = intent.getStringExtra("email");

        if (email.isEmpty()) {
            finish();
        } else {
            anhXa();

            setOnchangeText();

            btnXong.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    if (checkValues()) {
                        String strMa = String.valueOf(ipMa.getText());
                        String strPassword = String.valueOf(ipPassword.getText());
                        BodyChangePass body = new BodyChangePass(email, strMa, strPassword);
                        txtErr.setText("");

                        ApiServiceV1.apiServiceV1.doiMK(body).enqueue(new Callback<ResponseDefault>() {
                            @Override
                            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                                ResponseDefault res = response.body();
                                if (res != null) {
                                    if (res.getErrCode() == 0) {
                                        intent.putExtra("status", "success");
                                        setResult(101, intent);
                                        finish();
                                    } else {
                                        txtErr.setText(res.getErrMessage());
                                    }
                                }
                            }

                            @Override
                            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                                Toast.makeText(ChangePassForgetActivity.this, "Error api doi mat khau", Toast.LENGTH_SHORT)
                                        .show();
                            }
                        });
                    }
                }
            });


        }

    }

    private void setOnchangeText() {
        ipMa.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutMa.setErrorEnabled(false);
                layoutMa.setError("");
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
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ipRePassword.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutRePassword.setErrorEnabled(false);
                layoutRePassword.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Boolean checkValues() {
        String strMa = String.valueOf(ipMa.getText());
        String strPassword = String.valueOf(ipPassword.getText());
        String strRePassword = String.valueOf(ipRePassword.getText());

        Boolean isCheckEmpty = false;

        if (strMa.isEmpty()) {
            layoutMa.setErrorEnabled(true);
            layoutMa.setError("*Bắt buộc");
            isCheckEmpty = true;
        }
        if (strPassword.isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("*Bắt buộc");
            isCheckEmpty = true;
        }
        if (strRePassword.isEmpty()) {
            layoutRePassword.setErrorEnabled(true);
            layoutRePassword.setError("*Bắt buộc");
            isCheckEmpty = true;
        }

        if (isCheckEmpty) {
            return false;
        }

        if (strPassword.length() < 6) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu quá ngắn");
            return false;
        }

        if (!strPassword.equals(strRePassword)) {
            layoutRePassword.setErrorEnabled(true);
            layoutRePassword.setError("Mật khẩu không trùng khớp");
            return false;
        }


        return true;
    }


    private void anhXa() {
        btnXong = findViewById(R.id.btnXong);
        layoutMa = findViewById(R.id.layoutMa);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutRePassword = findViewById(R.id.layoutRePassword);
        ipMa = findViewById(R.id.ma);
        ipPassword = findViewById(R.id.password);
        ipRePassword = findViewById(R.id.rePassword);
        txtErr = findViewById(R.id.txtErr);
    }
}