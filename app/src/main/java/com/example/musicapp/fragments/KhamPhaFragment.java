package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;

import java.util.ArrayList;
import java.util.HashMap;
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
    AdapterKhamPha adapter;

    Boolean isScrolling = false;
    int currentItems, totalItems, scrollOutItems, offSetScroll;
    public static ArrayList<BaiHat> list = null;
    ProgressBar progressBar;

    int page = 1;
    int maxCount = 15;

    public KhamPhaFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment KhamPhaFragment.
     */
    // TODO: Rename and change types and number of parameters
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


        if (list == null) {
            list = new ArrayList<BaiHat>();
            getListBaiHat();
        } else {
            adapter = new AdapterKhamPha(list, getActivity());
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
                        adapter = new AdapterKhamPha(list, getActivity());
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
                        Toast.makeText(getActivity(), "Success", Toast.LENGTH_SHORT)
                                .show();
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


    //        new Handler().postDelayed(new Runnable() {
//            @Override
//            public void run() {
//            }
//        }, 1000);
}