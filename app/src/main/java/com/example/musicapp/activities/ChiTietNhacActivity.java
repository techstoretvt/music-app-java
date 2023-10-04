package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.viewpager.widget.ViewPager;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;
import com.example.musicapp.adapters.ViewPagerAdapter;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.slider.Slider;
import com.google.android.material.tabs.TabLayout;

public class ChiTietNhacActivity extends AppCompatActivity {

    ImageView iconClose, btnRandom, btnLoop, btnMore;
    public static ImageView btnNext, btnPrev;
    TextView txtTypePlay;
    public static TextView totalTime = null, tgHienTai = null;

    public static ImageView btnPausePlay = null;

    public static Slider sliderProgress = null;

    public static Boolean isChiTietNhac = false;
    public static FragmentManager supportFragmentManager;
    public static BsBaiHat md;

    Boolean isStartSlider = false;

    private TabLayout mTabLayout;
    private ViewPager mViewPager;

    LinearLayout layoutControl;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chi_tiet_nhac);

        overridePendingTransition(R.anim.slide_up, R.anim.no_animation);

        //anh xa view va gan gia tri
        anhXaView();

        isChiTietNhac = true;

        //event
        setEvent();

        initUI();
    }


    @Override
    protected void onPause() {
        super.onPause();
        tgHienTai = null;
        isChiTietNhac = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isChiTietNhac = true;
    }

    private void setEvent() {
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
                            .into(BaiHatFragment.imgNhac);
                    BaiHatFragment.txtTenBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getTenBaiHat());
                    BaiHatFragment.txtTenCS.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getCasi().getTenCaSi());
                    BaiHatFragment.txtLoiBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getLoiBaiHat());

                    btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                    if (MediaCustom.position == 0 && MediaCustom.typeDanhSachPhat == 1) {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                    } else {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);
                    }

                    BinhLuanFragment.getListComment(MediaCustom.danhSachPhats.get(MediaCustom.position)
                            .getId());
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
                            .into(BaiHatFragment.imgNhac);
                    BaiHatFragment.txtTenBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getTenBaiHat());
                    BaiHatFragment.txtTenCS.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getCasi().getTenCaSi());
                    BaiHatFragment.txtLoiBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                            getLoiBaiHat());

                    btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                    if (MediaCustom.position == 0 && MediaCustom.typeDanhSachPhat == 1) {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                    } else {
                        btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);
                    }

                    BinhLuanFragment.getListComment(MediaCustom.danhSachPhats.get(MediaCustom.position)
                            .getId());
                }
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idBH = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();
                BaiHatAdapter.idBaiHat = idBH;
                md = new BsBaiHat();
                supportFragmentManager = getSupportFragmentManager();
                md.show(supportFragmentManager, BsBaiHat.TAG);

                BaiHatAdapter.idCaSi = MediaCustom.danhSachPhats.get(MediaCustom.position)
                        .getCasi().getId();
                BaiHatAdapter.currentBaiHat = MediaCustom.danhSachPhats.get(MediaCustom.position);
                BaiHatAdapter.iconDownLoad = null;
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaCustom.isRandom = !MediaCustom.isRandom;
                if (MediaCustom.isRandom) {
                    btnRandom.setImageResource(R.drawable.random_enable);
                    MediaCustom.isLoop = false;
                    btnLoop.setImageResource(R.drawable.arrows_rotate_solid);
                } else {
                    btnRandom.setImageResource(R.drawable.random);
                }
                if (MediaCustom.typeDanhSachPhat == 1) {
                    BaiHatAdapter.getListRandomBaiHat(MediaCustom.danhSachPhats.get(MediaCustom.position));
                } else if (MediaCustom.typeDanhSachPhat == 2) {
                    ThongTinBaiHatFragment.getData();
                }

            }
        });

        btnLoop.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaCustom.isLoop == false) {
                    MediaCustom.isLoop = true;
                    MediaCustom.typeLoop = 1;
                    btnLoop.setImageResource(R.drawable.arrows_rotate_solid_enable);
                    MediaCustom.isRandom = false;
                    btnRandom.setImageResource(R.drawable.random);

                } else {
                    if (MediaCustom.typeLoop == 1) {
                        MediaCustom.typeLoop = 2;
                        btnLoop.setImageResource(R.drawable.arrows_rotate_solid_one);
                        MediaCustom.isRandom = false;
                        btnRandom.setImageResource(R.drawable.random);
                    } else {
                        MediaCustom.isLoop = false;
                        MediaCustom.typeLoop = 1;
                        btnLoop.setImageResource(R.drawable.arrows_rotate_solid);
                    }
                }
            }
        });

        iconClose.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();

            }
        });
    }

    private void initUI() {
        if (MediaCustom.isPlay) {
            btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
        } else {
            btnPausePlay.setImageResource(R.drawable.baseline_play_arrow_white);
        }

        if (MediaCustom.isRandom) {
            btnRandom.setImageResource(R.drawable.random_enable);
            btnLoop.setImageResource(R.drawable.arrows_rotate_solid);
        } else {
            btnRandom.setImageResource(R.drawable.random);
        }

        if (MediaCustom.typeDanhSachPhat == 1) {

            if (MediaCustom.position == 0) {
                btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
            }
        }

        txtTypePlay.setText("#" + MediaCustom.tenLoai);

        mTabLayout = findViewById(R.id.tab_layout);
        mViewPager = findViewById(R.id.view_pager);

        ViewPagerAdapter viewPagerAdapter = new ViewPagerAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerAdapter);
        mViewPager.setCurrentItem(1);

        mTabLayout.setupWithViewPager(mViewPager);
        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {
//                Log.e("vao 1", "");
                if (position == 0) {
                    layoutControl.setVisibility(View.VISIBLE);
                } else if (position == 1) {
                    layoutControl.setVisibility(View.VISIBLE);
                } else if (position == 2) {
                    layoutControl.setVisibility(View.GONE);
                }
            }

            @Override
            public void onPageSelected(int position) {
                Log.e("vao 2", "");

            }

            @Override
            public void onPageScrollStateChanged(int state) {
//                Log.e("vao 3", "");


            }
        });


        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#0f172a"), Color.parseColor("#581c87"),
                        Color.parseColor("#0f172a")}
        );
        LinearLayout layoutChitietbaihat = findViewById(R.id.layoutChitietbaihat);
        layoutChitietbaihat.setBackground(gradientDrawable);
    }

    private void anhXaView() {
        iconClose = findViewById(R.id.iconClose);
        btnPrev = findViewById(R.id.btnPrev);
        btnPausePlay = findViewById(R.id.btnPausePlay);
        btnNext = findViewById(R.id.btnNext);
        totalTime = findViewById(R.id.totalTime);
        tgHienTai = findViewById(R.id.tgHienTai);
        sliderProgress = findViewById(R.id.sliderProgress);

        txtTypePlay = findViewById(R.id.txtTypePlay);
        btnRandom = findViewById(R.id.btnRandom);
        btnLoop = findViewById(R.id.btnLoop);
        btnMore = findViewById(R.id.btnMore);
        layoutControl = findViewById(R.id.layoutControl);

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
                                    if (tgHienTai != null)
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