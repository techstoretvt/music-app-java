package com.example.musicapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
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
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.progressindicator.LinearProgressIndicator;

import java.util.ArrayList;

public class AdapterKhamPha extends RecyclerView.Adapter<AdapterKhamPha.VHolder> {

    ArrayList<BaiHat> data;
    Context context;

    public AdapterKhamPha(ArrayList<BaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_khampha, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.stt.setText(String.valueOf(position + 1));
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
                Log.e("Ten bai hat", data.get(holder.getAdapterPosition()).getTenBaiHat());
                MainActivity.currentBaiHat = data.get(holder.getAdapterPosition());

                // Create a new thread
                MainActivity.phatNhacMini(data.get(holder.getAdapterPosition()).getAnhBia(),
                        data.get(holder.getAdapterPosition()).getTenBaiHat(),
                        data.get(holder.getAdapterPosition()).getCasi().getTenCaSi());

                MediaCustom.phatNhac(data.get(holder.getAdapterPosition()).getLinkBaiHat());
                MainActivity.typePlay = 1;
                MainActivity.positionPlay = holder.getAdapterPosition();

                if (holder.getAdapterPosition() == 0) {
                    MainActivity.btnPrev.setVisibility(View.GONE);
                } else {
                    MainActivity.btnPrev.setVisibility(View.VISIBLE);
                }

//                ColorDrawable colorDrawable = new ColorDrawable(Color.GREEN);
//                holder.linearLayout.setBackground(colorDrawable);


            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView stt, tenBaiHat, tenCasi;
        LinearLayout linearLayout;

        ImageView imgView;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.txtStt);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
        }
    }
}
