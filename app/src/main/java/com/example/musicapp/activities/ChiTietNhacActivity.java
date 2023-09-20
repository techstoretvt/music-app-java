package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.slider.Slider;


public class ChiTietNhacActivity extends AppCompatActivity {

    ImageView iconClose, imgNhac, btnPrev, btnNext, btnRandom, btnLoop, btnMore;

    TextView totalTime = null, tgHienTai = null, txtTenBH, txtTenCS, txtLoiBH, txtTypePlay;

    Slider sliderProgress = null;
    public static ImageView btnPausePlay = null;
    public static Boolean isChiTietNhac = false;
    public static FragmentManager supportFragmentManager;
    public static BsBaiHat md;

    Boolean isStartSlider = false;

    ObjectAnimator animator;

    String idBH;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhac);

        overridePendingTransition(R.anim.slide_up, R.anim.fade_out);

        //anh xa view va gan gia tri
        anhXaView();
        isChiTietNhac = true;

        String tenBh = MediaCustom.danhSachPhats.get(MediaCustom.position).getTenBaiHat();
        String tenCs = MediaCustom.danhSachPhats.get(MediaCustom.position).getCasi().getTenCaSi();
        String loiBh = MediaCustom.danhSachPhats.get(MediaCustom.position).getLoiBaiHat();
        String anhBia = MediaCustom.danhSachPhats.get(MediaCustom.position).getAnhBia();
        idBH = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();

        Intent intent = getIntent();
        Glide.with(ChiTietNhacActivity.this).load(anhBia)
                .into(imgNhac);
        txtTenBH.setText(tenBh);
        txtTenCS.setText(tenCs);
        txtLoiBH.setText(loiBh);

        if (MediaCustom.typeDanhSachPhat == 1) {
            txtTypePlay.setText(MediaCustom.tenLoai);
            btnRandom.setVisibility(View.GONE);
            btnLoop.setVisibility(View.GONE);
            if (MediaCustom.position == 0) {
                btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
            }
        }

        if (MediaCustom.isPlay) {
            btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
        } else {
            btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
        }


        //su kien close
        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                Intent intent = new Intent(ChiTietNhacActivity.this, MainActivity.class);
//                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                startActivity(intent);

                finish();

            }
        });

        //animtion anh nhac
        animator = ObjectAnimator.ofFloat(imgNhac, "rotation", 0, 360);
        animator.setDuration(6000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();


        //event
        btnPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaCustom.isPlay) {
                    btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
                    if (MainActivity.dungNhac != null) {
                        MainActivity.dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
                    }
                    MediaCustom.pause();
                } else {
                    btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
                    if (MainActivity.dungNhac != null) {
                        MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);
                    }
                    MediaCustom.play();
                }

            }
        });

        sliderProgress.addOnSliderTouchListener(new Slider.OnSliderTouchListener() {
            @Override
            public void onStartTrackingTouch(@NonNull Slider slider) {
                isStartSlider = true;
            }

            @Override
            public void onStopTrackingTouch(@NonNull Slider slider) {
                isStartSlider = false;
                float value = slider.getValue();
                Log.e("value", String.valueOf(value));
                MediaCustom.setCurrentTime((int) value);
                tgHienTai.setText(MediaCustom.getStrCurrentTime());
            }
        });

        btnNext.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean statusPhatNhac = MediaCustom.next();
                if (statusPhatNhac) {
                    tgHienTai.setText(MediaCustom.getStrCurrentTime());
                    totalTime.setText(MediaCustom.strTotalTime);
                    sliderProgress.setValueTo(MediaCustom.totalTime);
                    String anhBia = MediaCustom.danhSachPhats.get(MediaCustom.position)
                            .getAnhBia();
                    Glide.with(ChiTietNhacActivity.this).load(anhBia)
                            .into(imgNhac);
                    txtTenBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getTenBaiHat());
                    txtTenCS.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getCasi().getTenCaSi());
                    txtLoiBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getLoiBaiHat());
                    btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                    if (MediaCustom.position == 0 && MediaCustom.typeDanhSachPhat == 1) {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                    } else {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);
                    }

                }

            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaCustom.position == 0) return;
                Boolean statusPhatNhac = MediaCustom.prev();
                if (statusPhatNhac) {
                    tgHienTai.setText(MediaCustom.getStrCurrentTime());
                    totalTime.setText(MediaCustom.strTotalTime);
                    sliderProgress.setValueTo(MediaCustom.totalTime);
                    String anhBia = MediaCustom.danhSachPhats.get(MediaCustom.position)
                            .getAnhBia();
                    Glide.with(ChiTietNhacActivity.this).load(anhBia)
                            .into(imgNhac);
                    txtTenBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getTenBaiHat());
                    txtTenCS.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getCasi().getTenCaSi());
                    txtLoiBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getLoiBaiHat());
                    btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                    if (MediaCustom.position == 0 && MediaCustom.typeDanhSachPhat == 1) {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                    } else {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);
                    }
                }
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaiHatAdapter.idBaiHat = idBH;
                md = new BsBaiHat();
                supportFragmentManager = getSupportFragmentManager();
                md.show(supportFragmentManager, BsBaiHat.TAG);
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tgHienTai = null;
        animator.cancel();
        isChiTietNhac = false;
    }

    private void anhXaView() {
        iconClose = findViewById(R.id.iconClose);
        imgNhac = findViewById(R.id.imgNhac);
        btnPrev = findViewById(R.id.btnPrev);
        btnPausePlay = findViewById(R.id.btnPausePlay);
        btnNext = findViewById(R.id.btnNext);
        totalTime = findViewById(R.id.totalTime);
        tgHienTai = findViewById(R.id.tgHienTai);
        sliderProgress = findViewById(R.id.sliderProgress);
        txtTenBH = findViewById(R.id.txtTenBH);
        txtTenCS = findViewById(R.id.txtTenCS);
        txtLoiBH = findViewById(R.id.txtLoiBH);
        txtTypePlay = findViewById(R.id.txtTypePlay);
        btnRandom = findViewById(R.id.btnRandom);
        btnLoop = findViewById(R.id.btnLoop);
        btnMore = findViewById(R.id.btnMore);

        if (MediaCustom.danhSachPhats != null) {
            totalTime.setText(MediaCustom.strTotalTime);
            sliderProgress.setValueTo(MediaCustom.totalTime);

            new Thread(new Runnable() {
                @Override
                public void run() {
                    while (true) {
                        if (tgHienTai == null) break;

                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MediaCustom.isPlay) {
                                    if (!isStartSlider) {
                                        sliderProgress.setValue(MediaCustom.getFloatCurrentTime());
                                    }
                                    tgHienTai.setText(MediaCustom.getStrCurrentTime());

                                }
                            }
                        });

                        // Nghỉ 100 mili giây
                        try {
                            Thread.sleep(100);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            }).start();
        }


    }


}