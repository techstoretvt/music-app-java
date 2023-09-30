package com.example.musicapp.fragments.fragment_chi_tiet_bh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.utils.MediaCustom;

import java.lang.reflect.Array;
import java.util.ArrayList;

public class ThongTinBaiHatFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    BaiHatAdapter adapter;

    ArrayList<BaiHat> danhSachBaiHat = null;


    public ThongTinBaiHatFragment() {
        // Required empty public constructor
    }

    public static ThongTinBaiHatFragment newInstance(String param1, String param2) {
        ThongTinBaiHatFragment fragment = new ThongTinBaiHatFragment();
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
        View view = inflater.inflate(R.layout.fragment_thong_tin_bai_hat, container, false);
        anhXa(view);

        getData();

        return view;
    }

    private void getData() {
        adapter = new BaiHatAdapter(MediaCustom.danhSachPhats, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        int desiredHeightInDp = MediaCustom.danhSachPhats.size() * 84;
        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
        );
        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
        layoutParams.height = desiredHeightInPixels;
        recyclerView.setLayoutParams(layoutParams);
        recyclerView.setNestedScrollingEnabled(false);
        recyclerView.setNestedScrollingEnabled(false);
    }

    private void anhXa(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
    }
}