package com.example.musicapp.fragments;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.example.musicapp.R;
import com.example.musicapp.modal.anhxajson.BaiHatComparator;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.Collections;


public class BsSapXepYeuThich extends BottomSheetDialogFragment {

    public static String TAG = "ModalYeuThichBaiHat";

    LinearLayout laMoiNhat, laCuNhat, laAdenZ, laZdenA;
    ImageView checkMoiNhat, checkCuNhat, checkAdenA, checkZdenA;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_bs_sap_xep_yeu_thich, container, false);
        laMoiNhat = view.findViewById(R.id.laMoiNhat);
        laCuNhat = view.findViewById(R.id.laCuNhat);
        laAdenZ = view.findViewById(R.id.laAdenZ);
        laZdenA = view.findViewById(R.id.laZdenA);

        checkMoiNhat = view.findViewById(R.id.checkMoiNhat);
        checkCuNhat = view.findViewById(R.id.checkCuNhat);
        checkAdenA = view.findViewById(R.id.checkAdenZ);
        checkZdenA = view.findViewById(R.id.checkZdenA);

        initCheck();

        initEvent();

        return view;
    }

    private void initEvent() {
        laMoiNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YeuThichFragment.Filter = 1;
                YeuThichFragment.getListBaiHat();
                initCheck();
            }
        });
        laCuNhat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YeuThichFragment.Filter = 2;
                YeuThichFragment.getListBaiHat();
                initCheck();
            }
        });
        laAdenZ.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YeuThichFragment.Filter = 3;
                initCheck();

                Collections.sort(YeuThichFragment.data, new BaiHatComparator());
                YeuThichFragment.adapter.notifyDataSetChanged();

                if (MediaCustom.tenLoai.equals("Yêu thích")) {
                    MediaCustom.danhSachPhats = YeuThichFragment.data;
                }
            }
        });
        laZdenA.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                YeuThichFragment.Filter = 4;
                initCheck();

                Collections.sort(YeuThichFragment.data, new BaiHatComparator().reversed());
                YeuThichFragment.adapter.notifyDataSetChanged();

                if (MediaCustom.tenLoai.equals("Yêu thích")) {
                    MediaCustom.danhSachPhats = YeuThichFragment.data;
                }

            }
        });
    }

    private void initCheck() {
        if (YeuThichFragment.Filter == 1) {
            checkMoiNhat.setVisibility(View.VISIBLE);
            checkCuNhat.setVisibility(View.GONE);
            checkAdenA.setVisibility(View.GONE);
            checkZdenA.setVisibility(View.GONE);
        } else if (YeuThichFragment.Filter == 2) {
            checkMoiNhat.setVisibility(View.GONE);
            checkCuNhat.setVisibility(View.VISIBLE);
            checkAdenA.setVisibility(View.GONE);
            checkZdenA.setVisibility(View.GONE);
        } else if (YeuThichFragment.Filter == 3) {
            checkMoiNhat.setVisibility(View.GONE);
            checkCuNhat.setVisibility(View.GONE);
            checkAdenA.setVisibility(View.VISIBLE);
            checkZdenA.setVisibility(View.GONE);
        } else if (YeuThichFragment.Filter == 4) {
            checkMoiNhat.setVisibility(View.GONE);
            checkCuNhat.setVisibility(View.GONE);
            checkAdenA.setVisibility(View.GONE);
            checkZdenA.setVisibility(View.VISIBLE);
        }
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
