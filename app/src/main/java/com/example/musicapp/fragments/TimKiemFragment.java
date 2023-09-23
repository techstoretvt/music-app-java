package com.example.musicapp.fragments;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.adapters.CaSiAdapter;
import com.example.musicapp.adapters.TimKiemGanDayAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.TimKiemBaiHat;
import com.example.musicapp.modal.anhxajson.TimKiemCaSi;
import com.example.musicapp.utils.Common;
import com.google.android.material.divider.MaterialDivider;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

import org.json.JSONArray;
import org.json.JSONException;

import java.lang.reflect.Type;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

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

    public static Boolean isTimKiem = false;

    RecyclerView rvTimKiemGanDay;
    RecyclerView rvBaiHat;
    RecyclerView rvCaSi;
    LinearLayoutManager manager;
    LinearLayoutManager managerBH;
    LinearLayoutManager managerCS;
    TimKiemGanDayAdapter adapter;
    CaSiAdapter adapterCS;
    BaiHatAdapter adapterBH;
    ArrayList<String> dataTimKiemGanDay;

    TextView txtBH, txtCS;
    MaterialDivider lineBH, lineCS;
    LinearLayout laTimBH, laTimCS, laTimKiemGanDay;
    public static TextInputEditText valueSearch;
    TextInputLayout laValueSearch;

    ImageView btnBack;

    ArrayList<BaiHat> dataBH = new ArrayList<>();
    ArrayList<Casi> dataCS = new ArrayList<>();

    SharedPreferences sharedPreferences;

    public static int typeBack = 1;

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
        rvBaiHat = view.findViewById(R.id.rvBaiHat);
        managerBH = new LinearLayoutManager(getContext());
        valueSearch = view.findViewById(R.id.valueSearch);
        laValueSearch = view.findViewById(R.id.laValueSearch);
        rvCaSi = view.findViewById(R.id.rvCaSi);
        btnBack = view.findViewById(R.id.btnBack);
        managerCS = new GridLayoutManager(getContext(), 2);
        laTimKiemGanDay = view.findViewById(R.id.laTimKiemGanDay);
        isTimKiem = true;
        sharedPreferences = getContext()
                .getSharedPreferences("DataLocal", Context.MODE_PRIVATE);

        setEventLoaiTK();
        setEventInput();

        loadTimKiemGanDay();

