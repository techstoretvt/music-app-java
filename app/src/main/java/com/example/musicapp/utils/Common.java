package com.example.musicapp.utils;

import android.content.Context;
import android.content.SharedPreferences;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class Common {

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
