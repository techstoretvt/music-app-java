package com.example.musicapp.fragments;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.activities.MVBaiHatActivity;
import com.example.musicapp.adapters.MVBaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.TimKiemMV;
import com.example.musicapp.utils.Common;
import com.google.android.gms.common.api.Api;
import com.google.android.material.textfield.TextInputEditText;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MvBaiHatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    MVBaiHatAdapter adapter;

    ArrayList<BaiHat> listVideo;

    TextInputEditText valueSearch;

    ImageView ic_search, btnBack;

    public static boolean isMVBaiHatFragment = false;

    public MvBaiHatFragment() {
        // Required empty public constructor
    }


    public static MvBaiHatFragment newInstance(String param1, String param2) {
        MvBaiHatFragment fragment = new MvBaiHatFragment();
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
        View view = inflater.inflate(R.layout.fragment_mv_bai_hat, container, false);
        anhXa(view);
        isMVBaiHatFragment = true;

        getGoiYVideo();

        setEvent();

        return view;
    }

    private void setEvent() {
        valueSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("vao", "onClick: ");
                timKiemMVBaiHat();
                valueSearch.clearFocus();

                InputMethodManager inputMethodManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(textView.getWindowToken(), 0);

                return true;
            }
        });

        ic_search.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                timKiemMVBaiHat();
                InputMethodManager inputMethodManager = (InputMethodManager)
                        getContext().getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new ThuVienFragment());
            }
        });
    }

    private void timKiemMVBaiHat() {
        String strValueSearch = valueSearch.getText().toString();
        if (strValueSearch.isEmpty()) {
            return;
        }

        ApiServiceV1.apiServiceV1.timKiemMV(strValueSearch).enqueue(new Callback<TimKiemMV>() {
            @Override
            public void onResponse(Call<TimKiemMV> call, Response<TimKiemMV> response) {
                TimKiemMV res = response.body();

                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> newArr = new ArrayList<>();

                        for (int i = 0; i < res.getData().size(); i++) {
                            newArr.add(res.getDataIndex(i));
                        }

                        listVideo = newArr;

                        adapter = new MVBaiHatAdapter(listVideo, getContext(), R.layout.item_search_mv_bai_hat);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = listVideo.size() * 90;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp,
                                getContext().getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recyclerView.setLayoutParams(layoutParams);
                        recyclerView.setNestedScrollingEnabled(false);


                    } else {

                    }
                }

            }

            @Override
            public void onFailure(Call<TimKiemMV> call, Throwable t) {

            }
        });

    }

    private void getGoiYVideo() {
        Gson gson = new GsonBuilder().create();
        ArrayList<String> a = new ArrayList<>();
        a.add("");
        String json = gson.toJson(a);


        ApiServiceV1.apiServiceV1.getGoiYMVBaiHat(json).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        listVideo = res.getData();

                        adapter = new MVBaiHatAdapter(listVideo, getContext(), R.layout.item_search_mv_bai_hat);
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = listVideo.size() * 90;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp,
                                getContext().getResources().getDisplayMetrics()
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

    private void anhXa(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        valueSearch = view.findViewById(R.id.valueSearch);
        ic_search = view.findViewById(R.id.ic_search);
        btnBack = view.findViewById(R.id.btnBack);
        manager = new LinearLayoutManager(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isMVBaiHatFragment = false;
    }

    @Override
    public void onPause() {
        super.onPause();
        isMVBaiHatFragment = false;
    }

    @Override
    public void onResume() {
        super.onResume();
        isMVBaiHatFragment = true;
    }
}