//        layDanhSachBaiHat("chau");
//        layDanhSachCaSi("Chau");


        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (typeBack == 1) {
                    Common.replace_fragment(new KhamPhaFragment());
                } else if (typeBack == 2) {
                    Common.replace_fragment(new ThuVienFragment());
                }

            }
        });

        return view;
    }

    private void setEventInput() {
        laValueSearch.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vao", "onClick: ");
                valueSearch.clearFocus();
                submitTimKiem();
            }
        });

        valueSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("vao", "onClick: ");
                submitTimKiem();
                return false;
            }
        });
    }

    private void submitTimKiem() {
        String keyword = String.valueOf(valueSearch.getText());
        if (loaiTimKiem == 1) {
            layDanhSachBaiHat(keyword);
            rvCaSi.setVisibility(View.GONE);
            rvBaiHat.setVisibility(View.VISIBLE);
            laTimKiemGanDay.setVisibility(View.GONE);
        } else {
            layDanhSachCaSi(keyword);
            rvCaSi.setVisibility(View.VISIBLE);
            rvBaiHat.setVisibility(View.GONE);
            laTimKiemGanDay.setVisibility(View.GONE);
        }

        //test
        String arrayListJson = sharedPreferences.getString("ArrTimKiemGanDay", "");
        Gson gson = new Gson();
        if (!arrayListJson.isEmpty()) {
            Type type = new TypeToken<ArrayList>() {
            }.getType();
            ArrayList loadedArrayList = gson.fromJson(arrayListJson, type);

            for (int i = 0; i < loadedArrayList.size(); i++) {
                Log.e(loadedArrayList.get(i).toString(), "");
            }
            SharedPreferences.Editor editor = sharedPreferences.edit();
            if (!loadedArrayList.contains(keyword)) {
                loadedArrayList.add(0, keyword);

                if (loadedArrayList.size() > 10) {
                    loadedArrayList.remove(10);
                }

            } else {
                loadedArrayList.remove(keyword);
                loadedArrayList.add(0, keyword);
            }

            String Json = gson.toJson(loadedArrayList);
            editor.putString("ArrTimKiemGanDay", Json);
            editor.apply();

        } else {
            ArrayList<String> arrayList = new ArrayList<>();
            arrayList.add(keyword);
            SharedPreferences.Editor editor = sharedPreferences.edit();

            String Json = gson.toJson(arrayList);
            editor.putString("ArrTimKiemGanDay", Json);
            editor.apply();
        }


    }

    private void loadTimKiemGanDay() {
        String arrayListJson = sharedPreferences.getString("ArrTimKiemGanDay", "");
        Gson gson = new Gson();
        if (!arrayListJson.isEmpty()) {
            laTimKiemGanDay.setVisibility(View.VISIBLE);
            Type type = new TypeToken<ArrayList>() {
            }.getType();
            ArrayList loadedArrayList = gson.fromJson(arrayListJson, type);

            dataTimKiemGanDay = loadedArrayList;

            adapter = new TimKiemGanDayAdapter(dataTimKiemGanDay, getContext());
            rvTimKiemGanDay.setAdapter(adapter);
            rvTimKiemGanDay.setLayoutManager(manager);
        }
    }

    private void layDanhSachBaiHat(String keyword) {
        String header = Common.getHeader();
        String tenBaiHat = keyword.trim();
        String limit = "";
        String offset = "";

        ApiServiceV1.apiServiceV1.timKiemBaiHat(tenBaiHat, offset, limit, header).enqueue(new Callback<TimKiemBaiHat>() {
            @Override
            public void onResponse(Call<TimKiemBaiHat> call, Response<TimKiemBaiHat> response) {
                TimKiemBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {

                        ArrayList<BaiHat> arr = new ArrayList<>();
                        for (int i = 0; i < res.getData().size(); i++) {
                            arr.add(res.getItemIndex(i));
                        }
                        dataBH = arr;
                        adapterBH = new BaiHatAdapter(dataBH, getContext());
                        rvBaiHat.setAdapter(adapterBH);
                        rvBaiHat.setLayoutManager(managerBH);


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
            public void onFailure(Call<TimKiemBaiHat> call, Throwable t) {
                Log.e("Loi call api  tim kiem", "");
            }
        });
    }

    private void layDanhSachCaSi(String keyword) {
        String header = Common.getHeader();
        String tenCS = keyword.trim();
        String limit = "";
        String offset = "";

        ApiServiceV1.apiServiceV1.timKiemCaSi(tenCS, offset, limit, header).enqueue(new Callback<TimKiemCaSi>() {
            @Override
            public void onResponse(Call<TimKiemCaSi> call, Response<TimKiemCaSi> response) {
                TimKiemCaSi res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        Log.e("Thanh Cong", "onResponse: ");
                        ArrayList<Casi> arr = new ArrayList<>();
                        for (int i = 0; i < res.getData().size(); i++) {
                            Log.e(res.getCaSiIndex(i).getTenCaSi(), "");
                            arr.add(res.getCaSiIndex(i));
                        }
                        dataCS = arr;
                        adapterCS = new CaSiAdapter(dataCS, getContext());
                        rvCaSi.setAdapter(adapterCS);
                        rvCaSi.setLayoutManager(managerCS);

                    } else {
                        Log.e("that bai", "onResponse: ");
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<TimKiemCaSi> call, Throwable t) {
                Log.e("Loi call api  tim kiem", "");
            }
        });
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

    @Override
    public void onDestroy() {
        isTimKiem = false;
        super.onDestroy();
    }
}