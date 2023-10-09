package com.example.musicapp.fragments;

import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.AddPlayListActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.adapters.DanhSachPhatAdapter;
import com.example.musicapp.adapters.XemCaSiAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.ThuVienFragment;
import com.example.musicapp.modal.anhxajson.BaiHat_CaSi;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class Bs_XemNS extends BottomSheetDialogFragment {

    public static String TAG = "BottomSheetThemBHVaoDS";
    RecyclerView recyclerView;
    XemCaSiAdapter adapter;
    LinearLayoutManager manager;

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_xem_ns, container, false);
        anhXa(view);

        ArrayList<Casi> listCs = new ArrayList<>();

        for (BaiHat_CaSi i : BaiHatAdapter.currentBaiHat.getBaiHat_caSis()) {
            listCs.add(i.getCasi());
        }

        adapter = new XemCaSiAdapter(listCs, getContext(), getActivity());
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        return view;
    }

    private void anhXa(View view) {
        recyclerView = view.findViewById(R.id.recycleView);
        manager = new LinearLayoutManager(getContext());
    }


    @Override
    public void onDestroy() {

        super.onDestroy();
    }
}
