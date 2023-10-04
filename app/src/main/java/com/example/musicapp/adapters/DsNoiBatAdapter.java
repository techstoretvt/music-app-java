package com.example.musicapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.CT_ThuVien_NoiBatFragment;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.DaTaiFragment;
import com.example.musicapp.fragments.TimKiemFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.CurrentMiniNhacFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DS_Noi_Bat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DsNoiBatAdapter extends RecyclerView.Adapter<DsNoiBatAdapter.VHolder> {

    ArrayList<DS_Noi_Bat> data;
    Context context;

    public DsNoiBatAdapter(ArrayList<DS_Noi_Bat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_ds_noi_bat, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.tenDs.setText(data.get(holder.getAdapterPosition()).getTenDS());
        Glide.with(holder.tenDs.getContext()).load(data.get(holder.getAdapterPosition()).getUrlAnh())
                .into(holder.anhDs);

        holder.anhDs.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                CT_ThuVien_NoiBatFragment.tenDs = data.get(holder.getAdapterPosition()).getTenDS();
                CT_ThuVien_NoiBatFragment.linkAnh = data.get(holder.getAdapterPosition()).getUrlAnh();
                Common.replace_fragment(new CT_ThuVien_NoiBatFragment());
            }
        });

    }

    public class VHolder extends RecyclerView.ViewHolder {

        ImageView anhDs;
        TextView tenDs;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            anhDs = itemView.findViewById(R.id.anhDs);
            tenDs = itemView.findViewById(R.id.tenDs);

        }
    }
}
