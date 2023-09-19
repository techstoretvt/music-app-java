package com.example.musicapp.fragments;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.AddPlayListActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.adapters.AdapterThuVien;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;

import java.util.ArrayList;
import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThuVienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThuVienFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RecyclerView recyclerView;
    LinearLayoutManager manager;
    public static AdapterThuVien adapter;
    Button btnAddNew;

    public static ArrayList<DanhSachPhat> danhSachPhats = null;

    public ThuVienFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ThuVienFragment.
     */
    // TODO: Rename and change types and number of parameters
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

//        recyclerView.setHasFixedSize(true);
//        recyclerView.setNestedScrollingEnabled(false);

        //get danh sach phat
        if (danhSachPhats != null) {
            adapter = new AdapterThuVien(danhSachPhats, getActivity());
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

                        adapter = new AdapterThuVien(danhSachPhats, getActivity());
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
        }
    }
}