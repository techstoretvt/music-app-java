package com.example.musicapp.fragments.fragment_chi_tiet_bh;

import android.animation.ObjectAnimator;
import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static ImageView btnThaTim = null;
    ImageView btnShare;
    public static ImageView imgNhac;

    public static TextView txtTenBH, txtTenCS, txtLoiBH;

    public BaiHatFragment() {
        // Required empty public constructor
    }

    public static BaiHatFragment newInstance(String param1, String param2) {
        BaiHatFragment fragment = new BaiHatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_bai_hat, container, false);

        anhXa(view);
        initUI();

        checkLike();

        setEvent();


        return view;
    }

    private void setEvent() {
        btnThaTim.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleToggleLike();
            }
        });

        btnShare.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // Tạo một intent chia sẻ
                String urlToShare = MediaCustom.danhSachPhats.get(MediaCustom.position).getLinkBaiHat();

// Tạo một Intent chia sẻ
                Intent shareIntent = new Intent(Intent.ACTION_SEND);
                shareIntent.setType("text/plain");
                shareIntent.putExtra(Intent.EXTRA_TEXT, urlToShare);

// Kiểm tra và chọn ứng dụng để chia sẻ
                startActivity(Intent.createChooser(shareIntent, "Chia sẻ URL với:"));
            }
        });
    }

    private void initUI() {
        String tenBh = MediaCustom.danhSachPhats.get(MediaCustom.position).getTenBaiHat();
        String tenCs = MediaCustom.danhSachPhats.get(MediaCustom.position).getCasi().getTenCaSi();
        String loiBh = MediaCustom.danhSachPhats.get(MediaCustom.position).getLoiBaiHat();
        String anhBia = MediaCustom.danhSachPhats.get(MediaCustom.position).getAnhBia();

        Glide.with(getActivity()).load(anhBia)
                .into(imgNhac);
        txtTenBH.setText(tenBh);
        txtTenCS.setText(tenCs);
        txtLoiBH.setText(loiBh);

        //animtion anh nhac
        ObjectAnimator animator = ObjectAnimator.ofFloat(imgNhac, "rotation", 0, 360);
        animator.setDuration(6000);
        animator.setRepeatCount(ObjectAnimator.INFINITE);
        animator.start();
    }

    private void anhXa(View view) {
        btnThaTim = view.findViewById(R.id.btnThaTim);
        imgNhac = view.findViewById(R.id.imgNhac);
        txtTenBH = view.findViewById(R.id.txtTenBH);
        txtTenCS = view.findViewById(R.id.txtTenCS);
        txtLoiBH = view.findViewById(R.id.txtLoiBH);
        btnShare = view.findViewById(R.id.btnShare);
    }

    public static void checkLike() {
        String header = Common.getHeader();
        String idBH = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();

        ApiServiceV1.apiServiceV1.kiemTraYeuThichBaiHat(idBH, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        if (res.getErrMessage().equals("like")) {
                            btnThaTim.setImageResource(R.drawable.baseline_favorite_red);
                        } else {
                            btnThaTim.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("Loi", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi", "Loi call api toggle like bai hat");
            }
        });
    }

    public static void handleToggleLike() {

        String header = Common.getHeader();
        String idBH = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();

        ApiServiceV1.apiServiceV1.toggleLikeBaiHat(idBH, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        if (res.getErrMessage().equals("like")) {
                            btnThaTim.setImageResource(R.drawable.baseline_favorite_red);
                        } else {
                            btnThaTim.setImageResource(R.drawable.baseline_favorite_24);
                        }
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("Loi", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi", "Loi call api toggle like bai hat");
            }
        });


    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        btnShare = null;
    }
}