package com.example.musicapp.activities;


import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentStatePagerAdapter;
import androidx.fragment.app.FragmentTransaction;
import androidx.viewpager.widget.ViewPager;

import android.Manifest;
import android.animation.ObjectAnimator;
import android.app.AlertDialog;
import android.app.DownloadManager;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.net.ConnectivityManager;
import android.net.Network;
import android.net.NetworkRequest;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.os.Handler;
import android.provider.Settings;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.adapters.ViewPagerMiniNhacAdapter;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.CT_ThuVien_NoiBatFragment;
import com.example.musicapp.fragments.CaNhanFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.DKTuXaFragment;
import com.example.musicapp.fragments.DaTaiFragment;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.fragments.MvBaiHatFragment;
import com.example.musicapp.fragments.NgheSiQuanTamFragment;
import com.example.musicapp.fragments.NoiBatFragment;
import com.example.musicapp.fragments.ThongBaoFragment;
import com.example.musicapp.fragments.ThuVienFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.CurrentMiniNhacFragment;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.DownloadReceiver;
import com.example.musicapp.utils.MediaCustom;
import com.example.musicapp.utils.MyWebSocketClient;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.badge.BadgeDrawable;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import io.socket.emitter.Emitter;


public class MainActivity extends AppCompatActivity {

    private static final int REQUEST_WRITE_SETTINGS_PERMISSION = 3;
    ActivityMainBinding binding;

    public static LinearLayout layoutTencasi;

    public static LinearLayout layoutAudio = null;
    public static ImageView imgNhac, btnPrev, dungNhac = null;
    ImageView btnNext;
    public static TextView txtTenBaiHat, txtTenCaSi;

    Boolean isNetwork = true, isNextBaiHat = false;


    public static String accessToken;
    String idUser;

    public static FragmentManager fragmentManager2;

    private long lastBackPressedTime;

    public static FragmentManager supportFragmentManager;

    public static BottomNavigationView bottomNavigationView;

    public static MyWebSocketClient webSocketClient;

    public static Boolean isActive = false;

    ViewPager mViewPager;

    DownloadReceiver downloadReceiver;

    public static ConstraintLayout constraintLayout;
    public static LinearProgressIndicator progess_phatNhac = null;

