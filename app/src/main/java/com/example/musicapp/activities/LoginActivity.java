package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.modal.body.BodyLoginGoogle;
import com.example.musicapp.utils.Common;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.api.ApiException;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;


import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoginActivity extends AppCompatActivity {
    Button btnLogin, btnSignUp, btnQuenMK, btnGoogle;
    TextView txtErrMess;
    TextInputEditText ipEmail, ipPassword;
    TextInputLayout layoutEmail, layoutPassword;

    Boolean isLogin = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);

        //Ánh xạ view
        anhXaView();

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#38383894"), Color.BLACK}
        );
        LinearLayout layoutFromLogin = findViewById(R.id.layoutFromLogin);
        layoutFromLogin.setBackground(gradientDrawable);

        //onchange input
        setChangeInput();


        //set click btn login
        setEvent();


    }

    private void setEvent() {
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

        ipPassword.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (handleCheckValue()) {
                    Log.e("Dữ liệu đúng", "Tiến hành xử lý");
                    txtErrMess.setText("");
                    handleLogin();
                }
                return false;
            }
        });

        ipEmail.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                if (handleCheckValue()) {
                    Log.e("Dữ liệu đúng", "Tiến hành xử lý");
                    txtErrMess.setText("");
                    handleLogin();
                }
                return false;
            }
        });

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestEmail()
                .build();

        GoogleSignInClient mGoogleSignInClient = GoogleSignIn.getClient(LoginActivity.this,
                gso);

        btnGoogle.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isLogin) return;
                Intent signInIntent = mGoogleSignInClient.getSignInIntent();
                startActivityForResult(signInIntent, 100);
            }
        });
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100) {
            // Đăng nhập thành công

            Task<GoogleSignInAccount> task = GoogleSignIn.getSignedInAccountFromIntent(data);
            handleSignInResult(task);

        }
    }

    private void handleSignInResult(Task<GoogleSignInAccount> completedTask) {
        try {
            GoogleSignInAccount account = completedTask.getResult(ApiException.class);

            String idGoogle = account.getId();
            String firstName = account.getGivenName();
            String lastName = account.getFamilyName();
            String avatar = account.getPhotoUrl().toString();

            BodyLoginGoogle body = new BodyLoginGoogle(firstName, lastName, avatar, idGoogle);

            ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
            progressDialog.setTitle("Đang đăng nhập...");
            progressDialog.show();

            isLogin = true;
            ApiServiceV1.apiServiceV1.loginGoogle(body).enqueue(new Callback<Login>() {
                @Override
                public void onResponse(Call<Login> call, Response<Login> response) {
                    Login res = response.body();
                    if (res != null) {
                        if (res.getErrCode() == 0) {
                            String accessToken = res.getAccessToken();
                            String refreshToken = res.getRefreshToken();
                            String idUser = res.getIdUser();

                            SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.putString("accessToken", accessToken);
                            editor.putString("refreshToken", refreshToken);
                            long time = System.currentTimeMillis() + 60000 * 60;
                            editor.putString("timeToken", String.valueOf(time));
                            editor.putString("idUser", String.valueOf(idUser));
                            editor.apply();

                            Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                            intent.putExtra("isNetwork", "true");
                            startActivity(intent);
                            progressDialog.dismiss();
                            finish();
                        } else {
                            txtErrMess.setText(res.getErrMessage());

                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(LoginActivity.this,
                                    GoogleSignInOptions.DEFAULT_SIGN_IN);
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(SettingActivity.this,
//                                        "Dang xuat", Toast.LENGTH_SHORT)
//                                .show();
                                }
                            });
                        }
                    }
                    progressDialog.dismiss();
                    isLogin = false;
                }

                @Override
                public void onFailure(Call<Login> call, Throwable t) {
                    Toast.makeText(LoginActivity.this,
                            "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
                    isLogin = false;
                }
            });


        } catch (ApiException e) {

            Log.w("error", "signInResult:failed code=" + e.getStatusCode());
            Toast.makeText(this, "Đăng nhập thất bại", Toast.LENGTH_SHORT).show();
            isLogin = false;
        }
    }

    private void handleLogin() {
        if (isLogin) return;
        String strEmail = String.valueOf(ipEmail.getText());
        String strPassword = String.valueOf(ipPassword.getText());

        BodyLogin bodyLogin = new BodyLogin(strEmail, strPassword);

        ProgressDialog progressDialog = new ProgressDialog(LoginActivity.this);
        progressDialog.setTitle("Đang đăng nhập...");
        progressDialog.show();

        isLogin = true;

        ApiServiceV1.apiServiceV1.login(bodyLogin).enqueue(new Callback<Login>() {
            @Override
            public void onResponse(Call<Login> call, Response<Login> response) {
                Login login = response.body();
                if (login != null) {
                    if (login.getErrCode() == 0) {
                        String accessToken = login.getAccessToken();
                        String refreshToken = login.getRefreshToken();
                        String idUser = login.getIdUser();

                        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                        SharedPreferences.Editor editor = sharedPreferences.edit();

                        editor.putString("accessToken", accessToken);
                        editor.putString("refreshToken", refreshToken);
                        long time = System.currentTimeMillis() + 60000 * 60;
                        editor.putString("timeToken", String.valueOf(time));
                        editor.putString("idUser", String.valueOf(idUser));
                        editor.apply();

                        Intent intent = new Intent(LoginActivity.this, MainActivity.class);
                        intent.putExtra("isNetwork", "true");
                        startActivity(intent);
                        progressDialog.dismiss();
                        finish();

                    } else {
                        txtErrMess.setText(login.getErrMessage());
                        progressDialog.dismiss();
                    }

                }
                isLogin = false;
            }

            @Override
            public void onFailure(Call<Login> call, Throwable t) {
                Toast.makeText(LoginActivity.this, "Lối api login", Toast.LENGTH_SHORT).show();
                progressDialog.dismiss();
                isLogin = false;
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
        btnGoogle = findViewById(R.id.btnGoogle);

        ipEmail = findViewById(R.id.email);
        ipPassword = findViewById(R.id.password);

        layoutEmail = findViewById(R.id.layoutEmail);
        layoutPassword = findViewById(R.id.layoutPassword);

        txtErrMess = findViewById(R.id.errMessage);

    }


}