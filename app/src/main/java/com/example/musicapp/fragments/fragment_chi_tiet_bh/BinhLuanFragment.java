package com.example.musicapp.fragments.fragment_chi_tiet_bh;

import android.content.Context;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BinhLuanAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.AddCommentCon;
import com.example.musicapp.modal.anhxajson.AddCommentParent;
import com.example.musicapp.modal.anhxajson.CommentBaiHat;
import com.example.musicapp.modal.anhxajson.CommentBaiHatCon;
import com.example.musicapp.modal.anhxajson.GetListCommentById;
import com.example.musicapp.modal.anhxajson.GetListIdLikeComment;
import com.example.musicapp.modal.anhxajson.GetListIdLikeComment_item;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.modal.body.BodyAddCommentCon;
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

    public static RecyclerView recyclerView;
    public static LinearLayoutManager manager;
    public static BinhLuanAdapter adapter;

    public static ArrayList<CommentBaiHat> listData;
    public static ArrayList<String> listIdLike = null;

    public static EditText valueCmt;
    ImageView btnSend, btnCloseReply;

    Boolean isAddCmt = false;

    public static LinearLayout layoutReply;
    public static TextView nameReply;

    public static Boolean isReply = false, isOpenMoreCmt = false;
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

                if (!isReply) {
                    addCommentCha();
                } else {
                    addCommentCon();
                }
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

    private void addCommentCon() {
        isAddCmt = true;
        String noiDung = valueCmt.getText().toString();
        BodyAddCommentCon body = new BodyAddCommentCon(idCommentCha, noiDung, nameReply.getText().toString());
        String header = Common.getHeader();

        if (noiDung.isEmpty()) return;
        ApiServiceV1.apiServiceV1.addCommentChild(body, header).enqueue(new Callback<AddCommentCon>() {
            @Override
            public void onResponse(Call<AddCommentCon> call, Response<AddCommentCon> response) {
                AddCommentCon res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        CommentBaiHatCon newCmt = res.getData();

                        CommentBaiHat cmtCha = null;
                        for (CommentBaiHat i : listData) {
                            if (i.getId().equals(idCommentCha))
                                cmtCha = i;
                        }
                        int indexCmtCha = listData.indexOf(cmtCha);
                        listData.get(indexCmtCha).getCommentBHCons().add(0, newCmt);
                        adapter.notifyItemChanged(indexCmtCha);

                        valueCmt.clearFocus();
                        valueCmt.setText("");
                        InputMethodManager inputMethodManager = (InputMethodManager) getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
                        inputMethodManager.hideSoftInputFromWindow(valueCmt.getWindowToken(), 0);
                        isReply = false;
                        layoutReply.setVisibility(View.GONE);
                        idCommentCha = null;

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
            public void onFailure(Call<AddCommentCon> call, Throwable t) {
                Log.e("Loi", "Loi add comment con");
            }
        });

    }

    private void addCommentCha() {
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

    public static void getListComment(String idBaiHat) {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.getListIdLikeComment(idBaiHat, header).enqueue(new Callback<GetListIdLikeComment>() {
            @Override
            public void onResponse(Call<GetListIdLikeComment> call, Response<GetListIdLikeComment> response) {
                GetListIdLikeComment res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<String> newListId = new ArrayList<>();

                        for (GetListIdLikeComment_item i : res.getData()) {
                            newListId.add(i.getIdComment());
                        }
                        listIdLike = newListId;

                        ApiServiceV1.apiServiceV1.getListCommentByIdBaiHat(idBaiHat, header).enqueue(new Callback<GetListCommentById>() {
                            @Override
                            public void onResponse(Call<GetListCommentById> call, Response<GetListCommentById> response) {
                                GetListCommentById res = response.body();
                                if (res != null) {
                                    if (res.getErrCode() == 0) {
                                        listData = res.getData();

                                        adapter = new BinhLuanAdapter(listData, recyclerView.getContext());
                                        recyclerView.setAdapter(adapter);
                                        recyclerView.setLayoutManager(manager);

                                        isOpenMoreCmt = false;
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

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListIdLikeComment> call, Throwable t) {

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

    @Override
    public void onPause() {
        super.onPause();
        isOpenMoreCmt = false;
        listIdLike = null;
    }
}