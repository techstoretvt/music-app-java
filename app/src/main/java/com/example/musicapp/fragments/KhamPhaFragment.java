package com.example.musicapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Map;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class KhamPhaFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    BaiHatAdapter adapter;

    Boolean isScrolling = false;
    public static Boolean isEnd = false;
    int currentItems, totalItems, scrollOutItems, offSetScroll = 0;
    public static ArrayList<BaiHat> list = null;
    ProgressBar progressBar;

    MaterialToolbar topAppBar;

    int page = 1;
    int maxCount = 15;

    public KhamPhaFragment() {
        // Required empty public constructor
    }


    public static KhamPhaFragment newInstance(String param1, String param2) {
        KhamPhaFragment fragment = new KhamPhaFragment();
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
        View view = inflater.inflate(R.layout.fragment_kham_pha, container, false);

        anhXa(view);

        //gradient
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c49515c"), Color.BLACK}
        );
        recyclerView.setBackground(gradientDrawable);
        View bgTitle = view.findViewById(R.id.bgTitle);
        GradientDrawable gradientDrawable2 = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c4951d6"), Color.BLACK}
        );
        bgTitle.setBackground(gradientDrawable2);

        setUi();

        setEvent();


        return view;
    }

    private void setEvent() {
        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e(String.valueOf(item), "");
                if (String.valueOf(item).equals("search")) {
                    TimKiemFragment.typeBack = 1;
                    Common.replace_fragment(new TimKiemFragment());
                } else if (String.valueOf(item).equals("micro")) {
                    Log.e("vao", "");
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                    startActivityForResult(intent, 1);

                }
                return false;
            }
        });
    }

    private void setUi() {
        if (list == null) {
            list = new ArrayList<BaiHat>();
            getListBaiHat();
        } else {
            page = (int) list.size() / maxCount;
            adapter = new BaiHatAdapter(list, getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);

//            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
//                @Override
//                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
//                    super.onScrollStateChanged(recyclerView, newState);
//                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
//                        isScrolling = true;
//                    }
//                }
//
//                @Override
//                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
//                    super.onScrolled(recyclerView, dx, dy);
//                    if (isEnd) return;
//                    currentItems = manager.getChildCount();
//                    totalItems = manager.getItemCount();
//                    scrollOutItems = manager.findFirstCompletelyVisibleItemPosition();
//
//                    if (isScrolling && (currentItems + scrollOutItems + offSetScroll == totalItems)) {
//                        isScrolling = false;
//                        fetchData();
//                    }
//                }
//
//
//            });
        }
    }

    private void anhXa(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getActivity());
        topAppBar = view.findViewById(R.id.topAppBar);
    }

    private void getListBaiHat() {
        Map<String, String> options = new HashMap<>();
        options.put("maxCount", String.valueOf(50));
        options.put("page", "1");
        options.put("order_style", "desc");
        options.put("orderBy", "luotNghe");

        ApiServiceV1.apiServiceV1.getListBaiHatHome(options).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> baiHats = res.getData();
                        list = baiHats;
                        adapter = new BaiHatAdapter(list, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);


                    } else {
                        Toast.makeText(getActivity(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {
                Toast.makeText(getActivity(), "Error call api get list bai hat", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void fetchData() {
        Map<String, String> options = new HashMap<>();
        options.put("maxCount", String.valueOf(100));
        options.put("page", String.valueOf(1));
        options.put("order_style", "desc");
        options.put("orderBy", "luotNghe");

        ApiServiceV1.apiServiceV1.getListBaiHatHome(options).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> baiHats = res.getData();
                        list = baiHats;
                        adapter.notifyDataSetChanged();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {
                Toast.makeText(getActivity(), "Error call api get list bai hat", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == 1 && resultCode == RESULT_OK) {
            // Trích xuất văn bản từ giọng nói
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);

            // Sử dụng văn bản để tìm kiếm
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, text);
//            startActivity(intent);

            TimKiemFragment.typeBack = 1;
            TimKiemFragment.strValueSearch = text;
            Common.replace_fragment(new TimKiemFragment());


        }
    }
}