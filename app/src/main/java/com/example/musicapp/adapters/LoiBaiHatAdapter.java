package com.example.musicapp.adapters;

import android.content.Context;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.TestActivity;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.LoiBaiHatFragment;
import com.example.musicapp.modal.anhxajson.LoiBaiHat;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

public class LoiBaiHatAdapter extends RecyclerView.Adapter<LoiBaiHatAdapter.VHolder> {


    ArrayList<LoiBaiHat> data;
    Context context;

    public static int currentPosition = -1;

    public LoiBaiHatAdapter(ArrayList<LoiBaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_loi_bai_hat, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        setUI(holder, position);

        LoiBaiHat currentData = data.get(holder.getAdapterPosition());

        holder.loiBH.setText(currentData.getLoiBaiHat());


        if (currentPosition != -1 &&
                currentPosition == holder.getAdapterPosition() && currentPosition == position) {
            holder.loiBH.setTextColor(Color.YELLOW);
        } else {
            holder.loiBH.setTextColor(Color.WHITE);
        }


        setEvent(holder);
    }

    private void setEvent(VHolder holder) {

        holder.layoutLoiBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoiBaiHat currentData = data.get(holder.getAdapterPosition());

                double currentTime = currentData.getThoiGian();
                MediaCustom.setCurrentTimeDouble(currentTime);
                MediaCustom.play();
                holder.loiBH.setTextColor(Color.YELLOW);

                LoiBaiHatFragment.isScrolling = false;
                if (ChiTietNhacActivity.isChiTietNhac) {
                    if (ChiTietNhacActivity.btnPausePlay != null) {
                        ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
                    }
                }

            }
        });

    }

    private void setUI(VHolder holder, int position) {

    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView loiBH;
        LinearLayout layoutLoiBH;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            loiBH = itemView.findViewById(R.id.loiBH);
            layoutLoiBH = itemView.findViewById(R.id.layoutLoiBH);


        }
    }
}
