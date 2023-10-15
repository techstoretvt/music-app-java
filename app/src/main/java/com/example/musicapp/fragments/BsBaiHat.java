package com.example.musicapp.fragments;

import android.Manifest;
import android.app.AlertDialog;
import android.app.SearchManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.database.Cursor;
import android.graphics.Color;
import android.os.Bundle;
import android.os.CountDownTimer;
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

import com.airbnb.lottie.LottieAnimationView;
import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MVBaiHatActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.DownloadReceiver;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.android.material.timepicker.MaterialTimePicker;
import com.google.android.material.timepicker.TimeFormat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BsBaiHat extends BottomSheetDialogFragment {

    public static String TAG = "ModalBottomSheet";

    public static BottomSheetThemBHVaoDS md = new BottomSheetThemBHVaoDS();

    public static ImageView iconDownLoad = null, ic_phatKeTiep = null;
    public static BaiHat currentBaiHat = null;

    ImageView iconYeuThich, anhBaiHat;
    TextView txtYeuThich, tenCaSi, tenBaiHat;

    public static TextView txtHenGio;
    LinearLayout layoutExtends, btnHenGio, layoutNhacChuong;

    public static MaterialTimePicker picker = null;

    public static CountDownTimer countDownTimer = null;

    public static Boolean isHenGio = false;

    public static ImageView iconHenGio;

    public static LottieAnimationView iconDownload2;
    public static ImageView iconDownLoad1;

    LinearLayout layoutThemVaoDS;
    LinearLayout layoutTaiVe;
    LinearLayout layoutPhatKeTiep;
    LinearLayout layoutXemMV;
    LinearLayout layoutXemNS;
    LinearLayout layoutChan;
    LinearLayout removeBH;
    LinearLayout layoutYeuThich;


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.content_bs_baihat, container, false);

        anhXa(view);

        initUi();

        checkYeuThich();

        setEvent();

        return view;
    }

    private void setEvent() {
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
                if (MediaCustom.danhSachPhats != null) {
                    MediaCustom.danhSachPhats.add(MediaCustom.position + 1, BaiHatAdapter.currentBaiHat);
                    if (ic_phatKeTiep != null)
                        ic_phatKeTiep.setImageResource(R.drawable.list_check);
                }
            }
        });

        layoutXemMV.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), MVBaiHatActivity.class);
                intent.putExtra("idBaiHat", BaiHatAdapter.currentBaiHat.getId());

                startActivity(intent);

                BaiHatAdapter.md.dismiss();
            }
        });

        layoutXemNS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (BaiHatAdapter.currentBaiHat.getBaiHat_caSis().size() > 1) {
                    if (ChiTietNhacActivity.isChiTietNhac)
                        BaiHatAdapter.mdXemNS.show(ChiTietNhacActivity.supportFragmentManager, Bs_XemNS.TAG);
                    else {
                        BaiHatAdapter.mdXemNS.show(MainActivity.supportFragmentManager, Bs_XemNS.TAG);
                        BaiHatAdapter.md.dismiss();
                    }

                    return;
                }


                ChiTietCaSiFragment.idCaSi = BaiHatAdapter.currentBaiHat.getBaiHat_caSis()
                        .get(0).getCasi().getId();
                //set type back
                if (ChiTietThuVienFragment.isChiTietDS && !ChiTietNhacActivity.isChiTietNhac) {
                    ChiTietCaSiFragment.typeBack = 1;
                    ChiTietCaSiFragment.idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;

                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else if (ChiTietNhacActivity.isChiTietNhac) {
                    getActivity().finish();
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

                } else if (CT_ThuVien_NoiBatFragment.isChiTietDSNoiBat) {
                    ChiTietCaSiFragment.typeBack = 5;
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


        btnHenGio.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                picker.show(getParentFragmentManager(), "tag");
            }
        });

        btnHenGio.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                if (!isHenGio) return false;
                Log.e("vao", "");

                AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
                builder.setTitle("Dừng hẹn giờ");
                builder.setMessage("Bạn có chắc chắn muốn dừng hẹn giờ.");
                builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialogInterface, int i) {
                        if (countDownTimer != null) {
                            countDownTimer.cancel();
                            isHenGio = false;

                            if (txtHenGio != null) {
                                txtHenGio.setText("Hẹn giờ");
                                txtHenGio.setTextColor(Color.WHITE);
                            }
                            if (iconHenGio != null)
                                iconHenGio.setImageResource(R.drawable.ic_clock);
                        }
                    }
                });

                builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        // Xử lý khi người dùng nhấn vào nút Hủy
                    }
                });

                builder.show();


                return true;
            }
        });

        picker.addOnPositiveButtonClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int hour = picker.getHour();
                int minute = picker.getMinute();

                long timestamp = minute * 60000;
                timestamp += hour * 3600000;


                if (countDownTimer != null) {
                    countDownTimer.cancel();
                }
                isHenGio = true;
                long finalTimestamp = timestamp;
                countDownTimer = new CountDownTimer(finalTimestamp, 1000) {
                    @Override
                    public void onTick(long millisUntilFinished) {
                        // Cập nhật thời gian còn lại trên bong bóng nổi
                        Log.e("dang dem", String.valueOf(millisUntilFinished));
                        int tgConLai = (int) millisUntilFinished / 1000;
                        if (txtHenGio != null) {
                            txtHenGio.setText(String.valueOf(tgConLai) + "s");
                            txtHenGio.setTextColor(Color.parseColor("#52267D"));
                        }
                        if (iconHenGio != null)
                            iconHenGio.setImageResource(R.drawable.clock_active);
                    }

                    @Override
                    public void onFinish() {
                        MediaCustom.pause();
                        Log.e("ket thuc", String.valueOf(finalTimestamp));
                        isHenGio = false;

                        if (txtHenGio != null) {
                            txtHenGio.setText("Hẹn giờ");
                            txtHenGio.setTextColor(Color.WHITE);
                        }
                        if (iconHenGio != null)
                            iconHenGio.setImageResource(R.drawable.ic_clock);
                    }
                };

                // Bắt đầu đếm ngược
                countDownTimer.start();


            }
        });

        layoutNhacChuong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (DownloadReceiver.isDownload) {
                    Toast.makeText(getContext(), "Đang tải file khác 1",
                            Toast.LENGTH_SHORT).show();
                    return;
                }

                iconDownLoad = BaiHatAdapter.iconDownLoad;
                currentBaiHat = BaiHatAdapter.currentBaiHat;
                DownloadReceiver.isNhacChuong = true;

                layoutNhacChuong.setBackgroundColor(Color.parseColor("#737373"));


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
    }

    private void initUi() {
        BaiHat currentBH = BaiHatAdapter.currentBaiHat;
        tenBaiHat.setText(currentBH.getTenBaiHat());


        String strTenCaSi = "";
        for (int i = 0; i < currentBH.getBaiHat_caSis().size(); i++) {
            if (i == 0)
                strTenCaSi = currentBH.getBaiHat_caSis().
                        get(i).getCasi().getTenCaSi();
            else
                strTenCaSi += ", " + currentBH.getBaiHat_caSis().
                        get(i).getCasi().getTenCaSi();
        }

        tenCaSi.setText(strTenCaSi);


        Glide.with(getContext()).load(currentBH.getAnhBia()).into(anhBaiHat);

        if (DaTaiFragment.isFragmentDaTai) {
            layoutTaiVe.setVisibility(View.GONE);
        }
//        if (ChiTietNhacActivity.isChiTietNhac) {
//            layoutTaiVe.setVisibility(View.GONE);
//        }

        if (BaiHatAdapter.linkMV == null || BaiHatAdapter.linkMV.equals("false")) {
            layoutXemMV.setVisibility(View.GONE);
        } else {
            layoutXemMV.setVisibility(View.VISIBLE);
        }

        if (ChiTietNhacActivity.isChiTietNhac) {
            layoutExtends.setVisibility(View.VISIBLE);

        }
        if (!DaTaiFragment.isFragmentDaTai) {
            layoutNhacChuong.setVisibility(View.VISIBLE);
        }


        MusicAppHelper musicAppHelper = new MusicAppHelper(getContext(),
                "MusicApp.sqlite", null, 1);

        Cursor dataBaiHat = musicAppHelper.GetData(String.format(
                "SELECT * FROM BaiHat where id = '%s'",
                currentBH.getId()
        ));

        layoutTaiVe.setVisibility(View.VISIBLE);
        while (dataBaiHat.moveToNext()) {
            layoutTaiVe.setVisibility(View.GONE);
        }


    }

    private void anhXa(View view) {
        layoutThemVaoDS = view.findViewById(R.id.layoutThemVaoDS);
        layoutTaiVe = view.findViewById(R.id.layoutTaiVe);
        layoutPhatKeTiep = view.findViewById(R.id.layoutPhatKeTiep);
        layoutXemMV = view.findViewById(R.id.layoutXemMV);
        layoutXemNS = view.findViewById(R.id.layoutXemNS);
        layoutChan = view.findViewById(R.id.layoutChan);
        removeBH = view.findViewById(R.id.layoutRemove);
        layoutYeuThich = view.findViewById(R.id.layouYeuThich);
        iconYeuThich = view.findViewById(R.id.iconYeuThich);
        txtYeuThich = view.findViewById(R.id.txtYeuThich);
        anhBaiHat = view.findViewById(R.id.anhBaiHat);
        tenBaiHat = view.findViewById(R.id.tenBaiHat);
        tenCaSi = view.findViewById(R.id.tenCaSi);
        txtHenGio = view.findViewById(R.id.txtHenGio);
        layoutExtends = view.findViewById(R.id.layoutExtends);
        btnHenGio = view.findViewById(R.id.btnHenGio);
        iconHenGio = view.findViewById(R.id.iconHenGio);
        layoutNhacChuong = view.findViewById(R.id.layoutNhacChuong);
        iconDownload2 = view.findViewById(R.id.iconDownload2);
        iconDownLoad1 = view.findViewById(R.id.iconDownload1);
        ic_phatKeTiep = view.findViewById(R.id.ic_phatKeTiep);

        if (picker == null) {
            picker = new MaterialTimePicker.Builder()
                    .setTimeFormat(TimeFormat.CLOCK_24H)
                    .setHour(12)
                    .setMinute(10)
                    .setTitleText("Chọn thời gian dừng phát")
                    .build();
        }

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
