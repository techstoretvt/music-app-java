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
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
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

    public static RecyclerView recyclerView;
    public static LinearLayoutManager manager;
    public static BaiHatAdapter adapter;

    ArrayList<BaiHat> danhSachBaiHat = null;

    public static ImageView anhBaiHat;
    public static TextView tenBaiHat = null, tenCaSi, theLoai, phatHanh, cungCap;


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

    public static void getData() {
        //gan gia tri
        tenBaiHat.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getTenBaiHat());
        tenCaSi.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).getCasi().getTenCaSi());

        if (ChiTietNhacActivity.isChiTietNhac)
            Glide.with(anhBaiHat.getContext()).load(MediaCustom.danhSachPhats.get(MediaCustom.position).getAnhBia())
                    .into(anhBaiHat);

//        list bai hat tiep theo
        ArrayList<BaiHat> listBH = new ArrayList<>();
        if (MediaCustom.typeDanhSachPhat == 1 || (MediaCustom.typeDanhSachPhat == 2 && !MediaCustom.isRandom)) {
            for (int i = 0; i < MediaCustom.danhSachPhats.size(); i++) {
                if (i > MediaCustom.position) {
                    listBH.add(MediaCustom.danhSachPhats.get(i));
                }
            }
        } else if (MediaCustom.typeDanhSachPhat == 2 && MediaCustom.isRandom) {
            listBH = MediaCustom.danhSachPhats;
        }


        adapter = new BaiHatAdapter(listBH, tenBaiHat.getContext());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        int desiredHeightInDp = listBH.size() * 84;
        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, tenBaiHat.getResources().getDisplayMetrics()
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
        anhBaiHat = view.findViewById(R.id.anhBaiHat);
        tenBaiHat = view.findViewById(R.id.tenBaiHat);
        tenCaSi = view.findViewById(R.id.tenCaSi);
        theLoai = view.findViewById(R.id.theLoai);
        phatHanh = view.findViewById(R.id.phatHanh);
        cungCap = view.findViewById(R.id.cungCap);
    }

    @Override
    public void onPause() {
        super.onPause();
        tenBaiHat = null;
    }
}