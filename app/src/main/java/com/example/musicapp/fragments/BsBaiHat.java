package com.example.musicapp.fragments;

import android.Manifest;
import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.app.ActivityCompat;

import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.DownloadReceiver;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BsBaiHat extends BottomSheetDialogFragment {

    public static String TAG = "ModalBottomSheet";

    public static BottomSheetThemBHVaoDS md = new BottomSheetThemBHVaoDS();

    public static ImageView iconDownLoad = null;
    public static BaiHat currentBaiHat = null;

    ImageView iconYeuThich;
    TextView txtYeuThich;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_bs_baihat, container, false);
        LinearLayout layoutThemVaoDS = view.findViewById(R.id.layoutThemVaoDS);
        LinearLayout layoutTaiVe = view.findViewById(R.id.layoutTaiVe);
        LinearLayout layoutPhatKeTiep = view.findViewById(R.id.layoutPhatKeTiep);
        LinearLayout layoutXemMV = view.findViewById(R.id.layoutXemMV);
        LinearLayout layoutXemNS = view.findViewById(R.id.layoutXemNS);
        LinearLayout layoutChan = view.findViewById(R.id.layoutChan);
        LinearLayout removeBH = view.findViewById(R.id.layoutRemove);
        LinearLayout layoutYeuThich = view.findViewById(R.id.layouYeuThich);
        iconYeuThich = view.findViewById(R.id.iconYeuThich);
        txtYeuThich = view.findViewById(R.id.txtYeuThich);

        checkYeuThich();

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

        if (ChiTietNhacActivity.isChiTietNhac || DaTaiFragment.isFragmentDaTai) {
            layoutTaiVe.setVisibility(View.GONE);
        }

        if (BaiHatAdapter.linkMV == null || BaiHatAdapter.linkMV.equals("false")) {
            layoutXemMV.setVisibility(View.GONE);
        } else {
            layoutXemMV.setVisibility(View.VISIBLE);
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


                if (DownloadReceiver.isDownload) {
                    Toast.makeText(getContext(), "Đang tải file khác 1",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                iconDownLoad = BaiHatAdapter.iconDownLoad;
                currentBaiHat = BaiHatAdapter.currentBaiHat;


                // Kiểm tra xem người dùng đã cấp quyền chưa
                if (ActivityCompat.shouldShowRequestPermissionRationale(getActivity(),
                        Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
                    // Giải thích lý do tại sao ứng dụng của bạn cần quyền
                    Toast.makeText(getContext(),
                            "Ứng dụng cần quyền truy cập bộ nhớ để lưu trữ dữ liệu",
                            Toast.LENGTH_SHORT).show();
                } else {
                    // Yêu cầu quyền truy cập bộ nhớ
                    ActivityCompat.requestPermissions(getActivity(), new String[]{
                            Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
                }


            }
        });

        layoutPhatKeTiep.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();
            }
        });

        layoutXemMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
                intent.putExtra(SearchManager.QUERY, BaiHatAdapter.linkMV);
                startActivity(intent);
            }
        });

        layoutXemNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChiTietCaSiFragment.idCaSi = BaiHatAdapter.idCaSi;

                //set type back
                if (ChiTietThuVienFragment.isChiTietDS && !ChiTietNhacActivity.isChiTietNhac) {
                    ChiTietCaSiFragment.typeBack = 1;
                    ChiTietCaSiFragment.idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;

                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else if (ChiTietNhacActivity.isChiTietNhac) {
//                    BaiHatAdapter.md.dismiss();
                    getActivity().finish();
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
//                    ChiTietCaSiFragment.typeBack = 2;
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Common.replace_fragment(new ChiTietCaSiFragment());
                        }
                    }, 500);


                } else if (YeuThichFragment.isYeuThich) {
                    ChiTietCaSiFragment.typeBack = 4;
                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else {
                    ChiTietCaSiFragment.typeBack = 0;
                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();
                }

            }
        });

        layoutChan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Toast.makeText(getContext(), "Tính năng này chưa cập nhật", Toast.LENGTH_SHORT)
                        .show();

            }
        });

        layoutYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = Common.getHeader();
                String idBaiHat = BaiHatAdapter.idBaiHat;

                ApiServiceV1.apiServiceV1.toggleLikeBaiHat(idBaiHat, header).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                        ResponseDefault res = response.body();
                        if (res != null) {
                            if (res.getErrCode() == 0) {
                                if (res.getErrMessage().equals("like")) {
                                    iconYeuThich.setImageResource(R.drawable.baseline_favorite_red);
                                    txtYeuThich.setText("Đã thêm vao yêu thích");
                                    if (BaiHatFragment.btnThaTim != null)
                                        BaiHatFragment.btnThaTim.setImageResource(R.drawable.baseline_favorite_red);
                                } else {
                                    iconYeuThich.setImageResource(R.drawable.baseline_favorite_24);
                                    txtYeuThich.setText("Thêm vao yêu thích");
                                    if (BaiHatFragment.btnThaTim != null)
                                        BaiHatFragment.btnThaTim.setImageResource(R.drawable.baseline_favorite_24);
                                }
                            } else {
                                if (res.getStatus() == 401) {
                                    System.exit(0);
                                }
                                Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                        .show();
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDefault> call, Throwable t) {
                        Log.e("Loi", "Loi kiem tra yeu thich bai hat");
                    }
                });
            }
        });

        return view;
    }

    private void checkYeuThich() {
        String header = Common.getHeader();
        String idBaiHat = BaiHatAdapter.idBaiHat;

        ApiServiceV1.apiServiceV1.kiemTraYeuThichBaiHat(idBaiHat, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        if (res.getErrMessage().equals("like")) {
                            iconYeuThich.setImageResource(R.drawable.baseline_favorite_red);
                            txtYeuThich.setText("Đã thêm vao yêu thích");
                        } else {
                            iconYeuThich.setImageResource(R.drawable.baseline_favorite_24);
                            txtYeuThich.setText("Thêm vao yêu thích");
                        }
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi", "Loi kiem tra yeu thich bai hat");
            }
        });
    }


    @Override
    public void onDestroy() {
        super.onDestroy();
    }


}
