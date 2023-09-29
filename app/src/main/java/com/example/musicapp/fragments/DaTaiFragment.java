package com.example.musicapp.fragments;

import android.database.Cursor;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

public class DaTaiFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    LinearLayoutManager manager;
    public static BaiHatAdapter adapter;
    RecyclerView recyclerView;
    public static ArrayList<BaiHat> danhSachBaiHat;

    ImageView btnBack, btnFilter;
    Button btnPhatNgauNhien;


    public static int Filter = 1;

    public static BsSapXepDaTai bs = null;


    public static Boolean isFragmentDaTai = false;

    public DaTaiFragment() {
        // Required empty public constructor
    }

    public static DaTaiFragment newInstance(String param1, String param2) {
        DaTaiFragment fragment = new DaTaiFragment();
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
        View view = inflater.inflate(R.layout.fragment_da_tai, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
        isFragmentDaTai = true;
        btnBack = view.findViewById(R.id.btnBack);
        btnPhatNgauNhien = view.findViewById(R.id.btnRandom);
        btnFilter = view.findViewById(R.id.btnFilter);
        bs = new BsSapXepDaTai();

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#38383894"), Color.BLACK}
        );
        LinearLayout layoutDaTai = view.findViewById(R.id.layoutDaTai);
        layoutDaTai.setBackground(gradientDrawable);

        getData();

        setEvent();

        return view;
    }

    private void setEvent() {
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new ThuVienFragment());
            }
        });

        btnPhatNgauNhien.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (danhSachBaiHat.size() == 0) {
                    return;
                }
                MediaCustom.position = 0;
                MediaCustom.danhSachPhats = danhSachBaiHat;
                MediaCustom.typeDanhSachPhat = 2;
                MediaCustom.tenLoai = "Đã tải";

                MainActivity.phatNhacMini(danhSachBaiHat.get(0).getAnhBia(),
                        danhSachBaiHat.get(0).getTenBaiHat(),
                        danhSachBaiHat.get(0).getCasi().getTenCaSi());

                MediaCustom.phatNhac(danhSachBaiHat.get(0).getLinkBaiHat());
                MediaCustom.isRandom = true;
            }
        });

        btnFilter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                bs.show(MainActivity.supportFragmentManager, BsSapXepDaTai.TAG);
            }
        });
    }

    private void getData() {
        danhSachBaiHat = new ArrayList<>();


        MusicAppHelper musicAppHelper = new MusicAppHelper(getContext(),
                "MusicApp.sqlite", null, 1);

        Cursor dataBaiHat = musicAppHelper.GetData("SELECT * FROM BaiHat");

        while (dataBaiHat.moveToNext()) {
            String tenBaiHat = dataBaiHat.getString(1);
            String idBaiHat = dataBaiHat.getString(0);
            String tenCS = dataBaiHat.getString(2);
            String linkBH = dataBaiHat.getString(3);
            String linkAnh = dataBaiHat.getString(4);
            String loiBH = dataBaiHat.getString(5);

            Casi cs = new Casi("1", tenCS, "mota", "anh");

            BaiHat bh = new BaiHat(idBaiHat, tenBaiHat, loiBH, linkAnh, linkBH, cs);
            danhSachBaiHat.add(bh);

        }
        if (danhSachBaiHat.size() == 0) {
            btnPhatNgauNhien.setEnabled(false);
            btnFilter.setEnabled(false);
        }

        adapter = new BaiHatAdapter(danhSachBaiHat, getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


    }

    @Override
    public void onPause() {
        super.onPause();
        isFragmentDaTai = false;
        danhSachBaiHat = null;
    }
}