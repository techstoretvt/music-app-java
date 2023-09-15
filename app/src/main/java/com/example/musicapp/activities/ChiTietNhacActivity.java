package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.AnimationUtils;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.slider.Slider;


public class ChiTietNhacActivity extends AppCompatActivity {

    ImageView iconClose, imgNhac, btnPrev, btnNext, btnRandom, btnLoop;

    TextView totalTime = null, tgHienTai = null, txtTenBH, txtTenCS, txtLoiBH, txtTypePlay;

    Slider sliderProgress = null;
    public static ImageView btnPausePlay = null;

    Boolean isStartSlider = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhac);

        overridePendingTransition(R.anim.slide_up, R.anim.fade_out);

        //anh xa view va gan gia tri
        anhXaView();
        Intent intent = getIntent();
        Glide.with(ChiTietNhacActivity.this).load(intent.getStringExtra("anhBia"))
                .into(imgNhac);
        txtTenBH.setText(intent.getStringExtra("tenBH"));
        txtTenCS.setText(intent.getStringExtra("tenCS"));
        txtLoiBH.setText(intent.getStringExtra("loiBH"));

        if (MainActivity.typePlay == 1) {
            txtTypePlay.setText("#Khám phá");
            btnRandom.setVisibility(View.GONE);
            btnLoop.setVisibility(View.GONE);
        }
        if (MainActivity.positionPlay == 0) {
            btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
        }


        //su kien close
        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(ChiTietNhacActivity.this, MainActivity.class);
                intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                startActivity(intent);

            }
        });

        //animtion anh nhac
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgNhac, "rotation", 0, 360);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();

        //event
        btnPausePlay.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MainActivity.isPlay) {
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
                if (MainActivity.typePlay != 0) {
                    if (MainActivity.typePlay == 1 && MainActivity.positionPlay != -1) {
                        int len = KhamPhaFragment.list.size();
                        int newPosition = (MainActivity.positionPlay + 1) % len;
                        MainActivity.positionPlay = newPosition;
                        String url = KhamPhaFragment.list.get(newPosition).getLinkBaiHat();
                        MediaCustom.phatNhac(url);

                        //update giao dien
                        tgHienTai.setText(MediaCustom.getStrCurrentTime());
                        totalTime.setText(MediaCustom.strTotalTime);
                        sliderProgress.setValueTo(MediaCustom.totalTime);
                        String anhBia = KhamPhaFragment.list.get(newPosition).getAnhBia();
                        Glide.with(ChiTietNhacActivity.this).load(anhBia)
                                .into(imgNhac);
                        txtTenBH.setText(KhamPhaFragment.list.get(newPosition).getTenBaiHat());
                        txtTenCS.setText(KhamPhaFragment.list.get(newPosition).getCasi().getTenCaSi());
                        txtLoiBH.setText(KhamPhaFragment.list.get(newPosition).getLoiBaiHat());

                        btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                        if (newPosition > 0) {
                            btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);
                            MainActivity.btnPrev.setVisibility(View.VISIBLE);
                        } else {
                            btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                            MainActivity.btnPrev.setVisibility(View.GONE);
                        }

                        //tai them danh sach

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

                        //update giao dien
                        tgHienTai.setText(MediaCustom.getStrCurrentTime());
                        totalTime.setText(MediaCustom.strTotalTime);
                        sliderProgress.setValueTo(MediaCustom.totalTime);
                        String anhBia = KhamPhaFragment.list.get(newPosition).getAnhBia();
                        Glide.with(ChiTietNhacActivity.this).load(anhBia)
                                .into(imgNhac);
                        txtTenBH.setText(KhamPhaFragment.list.get(newPosition).getTenBaiHat());
                        txtTenCS.setText(KhamPhaFragment.list.get(newPosition).getCasi().getTenCaSi());
                        txtLoiBH.setText(KhamPhaFragment.list.get(newPosition).getLoiBaiHat());

                        btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                        if (newPosition == 0) {
                            btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                            MainActivity.btnPrev.setVisibility(View.GONE);
                        }
                    }
                }
            }
        });
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        tgHienTai = null;
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

        if (MediaCustom.isData) {
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
                                if (MainActivity.isPlay) {
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

        if (MainActivity.isPlay) {
            btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
        } else {
            btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
        }
    }

}