package com.example.musicapp.fragments;

import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapters.CaSiAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.GetListCSQuanTam;
import com.example.musicapp.modal.anhxajson.QuanTamCS;
import com.example.musicapp.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class NgheSiQuanTamFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    GridLayoutManager manager;
    CaSiAdapter adapter;

    TextView numberQuanTam;

    ImageView btnBack;

    public static Boolean isQuanTamNgheSi = false;

    public NgheSiQuanTamFragment() {
        // Required empty public constructor
    }


    public static NgheSiQuanTamFragment newInstance(String param1, String param2) {
        NgheSiQuanTamFragment fragment = new NgheSiQuanTamFragment();
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
        View view = inflater.inflate(R.layout.fragment_nghe_si_quan_tam, container, false);
        recyclerView = view.findViewById(R.id.recycleView);
        btnBack = view.findViewById(R.id.btnBack);
        numberQuanTam = view.findViewById(R.id.numberQuanTam);
        manager = new GridLayoutManager(getContext(), 2);
        isQuanTamNgheSi = true;

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#38383894"), Color.BLACK}
        );
        LinearLayout layoutQuanTamNgheSi = view.findViewById(R.id.layoutQuanTamNgheSi);
        layoutQuanTamNgheSi.setBackground(gradientDrawable);

        getListCaSi();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new ThuVienFragment());
            }
        });

        return view;
    }

    private void getListCaSi() {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.getListCaSiQuanTam(header).enqueue(new Callback<GetListCSQuanTam>() {
            @Override
            public void onResponse(Call<GetListCSQuanTam> call, Response<GetListCSQuanTam> response) {
                GetListCSQuanTam res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<Casi> dtCS = new ArrayList<>();

                        for (QuanTamCS i : res.getData()) {
                            dtCS.add(i.getCasi());
                        }

                        adapter = new CaSiAdapter(dtCS, getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        numberQuanTam.setText(String.valueOf(dtCS.size()));

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListCSQuanTam> call, Throwable t) {
                Log.e("Loi", "Loi get danh sach ca si quan tam");
            }
        });
    }

    @Override
    public void onPause() {
        super.onPause();
        isQuanTamNgheSi = false;
    }
}