package com.example.musicapp.fragments;

import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.TimKiemGanDayAdapter;
import com.google.android.material.divider.MaterialDivider;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link TimKiemFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class TimKiemFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView rvTimKiemGanDay;
    LinearLayoutManager manager;
    TimKiemGanDayAdapter adapter;
    ArrayList<String> dataTimKiemGanDay;

    TextView txtBH, txtCS;
    MaterialDivider lineBH, lineCS;
    LinearLayout laTimBH, laTimCS;

    int loaiTimKiem = 1; //1 bai hat, 2 ca si

    public TimKiemFragment() {
        // Required empty public constructor
    }

    public static TimKiemFragment newInstance(String param1, String param2) {
        TimKiemFragment fragment = new TimKiemFragment();
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
        View view = inflater.inflate(R.layout.fragment_tim_kiem, container, false);
        rvTimKiemGanDay = view.findViewById(R.id.rvTimKiemGanDay);
        manager = new LinearLayoutManager(getContext());
        txtBH = view.findViewById(R.id.txtBH);
        txtCS = view.findViewById(R.id.txtCS);
        lineBH = view.findViewById(R.id.lineBH);
        lineCS = view.findViewById(R.id.lineCS);
        laTimBH = view.findViewById(R.id.laTimBH);
        laTimCS = view.findViewById(R.id.laTimCS);

        setEventLoaiTK();


        dataTimKiemGanDay = new ArrayList<>();
        for (int i = 0; i < 4; i++) {
            dataTimKiemGanDay.add("du lieu " + i);
        }
        adapter = new TimKiemGanDayAdapter(dataTimKiemGanDay, getContext());
        rvTimKiemGanDay.setAdapter(adapter);
        rvTimKiemGanDay.setLayoutManager(manager);


        return view;
    }

    private void setEventLoaiTK() {
        laTimBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaiTimKiem = 1;
                txtBH.setTextColor(Color.parseColor("#BDB3FF"));
                lineBH.setDividerColor(Color.parseColor("#BDB3FF"));

                txtCS.setTextColor(Color.WHITE);
                lineCS.setDividerColor(Color.BLACK);
            }
        });

        laTimCS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                loaiTimKiem = 2;
                txtCS.setTextColor(Color.parseColor("#BDB3FF"));
                lineCS.setDividerColor(Color.parseColor("#BDB3FF"));

                txtBH.setTextColor(Color.WHITE);
                lineBH.setDividerColor(Color.BLACK);
            }
        });
    }


}