package com.example.musicapp.utils;

import android.media.MediaPlayer;
import android.util.Log;

import androidx.fragment.app.Fragment;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;

import java.io.IOException;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

    public static int maxCountBaiHatHome = 15;

    public static String getHeader() {
        return "bearer " + MainActivity.accessToken;
    }

    public static void replace_fragment(Fragment fragment) {
        MainActivity.fragmentManager2.beginTransaction()
                .replace(R.id.frame_layout, fragment)
                .commit();
    }

    public static Boolean isValidEmail(String email) {
        // Biểu thức chính quy để kiểm tra địa chỉ email
        String regex = "^[A-Za-z0-9+_.-]+@(.+)$";

        // Tạo một đối tượng Pattern
        Pattern pattern = Pattern.compile(regex);

        // Tạo một đối tượng Matcher
        Matcher matcher = pattern.matcher(email);

        // Kiểm tra xem chuỗi khớp với biểu thức chính quy hay không
        return matcher.matches();
    }


}
