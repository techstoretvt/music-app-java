package com.example.musicapp.utils;

import android.view.View;
import android.widget.Toast;

import androidx.annotation.NonNull;

public class MyOnClickListener implements View.OnClickListener {
    @Override
    public void onClick(@NonNull View view) {
        // Code bạn muốn thực thi khi người dùng nhấp vào ImageView
        Toast.makeText(view.getContext(), "Bạn đã nhấp vào ImageView", Toast.LENGTH_SHORT).show();
    }
}
