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
import com.example.musicapp.modal.anhxajson.ThongBao;

import java.util.ArrayList;

public class ThongBaoAdapter extends RecyclerView.Adapter<ThongBaoAdapter.VHolder> {


    ArrayList<ThongBao> data;
    Context context;

    public ThongBaoAdapter(ArrayList<ThongBao> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_thong_bao, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.tieuDe.setText(data.get(holder.getAdapterPosition()).getTitle());
        holder.noiDung.setText(data.get(holder.getAdapterPosition()).getContent());
        Glide.with(context).load(data.get(holder.getAdapterPosition()).getUrlImage())
                .into(holder.anhTB);


        holder.layout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.noiDung.setMaxLines(1000);
            }
        });
    }

    public class VHolder extends RecyclerView.ViewHolder {

        ImageView anhTB;
        TextView tieuDe, noiDung;

        LinearLayout layout;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            anhTB = itemView.findViewById(R.id.anhTB);
            tieuDe = itemView.findViewById(R.id.tieuDe);
            noiDung = itemView.findViewById(R.id.noiDung);
            layout = itemView.findViewById(R.id.layout);
        }
    }
}
