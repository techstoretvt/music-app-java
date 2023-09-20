package com.example.musicapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class DanhSachPhatAdapter extends RecyclerView.Adapter<DanhSachPhatAdapter.VHolder> {


    public static Boolean isAddDS = false;
    ArrayList<DanhSachPhat> data;
    Context context;

    public DanhSachPhatAdapter(ArrayList<DanhSachPhat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_danhsachphat, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.tenDS.setText(data.get(holder.getAdapterPosition()).getTenDanhSach());
//        Glide.with(holder.itemView.getContext()).load(data.get(position).getAnhBia()).into(holder.imgView);
        if (data.get(holder.getAdapterPosition()).getChiTietDanhSachPhats() != null) {

            int slBh = data.get(holder.getAdapterPosition()).getChiTietDanhSachPhats().size();
            if (slBh > 0 && slBh < 4) {
                Glide.with(holder.anh1.getContext())
                        .load(data.get(holder.getAdapterPosition()).
                                getChiTietDanhSachPhats().get(0).getBaihat().getAnhBia())
                        .into(holder.img1anh);
            } else if (slBh >= 4) {
                holder.layout4anh.setVisibility(View.VISIBLE);
                holder.img1anh.setVisibility(View.GONE);
                Glide.with(holder.anh1.getContext())
                        .load(data.get(holder.getAdapterPosition()).
                                getChiTietDanhSachPhats().get(0).getBaihat().getAnhBia())
                        .into(holder.anh1);
                Glide.with(holder.anh1.getContext())
                        .load(data.get(holder.getAdapterPosition()).
                                getChiTietDanhSachPhats().get(1).getBaihat().getAnhBia())
                        .into(holder.anh2);
                Glide.with(holder.anh1.getContext())
                        .load(data.get(holder.getAdapterPosition()).
                                getChiTietDanhSachPhats().get(2).getBaihat().getAnhBia())
                        .into(holder.anh3);
                Glide.with(holder.anh1.getContext())
                        .load(data.get(holder.getAdapterPosition()).
                                getChiTietDanhSachPhats().get(3).getBaihat().getAnhBia())
                        .into(holder.anh4);
            }
        }

        //set onclick
        holder.layoutDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (isAddDS) {
                    Log.e("them vao ds", "sdfsd");
                    if (BaiHatAdapter.idBaiHat == null) {
                        return;
                    }

                    BodyThemBHVaoDS body = new BodyThemBHVaoDS(BaiHatAdapter.idBaiHat,
                            data.get(holder.getAdapterPosition()).getId());
                    String header = "bearer " + MainActivity.accessToken;

                    ApiServiceV1.apiServiceV1.themBaiHatVaoDS(body, header).enqueue(new Callback<ResponseDefault>() {
                        @Override
                        public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                            ResponseDefault res = response.body();
                            if (res != null) {
                                if (res.getErrCode() == 0) {
                                    Toast.makeText(view.getContext(), "Đã thêm vào danh sách",
                                                    Toast.LENGTH_SHORT)
                                            .show();

                                    BsBaiHat.md.dismiss();

                                } else {
                                    if (res.getStatus() == 401) {
                                        System.exit(0);
                                    }
                                    if (res.getErrCode() == 2) {
                                        Toast.makeText(view.getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                                .show();
                                    }
                                    Log.e("them vao ds fail", res.getErrMessage());
                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<ResponseDefault> call, Throwable t) {
                            Log.e("them vao ds fail", "sdfsd");
                        }
                    });


                    return;
                }


                Fragment fragment = new ChiTietThuVienFragment();

                MainActivity.fragmentManager2.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();

                ChiTietThuVienFragment.isChiTietDS = true;

                ChiTietThuVienFragment.idDanhSachPhat = data.get(holder.getAdapterPosition()).getId();
            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView tenDS;
        ImageView anhDS;

        LinearLayout layoutDS;

        ImageView img1anh, anh1, anh2, anh3, anh4;
        LinearLayout layout4anh;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            tenDS = itemView.findViewById(R.id.tenDS);
            layoutDS = itemView.findViewById(R.id.layoutDS);
            img1anh = itemView.findViewById(R.id.img1anh);
            layout4anh = itemView.findViewById(R.id.layout4anh);
            anh1 = itemView.findViewById(R.id.anh1);
            anh2 = itemView.findViewById(R.id.anh2);
            anh3 = itemView.findViewById(R.id.anh3);
            anh4 = itemView.findViewById(R.id.anh4);


        }
    }
}
