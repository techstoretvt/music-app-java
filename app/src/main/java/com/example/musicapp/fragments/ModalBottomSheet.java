package com.example.musicapp.fragments;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.adapters.AdapterThuVien;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

public class ModalBottomSheet extends BottomSheetDialogFragment {

    public static String TAG = "ModalBottomSheet";

    public static BottomSheetThemBHVaoDS md = new BottomSheetThemBHVaoDS();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.modal_bottom_sheet_content, container, false);
        LinearLayout layoutThemVaoDS = view.findViewById(R.id.layoutThemVaoDS);
        LinearLayout layoutTaiVe = view.findViewById(R.id.layoutTaiVe);
        LinearLayout layoutPhatKeTiep = view.findViewById(R.id.layoutPhatKeTiep);
        LinearLayout layoutXemAlbum = view.findViewById(R.id.layoutXemAlbum);
        LinearLayout layoutXemNS = view.findViewById(R.id.layoutXemNS);
        LinearLayout layoutChan = view.findViewById(R.id.layoutChan);


        layoutThemVaoDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                AdapterKhamPha.md.dismiss();


                md.show(MainActivity.supportFragmentManager, BottomSheetThemBHVaoDS.TAG);
            }
        });

        layoutTaiVe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        layoutPhatKeTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        layoutXemAlbum.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        layoutXemNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        layoutChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });
        return view;
    }

    
}
