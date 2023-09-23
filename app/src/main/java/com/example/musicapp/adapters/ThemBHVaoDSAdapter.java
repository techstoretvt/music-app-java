package com.example.musicapp.adapters;

import android.content.Context;
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
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.TimKiemFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

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

        if (position == 0) {
//            holder.stt.setTextColor(Color.RED);
//        }
//        if (position == 1) {
//            holder.stt.setTextColor(Color.BLUE);
//        }
//        if (position == 2) {
//            holder.stt.setTextColor(Color.YELLOW);
//        }
//
//        if (position == 0 || position == 1 || position == 2) {
//            holder.stt.setTextSize(16);
        }

        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView tenBaiHat, tenCasi;
        LinearLayout linearLayout;

        ImageView imgView;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}
