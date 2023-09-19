package com.example.musicapp.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import android.animation.ObjectAnimator;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.fragments.BottomSheetThemBHVaoDS;
import com.example.musicapp.fragments.CaNhanFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.fragments.ModalBottomSheet;
import com.example.musicapp.fragments.ThongBaoFragment;
import com.example.musicapp.fragments.ThuVienFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    public static LinearLayout layoutTencasi;

    public static LinearLayout layoutAudio;
    public static ImageView imgNhac, btnPrev, dungNhac = null;
    ImageView btnNext;
    public static TextView txtTenBaiHat, txtTenCaSi;

    public static BaiHat currentBaiHat = null;

    public static Boolean isPlay = false;

    public static int typePlay = 0; //0: khong co - 1: kham pha
    public static int positionPlay = -1;

    public static String accessToken;

    public static FragmentManager fragmentManager2;

    private long lastBackPressedTime;

    public static Boolean isChiTietThuVien = false;

    public static String idDanhSachPhat = null;

    public static FragmentManager supportFragmentManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replace_fragment(new KhamPhaFragment());

        anhXaView();

//        BottomSheetThemBHVaoDS md = new BottomSheetThemBHVaoDS();
//        md.show(MainActivity.supportFragmentManager, BottomSheetThemBHVaoDS.TAG);

//        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
//        SharedPreferences.Editor editor = sharedPreferences.edit();
//
//        editor.remove("accessToken");
//        editor.remove("refreshToken");
//        editor.apply();
        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);

        accessToken = sharedPreferences.getString("accessToken", null);

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

        //play/pause nhac
        dungNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isPlay) {
                    dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
                    MediaCustom.pause();
                } else {
                    dungNhac.setImageResource(R.drawable.baseline_pause_24);
                    MediaCustom.play();
                }

            }
        });

        //btn next
        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.typePlay != 0) {
                    if (MainActivity.typePlay == 1 && MainActivity.positionPlay != -1) {
                        int len = KhamPhaFragment.list.size();
                        int newPosition = (MainActivity.positionPlay + 1) % len;
                        MainActivity.positionPlay = newPosition;
                        String url = KhamPhaFragment.list.get(newPosition).getLinkBaiHat();
                        MediaCustom.phatNhac(url);

                        String anhBia = KhamPhaFragment.list.get(newPosition).getAnhBia();
                        Glide.with(MainActivity.this).load(anhBia)
                                .into(imgNhac);
                        txtTenBaiHat.setText(KhamPhaFragment.list.get(newPosition).getTenBaiHat());
                        txtTenCaSi.setText(KhamPhaFragment.list.get(newPosition).getCasi().getTenCaSi());
                        dungNhac.setImageResource(R.drawable.baseline_pause_24);

                        if (newPosition > 0) {
                            btnPrev.setVisibility(View.VISIBLE);
                        } else {
                            btnPrev.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.typePlay != 0) {
                    if (MainActivity.typePlay == 1 && MainActivity.positionPlay != -1) {
                        if (MainActivity.positionPlay == 0) return;

                        int newPosition = MainActivity.positionPlay - 1;
                        MainActivity.positionPlay = newPosition;
                        String url = KhamPhaFragment.list.get(newPosition).getLinkBaiHat();
                        MediaCustom.phatNhac(url);

                        String anhBia = KhamPhaFragment.list.get(newPosition).getAnhBia();
                        Glide.with(MainActivity.this).load(anhBia)
                                .into(imgNhac);
                        txtTenBaiHat.setText(KhamPhaFragment.list.get(newPosition).getTenBaiHat());
                        txtTenCaSi.setText(KhamPhaFragment.list.get(newPosition).getCasi().getTenCaSi());
                        dungNhac.setImageResource(R.drawable.baseline_pause_24);

                        if (newPosition == 0) {
                            btnPrev.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });

        //mo chi tiet nhac
        layoutTencasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (currentBaiHat != null) {
                    String tenBh = currentBaiHat.getTenBaiHat();
                    String tenCs = currentBaiHat.getCasi().getTenCaSi();
                    String loiBh = currentBaiHat.getLoiBaiHat();
                    String anhBia = currentBaiHat.getAnhBia();

                    Intent intent = new Intent(MainActivity.this, ChiTietNhacActivity.class);
                    intent.putExtra("tenBH", tenBh);
                    intent.putExtra("tenCS", tenCs);
                    intent.putExtra("loiBH", loiBh);
                    intent.putExtra("anhBia", anhBia);

                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);


                }

            }
        });

    }

    @Override
    public void onBackPressed() {

        if (isChiTietThuVien) {
            replace_fragment(new ThuVienFragment());
            isChiTietThuVien = false;
            return;
        }

        if (System.currentTimeMillis() - lastBackPressedTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        }
        lastBackPressedTime = System.currentTimeMillis();
    }

    private void anhXaView() {
        layoutAudio = findViewById(R.id.layoutAudio);
        imgNhac = findViewById(R.id.imgNhac);
        txtTenBaiHat = findViewById(R.id.tenBaiHat);
        txtTenCaSi = findViewById(R.id.tenCaSi);
        dungNhac = findViewById(R.id.dungNhac);
        layoutTencasi = findViewById(R.id.layoutTencasi);
        btnPrev = findViewById(R.id.btnPrev);
        btnNext = findViewById(R.id.btnNext);
        supportFragmentManager = getSupportFragmentManager();

    }

    private void replace_fragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager2 = fragmentManager;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    //phat nhac mini
    public static void phatNhacMini(String urlAnh, String tenbh, String tencasi) {
        String tenbhCu = (String) txtTenBaiHat.getText();
        if (tenbhCu.equals(tenbh)) {
            return;
        }
        layoutAudio.setVisibility(View.VISIBLE);
        txtTenBaiHat.setText(tenbh);
        txtTenCaSi.setText(tencasi);
        Glide.with(MainActivity.imgNhac.getContext()).load(urlAnh).into(imgNhac);

        ObjectAnimator animator = ObjectAnimator.ofFloat(imgNhac, "rotation", 0, 360);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();

    }


}


