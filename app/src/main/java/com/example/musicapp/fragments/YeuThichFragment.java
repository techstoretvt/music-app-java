package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.BaiHatComparator;
import com.example.musicapp.modal.anhxajson.GetListBHYeuThich;
import com.example.musicapp.modal.anhxajson.YeuThichBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;
import java.util.Collections;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class YeuThichFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static RecyclerView recyclerView;
    public static LinearLayoutManager manager = null;
    public static BaiHatAdapter adapter = null;

    public static ArrayList<BaiHat> data = null;

    ImageView btnBack, btnFilter;

    public static BsSapXepYeuThich bs = null;

    public static int Filter = 1;

    public static Boolean isYeuThich = false;

    Button btnRandom;

    public YeuThichFragment() {
        // Required empty public constructor
    }

    public static YeuThichFragment newInstance(String param1, String param2) {
        YeuThichFragment fragment = new YeuThichFragment();
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
        View view = inflater.inflate(R.layout.fragment_yeu_thich, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
        btnBack = view.findViewById(R.id.btnBack);
        btnFilter = view.findViewById(R.id.btnFilter);
        bs = new BsSapXepYeuThich();
        isYeuThich = true;
        btnRandom = view.findViewById(R.id.btnRandom);

        getListBaiHat();

        setEventClick();

        return view;
    }

    private void setEventClick() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new ThuVienFragment());
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bs.show(MainActivity.supportFragmentManager, BsSapXepYeuThich.TAG);
            }
        });

        btnRandom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaCustom.position = 0;
                MediaCustom.danhSachPhats = data;
                MediaCustom.typeDanhSachPhat = 2;
                MediaCustom.tenLoai = "Yêu thích";

                MainActivity.phatNhacMini(data.get(0).getAnhBia(),
                        data.get(0).getTenBaiHat(),
                        data.get(0).getCasi().getTenCaSi());

                MediaCustom.phatNhac(data.get(0).getLinkBaiHat());
                MediaCustom.isRandom = true;
            }
        });
    }

    public static void getListBaiHat() {
        String header = Common.getHeader();
        String orderType = "";
        String orderBy = "";

        if (Filter == 1) {
            orderBy = "createdAt";
            orderType = "desc";
        } else if (Filter == 2) {
            orderBy = "createdAt";
            orderType = "asc";
        }

        ApiServiceV1.apiServiceV1.getListBHYeuThich(orderBy, orderType, header).enqueue(new Callback<GetListBHYeuThich>() {
            @Override
            public void onResponse(Call<GetListBHYeuThich> call, Response<GetListBHYeuThich> response) {
                GetListBHYeuThich res = response.body();

                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> listBH = new ArrayList<>();

                        for (YeuThichBaiHat i : res.getData()) {
                            listBH.add(i.getBaihat());
                        }
//
                        data = listBH;

                        if (Filter == 3) {
                            Collections.sort(data, new BaiHatComparator());
                        } else if (Filter == 4) {
                            Collections.sort(data, new BaiHatComparator().reversed());
                        }

                        if (MediaCustom.tenLoai.equals("Yêu thích")) {
                            MediaCustom.danhSachPhats = data;
                        }

//
                        adapter = new BaiHatAdapter(data, recyclerView.getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(recyclerView.getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListBHYeuThich> call, Throwable t) {
                Log.e("Loi", "Loi get list bh yeu thich");
            }
        });

    }

    @Override
    public void onPause() {
        super.onPause();
        bs = null;
        data = null;
        manager = null;
        adapter = null;
        isYeuThich = false;
    }
}