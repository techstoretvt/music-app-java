package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.ThemDSPhat;
import com.example.musicapp.modal.body.BodyThemDSPhat;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.io.Serializable;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class AddPlayListActivity extends AppCompatActivity {

    TextInputLayout layoutTenDS;
    TextInputEditText tenDS;
    Button btnTao;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_add_play_list);

        layoutTenDS = findViewById(R.id.layoutTenDS);
        tenDS = findViewById(R.id.tenDS);
        btnTao = findViewById(R.id.btnTao);

        Intent intent = getIntent();

        tenDS.requestFocus();
        InputMethodManager imm = (InputMethodManager) getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.showSoftInput(tenDS, InputMethodManager.SHOW_IMPLICIT);

        tenDS.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
                layoutTenDS.setErrorEnabled(false);
                layoutTenDS.setError("");
            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        btnTao.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String strTen = String.valueOf(tenDS.getText());
                if (strTen.isEmpty()) {
                    layoutTenDS.setErrorEnabled(true);
                    layoutTenDS.setError("*Bắc buộc");
                    return;
                }
                layoutTenDS.setErrorEnabled(false);
                layoutTenDS.setError("");

                String header = "bearer " + MainActivity.accessToken;
                String strNameDS = String.valueOf(tenDS.getText());

                BodyThemDSPhat body = new BodyThemDSPhat(strNameDS);


                ApiServiceV1.apiServiceV1.themDanhSachPhat(body, header).enqueue(new Callback<ThemDSPhat>() {
                    @Override
                    public void onResponse(Call<ThemDSPhat> call, Response<ThemDSPhat> response) {
                        ThemDSPhat res = response.body();

                        if (res != null) {
                            if (res.getErrCode() == 0) {
                                DanhSachPhat ds = res.getData();
                                Bundle bundle = new Bundle();
                                bundle.putSerializable("newData", ds);
                                intent.putExtra("myBundle", bundle);
                                setResult(101, intent);
                                finish();
                            } else {
                                layoutTenDS.setErrorEnabled(true);
                                layoutTenDS.setError(res.getErrMessage());
                                if (res.getStatus() == 401) {
                                    System.exit(0);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ThemDSPhat> call, Throwable t) {
                        Toast.makeText(AddPlayListActivity.this,
                                "Error from them ds phat in AddPlayListActivity",
                                Toast.LENGTH_SHORT).show();
                    }
                });

            }
        });


    }
}