package com.example.musicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.media.Image;
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
import com.example.musicapp.activities.MVBaiHatActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.MvBaiHatFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class MVBaiHatAdapter extends RecyclerView.Adapter<MVBaiHatAdapter.VHolder> {


    int layout = R.layout.item_mv_bai_hat;
    ArrayList<BaiHat> data;
    Context context;

    public MVBaiHatAdapter(ArrayList<BaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    public MVBaiHatAdapter(ArrayList<BaiHat> data, Context context, int layout) {
        this.data = data;
        this.context = context;
        this.layout = layout;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(layout, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        setUI(holder, position);

        setEvent(holder);
    }

    private void setEvent(VHolder holder) {
        holder.anhBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                if (MvBaiHatFragment.isMVBaiHatFragment) {
                    Intent intent = new Intent(context, MVBaiHatActivity.class);
                    intent.putExtra("idBaiHat", data.get(holder.getAdapterPosition()).getId());

                    context.startActivity(intent);

                    return;
                }


                String idBaiHat = data.get(holder.getAdapterPosition()).getId();
                MVBaiHatActivity.listIdBaiHat.add(idBaiHat);

                if (MVBaiHatActivity.tenBH != null) {
                    MVBaiHatActivity.getVideoBaiHat(idBaiHat);
                    MVBaiHatActivity.getGoiYMV();
                    MVBaiHatActivity.scrollView.scrollTo(0, 0);
                }

            }
        });

    }

    private void setUI(VHolder holder, int position) {
        BaiHat currentBH = data.get(holder.getAdapterPosition());
        holder.tenBH.setText(currentBH.getTenBaiHat());
        Glide.with(context).load(currentBH.getAnhBia()).into(holder.anhBH);
        Glide.with(context).load(currentBH.getAnhBia()).into(holder.anhBH2);

        String strTenCS = "";
        for (int i = 0; i < currentBH.getBaiHat_caSis().size(); i++) {
            if (i == 0) {
                strTenCS = currentBH.getBaiHat_caSis().get(i).getCasi().getTenCaSi();
            } else {
                strTenCS += ", " +
                        currentBH.getBaiHat_caSis().get(i).getCasi().getTenCaSi();
            }
        }

        holder.tenCS.setText(strTenCS);

        int tg = (int) currentBH.getThoiGian() / 1000;

        int phut = tg / 60;
        int giay = tg - phut * 60;

        if (giay < 10) {
            holder.tgBH.setText(String.valueOf(phut) + ":0" + String.valueOf(giay));
        } else {
            holder.tgBH.setText(String.valueOf(phut) + ":" + String.valueOf(giay));
        }


    }

    public class VHolder extends RecyclerView.ViewHolder {

        ImageView anhBH, anhBH2;
        TextView tenBH, tenCS, tgBH;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            anhBH = itemView.findViewById(R.id.anhBH);
            anhBH2 = itemView.findViewById(R.id.anhBH2);
            tenBH = itemView.findViewById(R.id.tenBH);
            tenCS = itemView.findViewById(R.id.tenCS);
            tgBH = itemView.findViewById(R.id.tgBH);


        }
    }
}
