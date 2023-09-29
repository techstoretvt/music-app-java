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
import com.example.musicapp.modal.body.BodySignUp;
import com.example.musicapp.utils.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DangKyActivity extends AppCompatActivity {
    Button btnDangKy;
    TextInputLayout layoutFirstname, layoutLastname, layoutEmail, layoutPassword, layoutRePassword;
    TextInputEditText ipFirstname, ipLastname, ipEmail, ipPassword, ipRePassword;
    TextView txtErrMessage;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_dang_ky);

        //ánh xạ view
        anhXa();

        //set onchange input
        setOnChange();

        //set btn click
        btnDangKy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                if (checkValues()) {
                    handleSignUp();
                }
            }
        });
    }

    private void handleSignUp() {
        String firstname = String.valueOf(ipFirstname.getText());
        String lastname = String.valueOf(ipLastname.getText());
        String email = String.valueOf(ipEmail.getText());
        String password = String.valueOf(ipPassword.getText());

        BodySignUp bodySignUp = new BodySignUp(firstname, lastname, email, password);

        ApiServiceV1.apiServiceV1.signUp(bodySignUp).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault responseDefault = response.body();
                if (responseDefault != null) {
                    if (responseDefault.getErrCode() == 0) {
                        //logic success
                        Intent intent = new Intent(DangKyActivity.this, XacNhanTaiKhoanActivity.class);
                        intent.putExtra("email", email);
                        startActivity(intent);
                    } else {
                        txtErrMessage.setText(responseDefault.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Toast.makeText(DangKyActivity.this, "Error api sign up", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setOnChange() {
        ipFirstname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutFirstname.setError("");
                layoutFirstname.setErrorEnabled(false);
                txtErrMessage.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ipLastname.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutLastname.setError("");
                layoutLastname.setErrorEnabled(false);
                txtErrMessage.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
        ipEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutEmail.setError("");
                layoutEmail.setErrorEnabled(false);
                txtErrMessage.setText("");
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
                layoutPassword.setError("");
                layoutPassword.setErrorEnabled(false);
                txtErrMessage.setText("");
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
                layoutRePassword.setError("");
                layoutRePassword.setErrorEnabled(false);
                txtErrMessage.setText("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });
    }

    private Boolean checkValues() {
        String firstname = String.valueOf(ipFirstname.getText());
        String lastname = String.valueOf(ipLastname.getText());
        String email = String.valueOf(ipEmail.getText());
        String password = String.valueOf(ipPassword.getText());
        String rePassword = String.valueOf(ipRePassword.getText());

        Boolean isEmpty = false;
        if (firstname.isEmpty()) {
            layoutFirstname.setErrorEnabled(true);
            layoutFirstname.setError("Bắt buộc *");
            txtErrMessage.setText("Vui lòng điền đẩy đủ thông tin");
            isEmpty = true;
        }
        if (lastname.isEmpty()) {
            layoutLastname.setErrorEnabled(true);
            layoutLastname.setError("Bắt buộc *");
            txtErrMessage.setText("Vui lòng điền đẩy đủ thông tin");
            isEmpty = true;
        }
        if (email.isEmpty()) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Bắt buộc *");
            txtErrMessage.setText("Vui lòng điền đẩy đủ thông tin");
            isEmpty = true;
        }
        if (password.isEmpty()) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Bắt buộc *");
            txtErrMessage.setText("Vui lòng điền đẩy đủ thông tin");
            isEmpty = true;
        }
        if (rePassword.isEmpty()) {
            layoutRePassword.setErrorEnabled(true);
            layoutRePassword.setError("Bắt buộc *");
            txtErrMessage.setText("Vui lòng điền đẩy đủ thông tin");
            isEmpty = true;
        }

        if (isEmpty) {
            return false;
        }

        if (!Common.isValidEmail(email)) {
            layoutEmail.setErrorEnabled(true);
            layoutEmail.setError("Email không đúng định dạng");
            return false;
        }

        if (password.length() < 6) {
            layoutPassword.setErrorEnabled(true);
            layoutPassword.setError("Mật khẩu quá ngắn");
            return false;
        }
        if (!password.equals(rePassword)) {
            layoutRePassword.setErrorEnabled(true);
            layoutRePassword.setError("Mật khẩu không trùng khớp");
            return false;
        }

        return true;
    }

    private void anhXa() {
        btnDangKy = findViewById(R.id.btnDangKy);
        layoutFirstname = findViewById(R.id.layoutFirstname);
        layoutLastname = findViewById(R.id.layoutLastname);
        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);
        layoutRePassword = findViewById(R.id.layoutRePassword);
        ipFirstname = findViewById(R.id.firstname);
        ipLastname = findViewById(R.id.lastname);
        ipEmail = findViewById(R.id.email);
        ipPassword = findViewById(R.id.password);
        ipRePassword = findViewById(R.id.rePassword);
        txtErrMessage = findViewById(R.id.txtErrMessage);
    }
}