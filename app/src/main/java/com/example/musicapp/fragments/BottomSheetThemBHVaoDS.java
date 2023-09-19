package com.example.musicapp.fragments;

import android.content.Intent;
import android.media.Image;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.AddPlayListActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.adapters.AdapterThuVien;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BottomSheetThemBHVaoDS extends BottomSheetDialogFragment {

    public static String TAG = "BottomSheetThemBHVaoDS";


    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AdapterThuVien adapter;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_bottom_sheet_them_bh_vao_ds, container, false);

        ImageView btnAddNew = view.findViewById(R.id.btnAddNew);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getActivity(), RecyclerView.VERTICAL, false);
        AdapterThuVien.isAddDS = true;

        if (ThuVienFragment.danhSachPhats != null) {
            adapter = new AdapterThuVien(ThuVienFragment.danhSachPhats, getActivity());
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
        Toast.makeText(getContext(), "vao",
                Toast.LENGTH_SHORT).show();

        String header = "bearer " + MainActivity.accessToken;
        ApiServiceV1.apiServiceV1.getDanhSachPhat(header).enqueue(new Callback<GetListPlaylist>() {
            @Override
            public void onResponse(Call<GetListPlaylist> call, Response<GetListPlaylist> response) {
                GetListPlaylist res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ThuVienFragment.danhSachPhats = res.getData();

                        adapter = new AdapterThuVien(ThuVienFragment.danhSachPhats, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        Toast.makeText(getContext(), "Success",
                                Toast.LENGTH_SHORT).show();

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
            ThuVienFragment.danhSachPhats.add(newData);
            adapter.notifyDataSetChanged();
        }
    }

    @Override
    public void onDestroy() {
        AdapterThuVien.isAddDS = false;
        AdapterKhamPha.idBaiHat = null;
        super.onDestroy();
    }
}
