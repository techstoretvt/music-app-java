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

import com.example.musicapp.R;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.modal.anhxajson.BaiHat;

import java.util.ArrayList;

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
    int currentItems, totalItems, scrollOutItems;
    ArrayList list = null;
    ProgressBar progressBar;

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

        list = new ArrayList<BaiHat>();

        for (int i = 0; i < 20; i++) {
            BaiHat baiHat = new BaiHat(String.valueOf(i + 1), "Ten bai hat"
                    + String.valueOf(i + 1), "Ten ca si" + String.valueOf(i + 1));
            list.add(baiHat);
        }


        adapter = new AdapterKhamPha(list, getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

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

                if (isScrolling && (currentItems + scrollOutItems + 4 == totalItems)) {
                    isScrolling = false;
                    fetchData();
                }
            }

            private void fetchData() {
                progressBar.setVisibility(View.VISIBLE);
                new Handler().postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        for (int i = 0; i < 10; i++) {
                            BaiHat baiHat = new BaiHat(String.valueOf(i + 1), "Ten bai hat"
                                    + String.valueOf(i + 1), "Ten ca si" + String.valueOf(i + 1));
                            list.add(baiHat);

                            adapter.notifyDataSetChanged();
                            progressBar.setVisibility(View.GONE);
                        }
                    }
                }, 1000);
            }

        });
        return view;
    }


}