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
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.utils.Common;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BsBaiHat extends BottomSheetDialogFragment {

    public static String TAG = "ModalBottomSheet";

    public static BottomSheetThemBHVaoDS md = new BottomSheetThemBHVaoDS();

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_bs_baihat, container, false);
        LinearLayout layoutThemVaoDS = view.findViewById(R.id.layoutThemVaoDS);
        LinearLayout layoutTaiVe = view.findViewById(R.id.layoutTaiVe);
        LinearLayout layoutPhatKeTiep = view.findViewById(R.id.layoutPhatKeTiep);
        LinearLayout layoutXemAlbum = view.findViewById(R.id.layoutXemAlbum);
        LinearLayout layoutXemNS = view.findViewById(R.id.layoutXemNS);
        LinearLayout layoutChan = view.findViewById(R.id.layoutChan);
        LinearLayout removeBH = view.findViewById(R.id.layoutRemove);

        if (ChiTietThuVienFragment.isChiTietDS && ChiTietNhacActivity.isChiTietNhac == false) {
            removeBH.setVisibility(View.VISIBLE);

            removeBH.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {

                    String idBaiHat = BaiHatAdapter.idBaiHat;
                    String idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;
                    String header = Common.getHeader();
                    Log.e("vao", "rong");
                    if (idBaiHat == null || idDanhSachPhat == null || idBaiHat.isEmpty() || idDanhSachPhat.isEmpty()) {
                        Log.e("rong", "rong");
                        return;
                    }

                    ApiServiceV1.apiServiceV1.xoaBaiHatKhoiDS(idDanhSachPhat, idBaiHat, header).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                            ResponseDefault res = response.body();
                            if (res != null) {
                                if (res.getErrCode() == 0) {

                                    ArrayList<BaiHat> arr = ChiTietThuVienFragment.danhBaiHats;
                                    int index = 0;
                                    for (int i = 0; i < arr.size(); i++) {
                                        if (arr.get(i).getId().equals(idBaiHat)) {
                                            index = i;
                                        }
                                    }
                                    ChiTietThuVienFragment.danhBaiHats.remove(index);
                                    ChiTietThuVienFragment.adapter.notifyDataSetChanged();
                                    BaiHatAdapter.md.dismiss();
                                } else {
                                    if (res.getStatus() == 401) {
                                        System.exit(0);
                                    }
                                    Log.e("error", res.getErrMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                            Log.e("Loi server", "");
                        }
                    });

                }
            });
        } else {
            removeBH.setVisibility(View.GONE);
        }

        layoutThemVaoDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (ChiTietNhacActivity.isChiTietNhac == false) {
                    BaiHatAdapter.md.dismiss();
                    md.show(MainActivity.supportFragmentManager, BottomSheetThemBHVaoDS.TAG);
                } else {
                    ChiTietNhacActivity.md.dismiss();
                    md.show(ChiTietNhacActivity.supportFragmentManager, BottomSheetThemBHVaoDS.TAG);
                }

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


    @Override
    public void onDestroy() {
        super.onDestroy();
    }
}