    public static Fragment noiBat;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());

        anhXaView();


        Intent intent = getIntent();
        String isNetworkStr = intent.getStringExtra("isNetwork");
        if (isNetworkStr.equals("true")) {
            replace_fragment(new KhamPhaFragment());
//            replace_fragment(new DKTuXaFragment());
        } else {
            replace_fragment(new DaTaiFragment());
        }


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
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#BDB3FF"), Color.parseColor("#BDB3FF")}
                );
                bottomNavigationView.setBackground(gradientDrawable);
            } else if (item.getItemId() == R.id.thuVien) {
                replace_fragment(new ThuVienFragment());
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#BDB3FF"), Color.parseColor("#BDB3FF")}
                );
                bottomNavigationView.setBackground(gradientDrawable);
            } else if (item.getItemId() == R.id.thongBao) {
                replace_fragment(new ThongBaoFragment());
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#BDB3FF"), Color.parseColor("#BDB3FF")}
                );
                bottomNavigationView.setBackground(gradientDrawable);
            } else if (item.getItemId() == R.id.caNhan) {
                replace_fragment(new CaNhanFragment());
                GradientDrawable gradientDrawable = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#BDB3FF"), Color.parseColor("#BDB3FF")}
                );
                bottomNavigationView.setBackground(gradientDrawable);
            } else if (item.getItemId() == R.id.noiBat) {
                replace_fragment(noiBat);
                GradientDrawable gradientDrawable2 = new GradientDrawable(
                        GradientDrawable.Orientation.TOP_BOTTOM,
                        new int[]{Color.parseColor("#4c4951d6"), Color.BLACK}
                );
                bottomNavigationView.setBackground(gradientDrawable2);
            }

            return true;
        });

        setEvent();


        //set su kien mat mang
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);

        ConnectivityManager.NetworkCallback networkCallback = new ConnectivityManager.NetworkCallback() {
            @Override
            public void onLost(Network network) {
                isNetwork = false;

                if (isActive) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
                    builder.setTitle("Không có kết nối");
                    builder.setMessage("Bạn có có muốn chuyển sang nghe nhạc offline không?");
                    builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            // Xử lý khi người dùng nhấn vào nút OK
                            Common.replace_fragment(new DaTaiFragment());
//                            bottomNavigationView.setSelectedItemId(R.id.thuVien);

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


    }

    private void setEvent() {
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

        SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);

        idUser = sharedPreferences.getString("idUser", null);

        webSocketClient.socket.on("khoa_tai_khoan_" + idUser, new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                Log.e("vao", "");

                SharedPreferences sharedPreferences = getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sharedPreferences.edit();

                editor.remove("accessToken");
                editor.remove("refreshToken");
                editor.apply();


                GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(MainActivity.this,
                        GoogleSignInOptions.DEFAULT_SIGN_IN);
                googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                    @Override
                    public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(SettingActivity.this,
//                                        "Dang xuat", Toast.LENGTH_SHORT)
//                                .show();

                        finish();
                    }
                });

                Intent intent = new Intent(MainActivity.this, LoginActivity.class);
                startActivity(intent);
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
                ChiTietThuVienFragment.isChiTietDS || DaTaiFragment.isFragmentDaTai) {
            replace_fragment(new ThuVienFragment());
            return;
        } else if (CT_ThuVien_NoiBatFragment.isChiTietDSNoiBat) {
            replace_fragment(MainActivity.noiBat);
            return;
        }


        if (System.currentTimeMillis() - lastBackPressedTime < 2000) {
            finish();
        } else {
            Toast.makeText(this, "Nhấn back lần nữa để thoát ứng dụng",
                            Toast.LENGTH_SHORT)
                    .show();
        }
        lastBackPressedTime = System.currentTimeMillis();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        dungNhac = null;
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
        progess_phatNhac = findViewById(R.id.progess_phatNhac);
        mViewPager = findViewById(R.id.view_pager);
        constraintLayout = findViewById(R.id.layout);
        noiBat = new NoiBatFragment();

        ViewPagerMiniNhacAdapter viewPagerMiniNhacAdapter = new ViewPagerMiniNhacAdapter(getSupportFragmentManager(),
                FragmentStatePagerAdapter.BEHAVIOR_RESUME_ONLY_CURRENT_FRAGMENT);
        mViewPager.setAdapter(viewPagerMiniNhacAdapter);

        mViewPager.addOnPageChangeListener(new ViewPager.OnPageChangeListener() {
            @Override
            public void onPageScrolled(int position, float positionOffset, int positionOffsetPixels) {

            }

            @Override
            public void onPageSelected(int position) {
                if (!isNextBaiHat) {
                    isNextBaiHat = true;
                    Log.e("vao", "");

                    mViewPager.setCurrentItem(0);
                    Boolean status = MediaCustom.next();

                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            if (status) {
                                isNextBaiHat = false;
                            } else {
                                isNextBaiHat = false;
                            }
                        }
                    }, 1000);
                }

            }

            @Override
            public void onPageScrollStateChanged(int state) {

            }
        });

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (MediaCustom.isPlay) {
                        runOnUiThread(new Runnable() {
                            @Override
                            public void run() {
                                if (MediaCustom.isPlay) {
//                                    MediaCustom.getFloatCurrentTime()
                                    float time1 = MediaCustom.getFloatCurrentTime();
                                    int time2 = MediaCustom.totalTime;
                                    int persent = (int) time1 * 100 / time2;

                                    if (progess_phatNhac != null)
                                        progess_phatNhac.setProgress(persent);

                                }
                            }
                        });
                    }


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

    private void replace_fragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager2 = fragmentManager;
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


    //phat nhac mini
    public static void phatNhacMini(String urlAnh, String tenbh, String tencasi) {
        if (CurrentMiniNhacFragment.tenBaiHat == null) return;
        String tenbhCu = (String) CurrentMiniNhacFragment.tenBaiHat.getText();
        if (tenbhCu.equals(tenbh)) {
            return;
        }
        layoutAudio.setVisibility(View.VISIBLE);


        CurrentMiniNhacFragment.tenBaiHat.setText(tenbh);
        CurrentMiniNhacFragment.tenCaSi.setText(tencasi);
        Glide.with(MainActivity.layoutAudio.getContext()).load(urlAnh).into(CurrentMiniNhacFragment.imgNhac);

        ObjectAnimator animator = ObjectAnimator.ofFloat(CurrentMiniNhacFragment.imgNhac,
                "rotation", 0, 360);
        animator.setDuration(3000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();

    }

    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 1) {
            if (grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                if (!DownloadReceiver.isDownload) {
                    //xin quyen cai dat
                    if (DownloadReceiver.isNhacChuong) {
                        boolean permission;
                        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                            permission = Settings.System.canWrite(this);
                        } else {
                            permission = ContextCompat.checkSelfPermission(this,
                                    Manifest.permission.WRITE_SETTINGS) == PackageManager.PERMISSION_GRANTED;
                        }
                        if (permission) {
                            //do your code
                        } else {
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                Intent intent = new Intent(Settings.ACTION_MANAGE_WRITE_SETTINGS);
                                intent.setData(Uri.parse("package:" + this.getPackageName()));
                                this.startActivityForResult(intent, REQUEST_WRITE_SETTINGS_PERMISSION);
                                return;
                            } else {
                                ActivityCompat.requestPermissions(this,
                                        new String[]{Manifest.permission.WRITE_SETTINGS},
                                        REQUEST_WRITE_SETTINGS_PERMISSION);
                                return;
                            }
                        }
                    }


                    Log.e("Nhac chuong", "Code chay mat dinh");
                    BsBaiHat.iconDownLoad1.setVisibility(View.GONE);
                    BsBaiHat.iconDownload2.setVisibility(View.VISIBLE);

                    DownloadReceiver.isDownload = true;

                    String linkBaiHat = BaiHatAdapter.linkBaiHat;

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
                    Toast.makeText(MainActivity.this,
                            "Đang tải file khác", Toast.LENGTH_SHORT).show();
                }


            } else {
                // Người dùng từ chối quyền
                // Cung cấp cho người dùng một giải pháp thay thế

            }
        } else if (requestCode == REQUEST_WRITE_SETTINGS_PERMISSION) {
            Log.e("Nhac chuong", "Code chay trong permission");
            BsBaiHat.iconDownLoad1.setVisibility(View.GONE);
            BsBaiHat.iconDownload2.setVisibility(View.VISIBLE);

            DownloadReceiver.isDownload = true;

            String linkBaiHat = BaiHatAdapter.linkBaiHat;

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
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_WRITE_SETTINGS_PERMISSION) {
            Log.e("Nhac chuong", "Code chay trong onActivityResult");
            BsBaiHat.iconDownLoad1.setVisibility(View.GONE);
            BsBaiHat.iconDownload2.setVisibility(View.VISIBLE);

            DownloadReceiver.isDownload = true;

            String linkBaiHat = BaiHatAdapter.linkBaiHat;

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
        }
    }
}


