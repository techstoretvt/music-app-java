package com.example.musicapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CheckBox;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ThemBHVaoDSActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.example.musicapp.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThemBHVaoDSAdapter extends RecyclerView.Adapter<ThemBHVaoDSAdapter.VHolder> {

    public static String idBaiHat = null;
    public static String idCaSi = null;

    public static BsBaiHat md = new BsBaiHat();
    ArrayList<BaiHat> data;
    Context context;

    public ThemBHVaoDSAdapter(ArrayList<BaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_them_bh_vao_ds, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.tenBaiHat.setText(data.get(position).getTenBaiHat());
        holder.tenCasi.setText(data.get(position).getCasi().getTenCaSi());
        Glide.with(holder.itemView.getContext()).load(data.get(position).getAnhBia()).into(holder.imgView);

        String idBaiHat = data.get(holder.getAdapterPosition()).getId();
        for (BaiHat i : ChiTietThuVienFragment.danhBaiHats) {
            if (i.getId().equals(idBaiHat)) {
                holder.icon.setImageResource(R.drawable.baseline_check_white);
                holder.checkBox.setChecked(true);
                break;
            }
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idDanhSach = ThemBHVaoDSActivity.idDanhSach;
                String header = Common.getHeader();

                if (!holder.checkBox.isChecked()) {
                    BodyThemBHVaoDS body = new BodyThemBHVaoDS(idBaiHat, idDanhSach);
                    ApiServiceV1.apiServiceV1.themBaiHatVaoDS(body, header).enqueue(new Callback<ThemBHVaoDS>() {
                        @Override
                        public void onResponse(Call<ThemBHVaoDS> call, Response<ThemBHVaoDS> response) {
                            ThemBHVaoDS res = response.body();
                            if (res != null) {
                                if (res.getErrCode() == 0) {
                                    BaiHat newBH = res.getData();
                                    if (ChiTietThuVienFragment.danhBaiHats != null) {
                                        ChiTietThuVienFragment.danhBaiHats.add(newBH);
                                    }
                                    holder.icon.setImageResource(R.drawable.baseline_check_white);
                                    holder.checkBox.setChecked(true);

                                } else {
                                    if (res.getStatus() == 401) {
                                        System.exit(0);
                                    }
                                    Toast.makeText(view.getContext(), res.getErrMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ThemBHVaoDS> call, Throwable t) {
                            Log.e("Loi them bh vao ds", "");
                        }
                    });
                } else {

                    ApiServiceV1.apiServiceV1.xoaBaiHatKhoiDS(idDanhSach, idBaiHat, header).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                            ResponseDefault res = response.body();
                            if (res != null) {
                                if (res.getErrCode() == 0) {
                                    holder.icon.setImageResource(R.drawable.baseline_add_circle_white);
                                    holder.checkBox.setChecked(false);
                                } else {
                                    if (res.getStatus() == 401) {
                                        System.exit(0);
                                    }
                                    Toast.makeText(view.getContext(), res.getErrMessage(),
                                            Toast.LENGTH_SHORT).show();
                                }

                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                            Log.e("Loi xoa bh khoi ds", "");
                        }
                    });
                }
            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView tenBaiHat, tenCasi;
        LinearLayout linearLayout;

        ImageView imgView, icon;

        CheckBox checkBox;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
            icon = itemView.findViewById(R.id.icon);
            checkBox = itemView.findViewById(R.id.checkbox);
        }
    }
}
