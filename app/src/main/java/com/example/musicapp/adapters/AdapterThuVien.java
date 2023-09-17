package com.example.musicapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

public class AdapterThuVien extends RecyclerView.Adapter<AdapterThuVien.VHolder> {

    ArrayList<DanhSachPhat> data;
    Context context;

    public AdapterThuVien(ArrayList<DanhSachPhat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_thuvien, parent, false);
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


        //set onclick
        holder.layoutDS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Fragment fragment = new ChiTietThuVienFragment();

                MainActivity.fragmentManager2.beginTransaction()
                        .replace(R.id.frame_layout, fragment)
                        .commit();
            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView tenDS;
        ImageView anhDS;

        LinearLayout layoutDS;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            tenDS = itemView.findViewById(R.id.tenDS);
            anhDS = itemView.findViewById(R.id.anhDS);
            layoutDS = itemView.findViewById(R.id.layoutDS);

        }
    }
}
