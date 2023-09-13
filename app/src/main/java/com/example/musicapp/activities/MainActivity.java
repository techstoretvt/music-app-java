package com.example.musicapp.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.fragments.CaNhanFragment;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.fragments.ThongBaoFragment;
import com.example.musicapp.fragments.ThuVienFragment;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replace_fragment(new KhamPhaFragment());


//        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.remove("accessToken");
//        editor.remove("refreshToken");
//        editor.apply();

        //binding bottom tabs
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.khamPha) {
                replace_fragment(new KhamPhaFragment());
            } else if (item.getItemId() == R.id.thuVien) {
                replace_fragment(new ThuVienFragment());
            } else if (item.getItemId() == R.id.thongBao) {
                replace_fragment(new ThongBaoFragment());
            } else if (item.getItemId() == R.id.caNhan) {
                replace_fragment(new CaNhanFragment());
            }

            return true;
        });


    }

    private void replace_fragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


}


