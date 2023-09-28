package com.example.musicapp.fragments;

import static android.app.Activity.RESULT_OK;

import android.annotation.SuppressLint;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.Nullable;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.AddPlayListActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.DanhSachPhatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.example.musicapp.utils.Common;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVienFragment extends Fragment {
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    public static DanhSachPhatAdapter adapter;
    Button btnAddNew;

    MaterialToolbar topAppBar;

    LinearLayout layoutYeuThich, layoutNgheSi, layoutDaTai;

    public static ArrayList<DanhSachPhat> danhSachPhats = null;

    public ThuVienFragment() {
        // Required empty public constructor
    }

    public static ThuVienFragment newInstance(String param1, String param2) {
        ThuVienFragment fragment = new ThuVienFragment();
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

    @SuppressLint("MissingInflatedId")
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_thu_vien, container, false);
        btnAddNew = view.findViewById(R.id.btnAddNew);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        topAppBar = view.findViewById(R.id.topAppBar);
        layoutYeuThich = view.findViewById(R.id.layoutYeuThich);
        layoutNgheSi = view.findViewById(R.id.layoutNgheSi);
        layoutDaTai = view.findViewById(R.id.layoutDaTai);

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);

        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#4c49515c"), Color.BLACK}
        );
        recyclerView.setBackground(gradientDrawable);

        //get danh sach phat
        if (danhSachPhats != null) {
            adapter = new DanhSachPhatAdapter(danhSachPhats, getActivity());
            recyclerView.setAdapter(adapter);
            recyclerView.setLayoutManager(manager);
        } else {
            getDanhSachPhat();
        }

        btnAddNew.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), AddPlayListActivity.class);
                intent.putExtra("email", "dfhasdf");
                startActivityForResult(intent, 100);
            }
        });

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e(String.valueOf(item), "");
                if (String.valueOf(item).equals("search")) {
                    TimKiemFragment.typeBack = 2;
                    Common.replace_fragment(new TimKiemFragment());
                } else if (String.valueOf(item).equals("micro")) {
                    Log.e("vao", "");
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                    startActivityForResult(intent, 1);

                }
                return false;
            }
        });


        layoutYeuThich.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new YeuThichFragment());
            }
        });

        layoutNgheSi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new NgheSiQuanTamFragment());
            }
        });

        layoutDaTai.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Common.replace_fragment(new DaTaiFragment());
            }
        });


        return view;
    }

    private void getDanhSachPhat() {

        String header = "bearer " + MainActivity.accessToken;
        ApiServiceV1.apiServiceV1.getDanhSachPhat(header).enqueue(new Callback<GetListPlaylist>() {
            @Override
            public void onResponse(Call<GetListPlaylist> call, Response<GetListPlaylist> response) {
                GetListPlaylist res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        danhSachPhats = res.getData();

                        adapter = new DanhSachPhatAdapter(danhSachPhats, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                    }
                }
            }

            @Override
            public void onFailure(Call<GetListPlaylist> call, Throwable t) {
                Toast.makeText(getContext(), "Error from api get danh sach in ThuVienFragment",
                        Toast.LENGTH_SHORT).show();
            }
        });
    }


    @Override
    public void onActivityResult(int requestCode, int resultCode, @Nullable Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == 100 && resultCode == 101) {
            Bundle bundle = data.getBundleExtra("myBundle");
            DanhSachPhat newData = (DanhSachPhat) bundle.getSerializable("newData");
            danhSachPhats.add(newData);
            adapter.notifyDataSetChanged();
        } else if (requestCode == 1 && resultCode == RESULT_OK) {
            // Trích xuất văn bản từ giọng nói
            List<String> results = data.getStringArrayListExtra(RecognizerIntent.EXTRA_RESULTS);
            String text = results.get(0);

            // Sử dụng văn bản để tìm kiếm
//            Intent intent = new Intent(Intent.ACTION_WEB_SEARCH);
//            intent.putExtra(SearchManager.QUERY, text);
//            startActivity(intent);

            TimKiemFragment.typeBack = 1;
            TimKiemFragment.strValueSearch = text;
            Common.replace_fragment(new TimKiemFragment());


        }
    }
}