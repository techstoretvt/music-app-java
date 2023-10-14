package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.graphics.Point;
import android.os.Bundle;
import android.os.Handler;
import android.util.Log;
import android.util.TypedValue;
import android.view.Display;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.adapters.MVBaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.Bs_XemNS;
import com.example.musicapp.fragments.CT_ThuVien_NoiBatFragment;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.MvBaiHatFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.BaiHat_CaSi;
import com.example.musicapp.modal.anhxajson.GetBaiHatById;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.switchmaterial.SwitchMaterial;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.PlayerConstants;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.YouTubePlayer;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.listeners.AbstractYouTubePlayerListener;
import com.pierfrancescosoffritti.androidyoutubeplayer.core.player.views.YouTubePlayerView;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MVBaiHatActivity extends AppCompatActivity {

    public static YouTubePlayerView youTubePlayerView;

    public static RecyclerView recycleView;
    public static LinearLayoutManager manager;
    public static ImageView imgBH;
    public static TextView tenCS;
    public static TextView tenBH;
    public static ScrollView scrollView;

    public static MVBaiHatAdapter adapter;

    String idBaiHat = "0f5bb886-effd-4d77-b27d-74aafcc78f58";
    public static ArrayList<String> listIdBaiHat = new ArrayList<>();

    public static BaiHat currentBaiHat;

    public static ArrayList<BaiHat> listMV = new ArrayList<>();

    float curentSecond = -1f;

    LinearLayout laCaSi;
    SwitchMaterial autoNext;
    Bs_XemNS mdXemNS = new Bs_XemNS();

    public static boolean isMVBaiHatActivity = false;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_mvbai_hat);

        anhXa();

        Intent intent = getIntent();
        String getIdBaiHat = intent.getStringExtra("idBaiHat");
        idBaiHat = getIdBaiHat;

        listIdBaiHat.add(idBaiHat);
        MediaCustom.pause();
        if (MainActivity.dungNhac != null) {
            MainActivity.dungNhac.setImageResource(R.drawable.baseline_play_arrow_24);
        }
        isMVBaiHatActivity = true;


        getVideoBaiHat(idBaiHat);
        getGoiYMV();

        addEvent();

    }

    private void addEvent() {
        laCaSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                BaiHatAdapter.currentBaiHat = currentBaiHat;

                if (currentBaiHat.getBaiHat_caSis().size() == 1) {
                    ChiTietCaSiFragment.idCaSi = currentBaiHat.getBaiHat_caSis().get(0).getIdCaSi();
                    ChiTietCaSiFragment.typeBack = 6;
                    finish();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Common.replace_fragment(new ChiTietCaSiFragment());
                        }
                    }, 500);
                } else {
                    BaiHatAdapter.currentBaiHat = currentBaiHat;

                    mdXemNS.show(getSupportFragmentManager(), Bs_XemNS.TAG);
                }


            }
        });

        youTubePlayerView.addYouTubePlayerListener(new AbstractYouTubePlayerListener() {

            @Override
            public void onStateChange(@NonNull YouTubePlayer youTubePlayer, @NonNull PlayerConstants.PlayerState state) {
                Log.e("thoiGian: ", String.valueOf(curentSecond));
                Log.e("thoiGian: ", String.valueOf(currentBaiHat.getThoiGian() / 1000));

                boolean checked = autoNext.isChecked();
                Log.e("thoiGian: ", String.valueOf(checked));

                if ((double) curentSecond >= (currentBaiHat.getThoiGian() / 1000) && checked) {
                    Log.e("thoiGian: ", "vao");
                    curentSecond = -1f;
                    getVideoBaiHat(listMV.get(0).getId());
                    getGoiYMV();
                }
            }

            @Override
            public void onCurrentSecond(@NonNull YouTubePlayer youTubePlayer, float second) {
                super.onCurrentSecond(youTubePlayer, second);
                curentSecond = second + 1;
            }
        });
    }

    public static void getGoiYMV() {
        Gson gson = new GsonBuilder().create();
        String json = gson.toJson(listIdBaiHat);

        ApiServiceV1.apiServiceV1.getGoiYMVBaiHat(json).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        listMV = res.getData();

                        adapter = new MVBaiHatAdapter(listMV, MVBaiHatActivity.tenBH.getContext());
                        recycleView.setAdapter(adapter);
                        recycleView.setLayoutManager(manager);

                        WindowManager windowManager = (WindowManager)
                                MVBaiHatActivity.tenCS.getContext().getSystemService(Context.WINDOW_SERVICE);

                        // Lấy đối tượng Display
                        Display display = windowManager.getDefaultDisplay();

                        Point point = new Point();

                        display.getSize(point);

                        int width = point.x;

                        Log.d("width", String.valueOf(width));

                        int desiredHeightInDp = listMV.size() * (width / 4 - 0);
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp,
                                MVBaiHatActivity.tenBH.getContext().getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recycleView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recycleView.setLayoutParams(layoutParams);
                        recycleView.setNestedScrollingEnabled(false);


                    } else {
                        Log.e("MVBaiHatActivity", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {
                Log.e("MVBaiHatActivity", "Loi get goi y mv");
            }
        });

    }

    public static void getVideoBaiHat(String idBaiHat) {

        ApiServiceV1.apiServiceV1.getBaiHatById(idBaiHat).enqueue(new Callback<GetBaiHatById>() {
            @Override
            public void onResponse(Call<GetBaiHatById> call, Response<GetBaiHatById> response) {
                GetBaiHatById res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        String linkVideo = res.getData().getLinkMV();
                        String videoId = linkVideo.split("v=")[1].split("&")[0];


                        if (!videoId.isEmpty()) {
                            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                                // do stuff with it
                                youTubePlayer.loadVideo(videoId, 0);

                            });

                            tenBH.setText(res.getData().getTenBaiHat());
                            Glide.with(MVBaiHatActivity.tenBH.getContext())
                                    .load(res.getData().getAnhBia())
                                    .into(imgBH);

                            String strTenCS = "";
                            for (int i = 0; i < res.getData().getBaiHat_caSis().size(); i++) {
                                if (i == 0) {
                                    strTenCS = res.getData().getBaiHat_caSis().get(i).getCasi().getTenCaSi();
                                } else {
                                    strTenCS += ", " +
                                            res.getData().getBaiHat_caSis().get(i).getCasi().getTenCaSi();
                                }
                            }

                            tenCS.setText(strTenCS);
                            currentBaiHat = res.getData();


                        }

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("MVBaiHatActivity", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetBaiHatById> call, Throwable t) {
                Log.e("MVBaiHatActivity", "Loi get bai hat by id");
            }
        });

    }

    private void anhXa() {
        youTubePlayerView = findViewById(R.id.youtube_player_view);
        recycleView = findViewById(R.id.recycleView);
        imgBH = findViewById(R.id.imgBH);
        tenCS = findViewById(R.id.tenCS);
        tenBH = findViewById(R.id.tenBH);
        laCaSi = findViewById(R.id.laCaSi);
        scrollView = findViewById(R.id.scrollView);
        autoNext = findViewById(R.id.autoNext);
        getLifecycle().addObserver(youTubePlayerView);
        manager = new LinearLayoutManager(MVBaiHatActivity.this);


    }

    @Override
    protected void onPause() {
        super.onPause();
        isMVBaiHatActivity = false;
        youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
            // do stuff with it
            youTubePlayer.pause();

        });
    }

    @Override
    protected void onResume() {
        super.onResume();
        if (isMVBaiHatActivity == false) {
            isMVBaiHatActivity = true;
            youTubePlayerView.getYouTubePlayerWhenReady(youTubePlayer -> {
                // do stuff with it
                youTubePlayer.play();

            });
            mdXemNS.dismiss();
        }

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        ChiTietCaSiFragment.typeBack = 0;
    }

//    @Override
//    public void onBackPressed() {
//        finish();
//
//        Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                Common.replace_fragment(new MvBaiHatFragment());
//            }
//        }, 500);
//    }
}