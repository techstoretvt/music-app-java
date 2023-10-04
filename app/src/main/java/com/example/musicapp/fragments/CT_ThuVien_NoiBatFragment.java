package com.example.musicapp.fragments;

import android.os.Bundle;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class CT_ThuVien_NoiBatFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;


    TextView chuaCoBaihat, tenDS;
    public static TextView slBaiHat;

    ImageView btnBack, img1anh, btnEdit;

    Button btnSubmitEdit;

    public static String tenDs = null, linkAnh = null;

    public static Boolean isChiTietDSNoiBat = false;
    public static BaiHatAdapter adapter;


    public CT_ThuVien_NoiBatFragment() {
        // Required empty public constructor
    }

    public static CT_ThuVien_NoiBatFragment newInstance(String param1, String param2) {
        CT_ThuVien_NoiBatFragment fragment = new CT_ThuVien_NoiBatFragment();
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
        View view = inflater.inflate(R.layout.fragment_ct_thu_vien_noi_bat, container, false);

        anhXa(view);

        layDanhSachBaiHat();

        //set event
        initEvent();

        //set ui
        if (tenDs != null) {
            tenDS.setText(tenDs);
        }
        if (linkAnh != null) {
            Glide.with(getActivity()).load(linkAnh).into(img1anh);
        }

        return view;
    }

    private void anhXa(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        manager = new GridLayoutManager(getActivity(), 1);
        chuaCoBaihat = view.findViewById(R.id.chuaCoBaihat);
        btnBack = view.findViewById(R.id.btnBack);
        tenDS = view.findViewById(R.id.tenDS);
        slBaiHat = view.findViewById(R.id.slBaiHat);
        img1anh = view.findViewById(R.id.img1anh);

        isChiTietDSNoiBat = true;
    }

    private void initEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(MainActivity.noiBat);
            }
        });

    }


//    @Override
//    public void onResume() {
//        super.onResume();
//        layDanhSachBaiHat();
//    }


    @Override
    public void onDestroy() {
        super.onDestroy();
        isChiTietDSNoiBat = false;
    }

    private void layDanhSachBaiHat() {
        String header = "bearer " + MainActivity.accessToken;

        String[] listId = new String[]{"", ""};

        ApiServiceV1.apiServiceV1.getListRandomBaiHat(20, listId, header).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> listBH = res.getData();

                        adapter = new BaiHatAdapter(listBH, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = listBH.size() * 84;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recyclerView.setLayoutParams(layoutParams);
                        recyclerView.setNestedScrollingEnabled(false);

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {

            }
        });

    }

}