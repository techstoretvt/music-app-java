package com.example.musicapp.activities;


import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.fragments.CaNhanFragment;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.fragments.NgheSiQuanTamFragment;
import com.example.musicapp.fragments.ThongBaoFragment;
import com.example.musicapp.fragments.ThuVienFragment;
import com.example.musicapp.fragments.TimKiemFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.QuanTamCS;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyXoaDSPhat;
import com.example.musicapp.utils.DownloadReceiver;
import com.example.musicapp.utils.MediaCustom;
import com.example.musicapp.utils.MyWebSocketClient;
import com.google.android.material.appbar.MaterialToolbar;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.snackbar.Snackbar;

import java.util.ArrayList;

import javax.crypto.SecretKey;

import io.jsonwebtoken.Jwts;
import io.socket.emitter.Emitter;
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

    Boolean isNetwork = true;


    public static String accessToken;
    String idUser;

    public static FragmentManager fragmentManager2;

    private long lastBackPressedTime;

//    public static Boolean isChiTietThuVien = false;


    public static FragmentManager supportFragmentManager;

    public static BottomNavigationView bottomNavigationView;

    public static MyWebSocketClient webSocketClient;

    Boolean isActive = false;

    DownloadReceiver downloadReceiver;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replace_fragment(new KhamPhaFragment());

        anhXaView();
        webSocketClient = new MyWebSocketClient();
        isActive = true;

        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);

        accessToken = sharedPreferences.getString("accessToken", null);
        idUser = sharedPreferences.getString("idUser", null);
        initEventSocket();

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
                if (MediaCustom.isPlay) {
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
                Boolean statusPhatNhac = MediaCustom.next();
            }
        });

        btnPrev.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Boolean statusPhatNhac = MediaCustom.prev();
            }
        });

//        //mo chi tiet nhac
        layoutTencasi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MediaCustom.danhSachPhats != null) {
//                    String tenBh = currentBaiHat.getTenBaiHat();
//                    String tenCs = currentBaiHat.getCasi().getTenCaSi();
//                    String loiBh = currentBaiHat.getLoiBaiHat();
//                    String anhBia = currentBaiHat.getAnhBia();
//                    String idBH = currentBaiHat.getId();

                    Intent intent = new Intent(MainActivity.this, ChiTietNhacActivity.class);
//                    intent.putExtra("tenBH", tenBh);
//                    intent.putExtra("tenCS", tenCs);
//                    intent.putExtra("loiBH", loiBh);
//                    intent.putExtra("anhBia", anhBia);
//                    intent.putExtra("idBH", idBH);


//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);


                }

            }
        });


        //set su kien mat mang
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                isNetwork = false;

//                Toast.makeText(MainActivity.this, "Mat mang", Toast.LENGTH_SHORT)
//                        .show();

                if (isActive) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Không có kết nối");
                    builder.setMessage("Bạn có có muốn chuyển sang nghe nhạc offline không?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn vào nút OK


                        }
                    });
                    builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn vào nút Hủy
                        }
                    });
                    builder.show();
                }


            }

            @Override
            public void onAvailable(@NonNull Network network) {
                super.onAvailable(network);

                if (isNetwork == false) {
                    Toast.makeText(MainActivity.this, "Đã kết nối lại", Toast.LENGTH_SHORT)
                            .show();
                }


                isNetwork = true;
            }
        };

        connectivityManager.registerNetworkCallback(new NetworkRequest.Builder().build(), networkCallback);


        //sqlite


    }

    private void initEventSocket() {
        webSocketClient.socket.on("new_thong_bao", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                BadgeDrawable badge = bottomNavigationView.getOrCreateBadge(R.id.thongBao);
                badge.setVisible(true);
                ThongBaoFragment.numberThongBao++;
                badge.setNumber(ThongBaoFragment.numberThongBao);

                //thong bao cho nguoi dung

            }
        });

        webSocketClient.socket.on("khoa_tai_khoan_" + idUser, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.e("vao", "");

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {

        if (ChiTietThuVienFragment.isChiTietDS) {
            replace_fragment(new ThuVienFragment());
            ChiTietThuVienFragment.isChiTietDS = false;
            return;
        } else if (YeuThichFragment.isYeuThich || NgheSiQuanTamFragment.isQuanTamNgheSi ||
                ChiTietThuVienFragment.isChiTietDS) {
            replace_fragment(new ThuVienFragment());
            return;
        }


        if (System.currentTimeMillis() - lastBackPressedTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát ứng dụng", Toast.LENGTH_SHORT).show();
        }
        lastBackPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onPause() {
        super.onPause();
        isActive = false;
    }

    @Override
    protected void onResume() {
        super.onResume();
        isActive = true;
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
        bottomNavigationView = findViewById(R.id.bottomNavigationView);


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

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                String linkBaiHat = BaiHatAdapter.linkBaiHat;
                String linkAnh = BaiHatAdapter.linkAnh;

                DownloadManager downloadManager = (DownloadManager) getSystemService(Context.DOWNLOAD_SERVICE);
                Uri uri = Uri.parse(linkBaiHat);
                DownloadManager.Request request = new DownloadManager.Request(uri);
                request.setTitle("Tải nhạc");
                request.setDescription("Tải nhạc từ link");
                request.setDestinationInExternalPublicDir(Environment.DIRECTORY_MUSIC,
                        BaiHatAdapter.idBaiHat + ".mp3");
                downloadManager.enqueue(request);

                downloadReceiver = new DownloadReceiver();
                IntentFilter filter = new IntentFilter(DownloadManager.ACTION_DOWNLOAD_COMPLETE);
                registerReceiver(downloadReceiver, filter);


            } else {
                // Người dùng từ chối quyền
                // Cung cấp cho người dùng một giải pháp thay thế

            }
        }
    }
}


