package com.example.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.NgheSiQuanTamFragment;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.utils.Common;

import java.util.ArrayList;

public class CaSiAdapter extends RecyclerView.Adapter<CaSiAdapter.VHolder> {

    ArrayList<Casi> data;
    Context context;

    public CaSiAdapter(ArrayList<Casi> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_casi, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        Glide.with(holder.anh.getContext()).load(data.get(holder.getAdapterPosition()).getAnh())
                .into(holder.anh);
        holder.tenCaSi.setText(data.get(holder.getAdapterPosition()).getTenCaSi());

        holder.anh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChiTietCaSiFragment.idCaSi = data.get(holder.getAdapterPosition()).getId();
                Common.replace_fragment(new ChiTietCaSiFragment());

                if (NgheSiQuanTamFragment.isQuanTamNgheSi) {
                    ChiTietCaSiFragment.typeBack = 3;
                }
            }
        });

    }

    public class VHolder extends RecyclerView.ViewHolder {
        ImageView anh;
        TextView tenCaSi;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            anh = itemView.findViewById(R.id.anh);
            tenCaSi = itemView.findViewById(R.id.tenCaSi);

        }
    }
}
