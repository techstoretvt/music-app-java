package com.example.musicapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.app.SearchManager;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.core.app.ActivityCompat;
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
import android.widget.LinearLayout;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
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

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link KhamPhaFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class KhamPhaFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    BaiHatAdapter adapter;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems, offSetScroll;
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

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        progressBar = (ProgressBar) view.findViewById(R.id.progress);
        manager = new LinearLayoutManager(getActivity());
        topAppBar = view.findViewById(R.id.topAppBar);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c49515c"), Color.BLACK}
        );
        recyclerView.setBackground(gradientDrawable);


        if (list == null) {
            list = new ArrayList<BaiHat>();
            getListBaiHat();
        } else {
            adapter = new BaiHatAdapter(list, getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);

            offSetScroll = 3;

            recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                @Override
                public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                    super.onScrollStateChanged(recyclerView, newState);
                    if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                        isScrolling = true;
                    }
                }

                @Override
                public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                    super.onScrolled(recyclerView, dx, dy);
                    currentItems = manager.getChildCount();
                    totalItems = manager.getItemCount();
                    scrollOutItems = manager.findFirstCompletelyVisibleItemPosition();

                    if (isScrolling && (currentItems + scrollOutItems + offSetScroll == totalItems)) {
                        isScrolling = false;
                        fetchData();
                    }
                }


            });
        }

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


        return view;
    }

    private void getListBaiHat() {
        Map<String, String> options = new HashMap<>();
        options.put("maxCount", String.valueOf(maxCount));
        options.put("page", "1");
        options.put("order_style", "desc");

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

                        offSetScroll = 3;

                        recyclerView.addOnScrollListener(new RecyclerView.OnScrollListener() {
                            @Override
                            public void onScrollStateChanged(@NonNull RecyclerView recyclerView, int newState) {
                                super.onScrollStateChanged(recyclerView, newState);
                                if (newState == AbsListView.OnScrollListener.SCROLL_STATE_TOUCH_SCROLL) {
                                    isScrolling = true;
                                }
                            }

                            @Override
                            public void onScrolled(@NonNull RecyclerView recyclerView, int dx, int dy) {
                                super.onScrolled(recyclerView, dx, dy);
                                currentItems = manager.getChildCount();
                                totalItems = manager.getItemCount();
                                scrollOutItems = manager.findFirstCompletelyVisibleItemPosition();

                                if (isScrolling && (currentItems + scrollOutItems + offSetScroll == totalItems)) {
                                    isScrolling = false;
                                    fetchData();
                                }
                            }


                        });
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
        progressBar.setVisibility(View.VISIBLE);

        page++;

        Map<String, String> options = new HashMap<>();
        options.put("maxCount", String.valueOf(maxCount));
        options.put("page", String.valueOf(page));
        options.put("order_style", "desc");

        ApiServiceV1.apiServiceV1.getListBaiHatHome(options).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> baiHats = res.getData();
                        list.addAll(baiHats);
                        offSetScroll += 3;
                        adapter.notifyDataSetChanged();
                        progressBar.setVisibility(View.GONE);
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