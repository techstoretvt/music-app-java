package com.example.musicapp.adapters;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.fragments.TimKiemFragment;

import java.util.ArrayList;

public class TimKiemGanDayAdapter extends RecyclerView.Adapter<TimKiemGanDayAdapter.VHolder> {

    ArrayList<String> data;
    Context context;

    public TimKiemGanDayAdapter(ArrayList<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.tim_kiem_gan_day, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        holder.txtTuKhoa.setText(data.get(holder.getAdapterPosition()));

        holder.layoutText.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                TimKiemFragment.valueSearch.setText(data.get(holder.getAdapterPosition()));
            }
        });

    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView txtTuKhoa;

        CardView layoutText;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            txtTuKhoa = itemView.findViewById(R.id.txtTuKhoa);
            layoutText = itemView.findViewById(R.id.layoutText);
        }
    }
}
