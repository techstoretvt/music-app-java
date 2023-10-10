package com.example.musicapp.fragments.fragment_chi_tiet_bh;

import android.content.Context;
import android.graphics.Point;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.Display;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.example.musicapp.R;
import com.example.musicapp.adapters.LoiBaiHatAdapter;
import com.example.musicapp.adapters.TestAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.GetLoiBaiHat;
import com.example.musicapp.modal.anhxajson.LoiBaiHat;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoiBaiHatFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    LoiBaiHatAdapter adapter;

    ArrayList<LoiBaiHat> listLoiBH;

    public static LoiBaiHat currentLoiBH = null;

    public static int oldPosition = -1;

    Boolean isLoiBaiHat = false;


    public LoiBaiHatFragment() {
        // Required empty public constructor
    }

    public static LoiBaiHatFragment newInstance(String param1, String param2) {
        LoiBaiHatFragment fragment = new LoiBaiHatFragment();
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
        View view = inflater.inflate(R.layout.fragment_loi_bai_hat, container, false);
        anhXa(view);
        isLoiBaiHat = true;

        initUI();

        return view;
    }

    private void initUI() {
        getLoiBaiHat();

        new Thread(new Runnable() {
            @Override
            public void run() {
                while (true) {
                    if (isLoiBaiHat == false) {
                        break;
                    }

                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            if (MediaCustom.isPlay && listLoiBH != null && listLoiBH.size() != 0) {
                                float currentTimeBH = MediaCustom.getFloatCurrentTime();
                                if (currentTimeBH < listLoiBH.get(0).getThoiGian())
                                    return;

                                int currentPosition = -1;

                                for (int i = 0; i < listLoiBH.size() - 1; i++) {
                                    if (currentTimeBH >= listLoiBH.get(i).getThoiGian()
                                            && currentTimeBH < listLoiBH.get(i + 1).getThoiGian()) {
                                        currentPosition = i;
                                        break;
                                    }
                                }
                                if (currentPosition == -1)
                                    currentPosition = listLoiBH.size() - 1;

                                if (currentPosition != -1 && currentPosition != oldPosition) {

                                    RecyclerView.ViewHolder viewHolder = recyclerView.
                                            findViewHolderForLayoutPosition(currentPosition);
                                    if (viewHolder != null && viewHolder.isRecyclable()) {
                                        // Phần tử đó đã được cuộn đến
                                        Log.e("Test", "True");
                                        recyclerView.smoothScrollToPosition(currentPosition + 3);
                                    } else {
                                        // Phần tử đó chưa được cuộn đến
                                        Log.e("Test", "False");
                                        recyclerView.smoothScrollToPosition(currentPosition);
                                    }
                                    Log.e("LoiBH", String.valueOf(oldPosition));
                                    Log.e("LoiBH", String.valueOf(currentPosition));

                                    LoiBaiHatAdapter.currentPosition = currentPosition;
                                    if (oldPosition != -1)
                                        adapter.notifyItemChanged(oldPosition);
                                    oldPosition = currentPosition;
                                    adapter.notifyItemChanged(currentPosition);


                                }


                            }
                        }
                    });


                    // Nghỉ 100 mili giây
                    try {
                        Thread.sleep(50);
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
            }
        }).start();
    }

    private void getLoiBaiHat() {
        String idBaiHat = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();
        Log.e("idBaiHat", idBaiHat);
        if (idBaiHat.isEmpty()) {
            return;
        }

        ApiServiceV1.apiServiceV1.getLoiBaiHatById(idBaiHat).enqueue(new Callback<GetLoiBaiHat>() {
            @Override
            public void onResponse(Call<GetLoiBaiHat> call, Response<GetLoiBaiHat> response) {
                GetLoiBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        listLoiBH = res.getData();
                        if (listLoiBH.size() == 0) {
                            return;
                        }

                        adapter = new LoiBaiHatAdapter(listLoiBH, getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);


                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("LoiBaiHatFragment", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetLoiBaiHat> call, Throwable t) {
                Log.e("LoiBaiHatFragment", "Loi call api get loi bai hat");
            }
        });

    }

    private void anhXa(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isLoiBaiHat = false;
    }
}