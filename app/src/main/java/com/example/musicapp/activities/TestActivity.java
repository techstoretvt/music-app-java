package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.musicapp.R;

public class TestActivity extends AppCompatActivity {

    Button btnTest;
    TextView txtTest;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTest = findViewById(R.id.btnTest);
        txtTest = findViewById(R.id.txtTest);

        txtTest.setText("<p>Có lẽ anh và em ta nên dừng lại.<br />Có lẽ anh và em mình chia tay.</p>");


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

}