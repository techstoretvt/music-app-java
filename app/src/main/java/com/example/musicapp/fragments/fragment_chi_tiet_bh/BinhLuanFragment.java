package com.example.musicapp.fragments.fragment_chi_tiet_bh;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BinhLuanAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.AddCommentParent;
import com.example.musicapp.modal.anhxajson.CommentBaiHat;
import com.example.musicapp.modal.anhxajson.GetListCommentById;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.modal.body.BodyAddCommentParent;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.gms.common.api.Api;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class BinhLuanFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    BinhLuanAdapter adapter;

    ArrayList<CommentBaiHat> listData;

    public static EditText valueCmt;
    ImageView btnSend, btnCloseReply;

    Boolean isAddCmt = false;

    public static LinearLayout layoutReply;
    public static TextView nameReply;

    public static Boolean isReply = false;
    public static String idCommentCha = null;

    public BinhLuanFragment() {
        // Required empty public constructor
    }

    public static BinhLuanFragment newInstance(String param1, String param2) {
        BinhLuanFragment fragment = new BinhLuanFragment();
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
        View view = inflater.inflate(R.layout.fragment_binh_luan, container, false);

        anhXa(view);

        //set adapter
        getListComment(MediaCustom.danhSachPhats.get(MediaCustom.position).getId());

        //set Event
        setEvent();


        return view;
    }

    private void setEvent() {
        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAddCmt) return;
                isAddCmt = true;
                valueCmt.clearFocus();
                String header = Common.getHeader();
                String idBaiHat = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();
                String noiDung = valueCmt.getText().toString();
                if (noiDung.isEmpty()) return;

                BodyAddCommentParent body = new BodyAddCommentParent(idBaiHat, noiDung);

                ApiServiceV1.apiServiceV1.addCommentParent(body, header).enqueue(new Callback<AddCommentParent>() {
                    @Override
                    public void onResponse(Call<AddCommentParent> call, Response<AddCommentParent> response) {
                        AddCommentParent res = response.body();
                        if (res != null) {
                            if (res.getErrCode() == 0) {
                                CommentBaiHat newCmt = res.getData();
                                listData.add(0, newCmt);
                                adapter.notifyDataSetChanged();

                                valueCmt.setText("");
                                isAddCmt = false;

                            } else {
                                if (res.getStatus() == 401) {
                                    System.exit(0);
                                }
                                Log.e("Loi", res.getErrMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<AddCommentParent> call, Throwable t) {
                        Log.e("Loi", "Loi add comment parent");
                    }
                });
            }
        });

        btnCloseReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                isReply = false;
                layoutReply.setVisibility(View.GONE);
                idCommentCha = null;
            }
        });
    }

    private void getListComment(String idBaiHat) {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.getListCommentByIdBaiHat(idBaiHat, header).enqueue(new Callback<GetListCommentById>() {
            @Override
            public void onResponse(Call<GetListCommentById> call, Response<GetListCommentById> response) {
                GetListCommentById res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        listData = res.getData();

                        adapter = new BinhLuanAdapter(listData, getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("Loi", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListCommentById> call, Throwable t) {
                Log.e("Loi", "Loi get list comment");
            }
        });
    }

    private void anhXa(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
        valueCmt = view.findViewById(R.id.valueCmt);
        btnSend = view.findViewById(R.id.btnSend);
        layoutReply = view.findViewById(R.id.layoutReply);
        nameReply = view.findViewById(R.id.nameReply);
        btnCloseReply = view.findViewById(R.id.btnCloseReply);
    }
}