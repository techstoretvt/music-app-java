package com.example.musicapp.fragments;

import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.adapters.CaSiAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DanhSachCaSi;
import com.example.musicapp.modal.anhxajson.GetCaSiByID;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ChiTietCaSiFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    RecyclerView recyclerViewCS;
    LinearLayoutManager manager;
    LinearLayoutManager managerCS;
    BaiHatAdapter adapter;
    CaSiAdapter adapterCS;
    ArrayList<BaiHat> dsBaiHats = null;

    TextView chuaCoBaihat, thongTin, tenCS, numberQuanTam;

    ImageView anhCS, btnBack;

    Button btnQuanTam, btnPhatNhac;


    public static String idCaSi = "e0e1064e-2f2f-40a9-8d0d-2cee76da27f5";
    public static Boolean isChiTietCS = false;
    public static String strTenCS = "";

    public static int typeBack = 0;

    //biến hỗ trợ cho việc goback
    public static String idDanhSachPhat;
    //end


    public ChiTietCaSiFragment() {
        // Required empty public constructor
    }


    public static ChiTietCaSiFragment newInstance(String param1, String param2) {
        ChiTietCaSiFragment fragment = new ChiTietCaSiFragment();
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
        View view = inflater.inflate(R.layout.fragment_chi_tiet_ca_si, container, false);
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        recyclerViewCS = (RecyclerView) view.findViewById(R.id.recycleViewCS);
        manager = new LinearLayoutManager(getActivity());
        managerCS = new LinearLayoutManager(getActivity(), LinearLayoutManager.HORIZONTAL, false);
        chuaCoBaihat = view.findViewById(R.id.chuaCoBaihat);
        thongTin = view.findViewById(R.id.thongTin);
        tenCS = view.findViewById(R.id.tenCS);
        anhCS = view.findViewById(R.id.anhCS);
        btnBack = view.findViewById(R.id.btnBack);
        isChiTietCS = true;
        btnQuanTam = view.findViewById(R.id.btnQuanTam);
        numberQuanTam = view.findViewById(R.id.numberQuanTam);
        btnPhatNhac = view.findViewById(R.id.btnPhatNhac);


        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c49515c"), Color.BLACK}
        );
        LinearLayout linearLayout = view.findViewById(R.id.layoutInfo);
        linearLayout.setBackground(gradientDrawable);


        layThongTinCaSi();

        layDanhSachBaiHat();

        layDanhSachCaSi();

        kiemTraQuanTam();

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                // 0 main, 1 chi tiet thu vien, 2 chi tiet nhac
                if (typeBack == 0) {
                    Common.replace_fragment(new KhamPhaFragment());
//                    Intent intent = new Intent(getContext(), MainActivity.class);
//                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
//                    startActivity(intent);
                    MainActivity.bottomNavigationView.setSelectedItemId(R.id.khamPha);
                } else if (typeBack == 1) {
                    ChiTietThuVienFragment.idDanhSachPhat = idDanhSachPhat;
                    Common.replace_fragment(new ChiTietThuVienFragment());

                } else if (typeBack == 2) {

                    Intent intent = new Intent(getContext(), ChiTietNhacActivity.class);
                    intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP | Intent.FLAG_ACTIVITY_SINGLE_TOP);
                    startActivity(intent);

                } else if (typeBack == 3) {

                    Common.replace_fragment(new NgheSiQuanTamFragment());

                } else if (typeBack == 4) {

                    Common.replace_fragment(new YeuThichFragment());

                }
            }
        });

        btnQuanTam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                handleToggleQuanTam();
            }
        });

        btnPhatNhac.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MediaCustom.position = 0;
                MediaCustom.danhSachPhats = dsBaiHats;
                MediaCustom.typeDanhSachPhat = 2;
                MediaCustom.tenLoai = tenCS.getText().toString();

                MainActivity.phatNhacMini(dsBaiHats.get(0).getAnhBia(),
                        dsBaiHats.get(0).getTenBaiHat(),
                        dsBaiHats.get(0).getCasi().getTenCaSi());

                MediaCustom.phatNhac(dsBaiHats.get(0).getLinkBaiHat());

                BaiHat bh2 = dsBaiHats.get(1);
                if (bh2 != null) {
                    NextMiniNhacFragment.tenBaiHat.setText(bh2.getTenBaiHat());
                    NextMiniNhacFragment.tenCaSi.setText(bh2.getCasi().getTenCaSi());
                    Glide.with(getContext()).load(bh2.getAnhBia()).into(NextMiniNhacFragment.imgNhac);
                }
            }
        });


        return view;
    }

    private void kiemTraQuanTam() {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.kiemTraQuanTamCaSi(idCaSi, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        btnQuanTam.setText("Đã quan tâm");
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("Loi", res.getErrMessage());
                    }
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi", "Loi kiem tra quan tam ca si");
            }
        });
    }

    private void handleToggleQuanTam() {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.toggleQuanTamCasi(idCaSi, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        if (res.getErrMessage().equals("yes")) {
                            btnQuanTam.setText("ĐÃ QUAN TÂM");
                            int number = Integer.parseInt(numberQuanTam.getText().toString());
                            number++;
                            numberQuanTam.setText(String.valueOf(number));

                        } else {
                            btnQuanTam.setText("QUAN TÂM");
                            int number = Integer.parseInt(numberQuanTam.getText().toString());
                            number--;
                            numberQuanTam.setText(String.valueOf(number));
                        }
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
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi", "Loi toggle quan tam ca si");
            }
        });


    }

    private void layThongTinCaSi() {
        String header = "bearer " + MainActivity.accessToken;

        ApiServiceV1.apiServiceV1.layCaSiById(idCaSi, header).enqueue(new Callback<GetCaSiByID>() {
            @Override
            public void onResponse(Call<GetCaSiByID> call, Response<GetCaSiByID> response) {
                GetCaSiByID res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        strTenCS = res.getData().getTenCaSi();
                        tenCS.setText(res.getData().getTenCaSi());
                        thongTin.setText((res.getData().getMoTa()));
                        numberQuanTam.setText(String.valueOf(res.getCountQuanTam()));

                        Glide.with(getContext()).load(res.getData().getAnh()).into(anhCS);

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
            public void onFailure(Call<GetCaSiByID> call, Throwable t) {
                Toast.makeText(getContext(), "Get danh sach fail", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void layDanhSachCaSi() {
        String header = "bearer " + MainActivity.accessToken;

        ApiServiceV1.apiServiceV1.layDSGoiYCaSi(idCaSi, header).enqueue(new Callback<DanhSachCaSi>() {
            @Override
            public void onResponse(Call<DanhSachCaSi> call, Response<DanhSachCaSi> response) {
                DanhSachCaSi res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        adapterCS = new CaSiAdapter(res.getData(), getActivity());
                        recyclerViewCS.setAdapter(adapterCS);
                        recyclerViewCS.setLayoutManager(managerCS);
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
            public void onFailure(Call<DanhSachCaSi> call, Throwable t) {
                Toast.makeText(getContext(), "Get danh sach fail", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void layDanhSachBaiHat() {
//        1f1091af-9c00-44cc-8102-f3d6605a376a

        String header = "bearer " + MainActivity.accessToken;

        ApiServiceV1.apiServiceV1.layBaiHatCuaCaSi(idCaSi, header).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {

                        if (res.getData().size() == 0) {

                            chuaCoBaihat.setVisibility(View.VISIBLE);
                            return;
                        }

                        dsBaiHats = res.getData();


                        adapter = new BaiHatAdapter(dsBaiHats, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = dsBaiHats.size() * 84;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recyclerView.setLayoutParams(layoutParams);
                        recyclerView.setNestedScrollingEnabled(false);


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
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {
                Toast.makeText(getContext(), "Get danh sach fail", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isChiTietCS = false;
    }

}