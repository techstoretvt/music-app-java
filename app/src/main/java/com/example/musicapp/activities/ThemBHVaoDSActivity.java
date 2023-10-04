package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.adapters.ThemBHVaoDSAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.TimKiemBaiHat;
import com.example.musicapp.utils.Common;
import com.google.android.material.textfield.TextInputEditText;
import com.google.android.material.textfield.TextInputLayout;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemBHVaoDSActivity extends AppCompatActivity {

    LinearLayoutManager manager;
    public static ThemBHVaoDSAdapter adapterBH = null;
    RecyclerView rvBaiHat;
    TextInputEditText valueSearch;
    TextInputLayout laValueSearch;

    ArrayList<BaiHat> dataBH = new ArrayList<>();

    ImageView btnBack;

    public static String idDanhSach;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_them_bhvao_dsactivity);

        anhXa();


        layDanhSachBaiHat("a");

        setEvent();


    }

    private void setEvent() {
        laValueSearch.setStartIconOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("vao", "onClick: ");
                valueSearch.clearFocus();
                String keyword = String.valueOf(valueSearch.getText());
                layDanhSachBaiHat(keyword);
            }
        });

        valueSearch.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                Log.e("vao", "onClick: ");
                String keyword = String.valueOf(valueSearch.getText());
                layDanhSachBaiHat(keyword);
                return false;
            }
        });

        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                finish();
            }
        });
    }

    private void anhXa() {
        manager = new LinearLayoutManager(this);
        rvBaiHat = findViewById(R.id.rvBaiHat);
        valueSearch = findViewById(R.id.valueSearch);
        laValueSearch = findViewById(R.id.laValueSearch);
        btnBack = findViewById(R.id.btnBack);
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
                        adapterBH = new ThemBHVaoDSAdapter(dataBH, ThemBHVaoDSActivity.this);
                        rvBaiHat.setAdapter(adapterBH);
                        rvBaiHat.setLayoutManager(manager);


                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(ThemBHVaoDSActivity.this, res.getErrMessage(), Toast.LENGTH_SHORT)
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

    @Override
    protected void onDestroy() {
        super.onDestroy();

    }
}