package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;

import com.example.musicapp.R;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

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


            }
        });


    }
}