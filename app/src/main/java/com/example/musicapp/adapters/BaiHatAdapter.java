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

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.VHolder> {

    public static String idBaiHat = null;
    public static String idCaSi = null;

    public static String linkMV = null;

    public static BsBaiHat md = new BsBaiHat();
    ArrayList<BaiHat> data;
    Context context;

    public BaiHatAdapter(ArrayList<BaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_baihat, parent, false);
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


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaCustom.position = holder.getAdapterPosition();
                MediaCustom.danhSachPhats = data;

                if (ChiTietThuVienFragment.isChiTietDS) {
                    MediaCustom.typeDanhSachPhat = 2;
                    MediaCustom.tenLoai = ChiTietThuVienFragment.tenDanhSach;
                } else if (ChiTietCaSiFragment.isChiTietCS) {
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = ChiTietCaSiFragment.strTenCS;
                } else if (TimKiemFragment.isTimKiem) {
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = "Tìm kiếm";
                } else {
                    //kham pha
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = "Khám phá";
                }

                // Create a new thread
                MainActivity.phatNhacMini(data.get(holder.getAdapterPosition()).getAnhBia(),
                        data.get(holder.getAdapterPosition()).getTenBaiHat(),
                        data.get(holder.getAdapterPosition()).getCasi().getTenCaSi());

                MediaCustom.phatNhac(data.get(holder.getAdapterPosition()).getLinkBaiHat());

                if (holder.getAdapterPosition() == 0) {
                    MainActivity.btnPrev.setVisibility(View.GONE);
                } else {
                    MainActivity.btnPrev.setVisibility(View.VISIBLE);
                }

//                ColorDrawable colorDrawable = new ColorDrawable(Color.GREEN);
//                holder.linearLayout.setBackground(colorDrawable);


                MainActivity.layoutTencasi.callOnClick();


            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                idBaiHat = data.get(holder.getAdapterPosition()).getId();
                idCaSi = data.get(holder.getAdapterPosition()).getCasi().getId();
                linkMV = data.get(holder.getAdapterPosition()).getLinkMV();

                Log.e("link mv", String.valueOf(data.get(holder.getAdapterPosition()).getLinkMV()));

                md.show(MainActivity.supportFragmentManager, BsBaiHat.TAG);
            }
        });

    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView stt, tenBaiHat, tenCasi;
        LinearLayout linearLayout;

        ImageView imgView, btnMore;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.txtStt);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
            btnMore = itemView.findViewById(R.id.btnMore);
        }
    }
}
