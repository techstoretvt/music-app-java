package com.example.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.modal.anhxajson.BaiHat;

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
        holder.tenCasi.setText(data.get(position).getTenCaSi());
    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView stt, tenBaiHat, tenCasi;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.txtStt);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
        }
    }
}
