package com.example.musicapp.fragments.fragment_mini_nhac;

import android.content.Intent;
import android.os.Bundle;

import androidx.fragment.app.Fragment;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;

import de.hdodenhof.circleimageview.CircleImageView;


public class CurrentMiniNhacFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    public static CircleImageView imgNhac;
    public static TextView tenBaiHat, tenCaSi;

    LinearLayout wrap_baihat;


    public CurrentMiniNhacFragment() {
        // Required empty public constructor
    }


    public static CurrentMiniNhacFragment newInstance(String param1, String param2) {
        CurrentMiniNhacFragment fragment = new CurrentMiniNhacFragment();
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
        View view = inflater.inflate(R.layout.fragment_current_mini_nhac, container, false);
        anhXa(view);

        setEvent();

        return view;
    }

    private void setEvent() {
        wrap_baihat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ChiTietNhacActivity.class);
                startActivity(intent);
            }
        });
    }

    private void anhXa(View view) {
        imgNhac = view.findViewById(R.id.imgNhac);
        tenBaiHat = view.findViewById(R.id.tenBaiHat);
        tenCaSi = view.findViewById(R.id.tenCaSi);
        wrap_baihat = view.findViewById(R.id.wrap_baihat);

    }
}