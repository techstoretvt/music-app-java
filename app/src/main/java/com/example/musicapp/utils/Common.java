package com.example.musicapp.utils;


import android.content.ContentUris;
import android.content.ContentValues;
import android.content.Context;
import android.database.Cursor;
import android.media.RingtoneManager;
import android.net.Uri;
import android.provider.MediaStore;
import android.util.Log;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;

import java.io.File;
import java.util.ArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

//mở lại activity
//Intent intent = new Intent(MVBaiHatActivity.this, MainActivity.class);
//intent.addFlags(Intent.FLAG_ACTIVITY_REORDER_TO_FRONT);
//startActivity(intent);

//search trên google
//Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//intent.putExtra(SearchManager.QUERY, BaiHatAdapter.linkMV);

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


    //check từ khóa
    public static Boolean checkTuKhoa(String value) {
        Boolean check = true;

        ArrayList<String> listKeyword = new ArrayList<>();
        listKeyword.add("concac");
        listKeyword.add("sex");
        listKeyword.add("phim sex");
        listKeyword.add("phim sẽ");
        listKeyword.add("segay");
        listKeyword.add("cặc");
        listKeyword.add("lồn");

        if (listKeyword.contains(value.toLowerCase()))
            check = false;
        return check;
    }

    public static void setRingtone(Context context, String path) {
        Log.e("Nhac chuong", "setRingtone " + path);
        if (path == null) {

//            Toast.makeText(context, "Vui lòng thử lại sau", Toast.LENGTH_SHORT).show();
            return;
        }
        File file = new File(path);
        ContentValues contentValues = new ContentValues();
        contentValues.put(MediaStore.MediaColumns.DATA, file.getAbsolutePath());
        String filterName = path.substring(path.lastIndexOf("/") + 1);
        contentValues.put(MediaStore.MediaColumns.TITLE, filterName);
        contentValues.put(MediaStore.MediaColumns.MIME_TYPE, "audio/mp3");
        contentValues.put(MediaStore.MediaColumns.SIZE, file.length());
        contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);

        Uri uri = MediaStore.Audio.Media.getContentUriForPath(path);
        Cursor cursor = context.getContentResolver().query(uri, null, MediaStore.MediaColumns.DATA + "=?", new String[]{path}, null);
        if (cursor != null && cursor.moveToFirst() && cursor.getCount() > 0) {
            String id = cursor.getString(0);
            contentValues.put(MediaStore.Audio.Media.IS_RINGTONE, true);
            context.getContentResolver().update(uri, contentValues, MediaStore.MediaColumns.DATA + "=?", new String[]{path});
            Uri newuri = ContentUris.withAppendedId(uri, Long.valueOf(id));
            Log.e("Nhac chuong", "vao 1");
            try {
                RingtoneManager.setActualDefaultRingtoneUri(context, RingtoneManager.TYPE_RINGTONE, newuri);
                Toast.makeText(context.getApplicationContext(), "Đã cài làm nhạc chuông", Toast.LENGTH_SHORT).show();
                Log.e("Nhac chuong", "vao 2");
            } catch (Throwable t) {
                Toast.makeText(context, "Cài làm nhạc chuông thất bại", Toast.LENGTH_SHORT).show();
                t.printStackTrace();
                Log.e("Nhac chuong", "vao 3");
            }
            DownloadReceiver.isNhacChuong = false;
            cursor.close();
        } else {
            DownloadReceiver.isNhacChuong = false;
        }
    }

}
