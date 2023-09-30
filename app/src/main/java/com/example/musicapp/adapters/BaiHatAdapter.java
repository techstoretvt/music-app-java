package com.example.musicapp.adapters;

import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
import android.os.Handler;
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
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.database.MusicAppHelper;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.TimKiemFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BaiHatAdapter extends RecyclerView.Adapter<BaiHatAdapter.VHolder> {

    public static String idBaiHat = null;
    public static String idCaSi = null;

    public static String linkMV = null;
    public static String linkBaiHat = null;
    public static String linkAnh = null;

    public static BaiHat currentBaiHat = null;

    public static ImageView iconDownLoad = null;


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

        MusicAppHelper musicAppHelper = new MusicAppHelper(context,
                "MusicApp.sqlite", null, 1);

        Cursor dataBaiHat = musicAppHelper.GetData(String.format(
                "SELECT * FROM BaiHat where id = '%s'",
                data.get(position).getId()
        ));

        while (dataBaiHat.moveToNext()) {
            holder.iconDownLoad.setVisibility(View.VISIBLE);
        }


        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(view.getContext(), ChiTietNhacActivity.class);
                view.getContext().startActivity(intent);
                MainActivity.progess_phatNhac.setProgress(50);

                android.os.Handler handler = new Handler();
                handler.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        MainActivity.progess_phatNhac.setProgress(100);
                    }
                }, 1000);


                if (ChiTietThuVienFragment.isChiTietDS) {
                    MediaCustom.position = holder.getAdapterPosition();
                    MediaCustom.danhSachPhats = data;
                    MediaCustom.typeDanhSachPhat = 2;
                    MediaCustom.tenLoai = ChiTietThuVienFragment.tenDanhSach;
                } else if (ChiTietCaSiFragment.isChiTietCS) {
                    MediaCustom.position = 0;
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = ChiTietCaSiFragment.strTenCS;

                    getListRandomBaiHat(data.get(holder.getAdapterPosition()));
                } else if (TimKiemFragment.isTimKiem) {
                    MediaCustom.position = 0;
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = "Tìm kiếm";

                    getListRandomBaiHat(data.get(holder.getAdapterPosition()));
                } else if (YeuThichFragment.isYeuThich) {
                    MediaCustom.position = holder.getAdapterPosition();
                    MediaCustom.danhSachPhats = data;
                    MediaCustom.typeDanhSachPhat = 2;
                    MediaCustom.tenLoai = "Yêu thích";
                } else {
                    //kham pha
                    MediaCustom.position = 0;
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = "Khám phá";
                    getListRandomBaiHat(data.get(holder.getAdapterPosition()));
                }

                // Create a new thread
                MainActivity.phatNhacMini(data.get(holder.getAdapterPosition()).getAnhBia(),
                        data.get(holder.getAdapterPosition()).getTenBaiHat(),
                        data.get(holder.getAdapterPosition()).getCasi().getTenCaSi());

                if (MediaCustom.phatNhac(data.get(holder.getAdapterPosition()).getLinkBaiHat())) {
//                    MainActivity.progess_phatNhac.setProgress(0);
                }

                if (holder.getAdapterPosition() == 0) {
                    MainActivity.btnPrev.setVisibility(View.GONE);
                } else {
                    MainActivity.btnPrev.setVisibility(View.VISIBLE);
                }

//                ColorDrawable colorDrawable = new ColorDrawable(Color.GREEN);
//                holder.linearLayout.setBackground(colorDrawable);


//                MainActivity.layoutTencasi.callOnClick();


            }
        });

        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                idBaiHat = data.get(holder.getAdapterPosition()).getId();
                idCaSi = data.get(holder.getAdapterPosition()).getCasi().getId();
                linkMV = data.get(holder.getAdapterPosition()).getLinkMV();
                linkBaiHat = data.get(holder.getAdapterPosition()).getLinkBaiHat();
                linkAnh = data.get(holder.getAdapterPosition()).getAnhBia();

                currentBaiHat = data.get(holder.getAdapterPosition());
                iconDownLoad = holder.iconDownLoad;

                Log.e("link mv", String.valueOf(data.get(holder.getAdapterPosition()).getLinkMV()));

                md.show(MainActivity.supportFragmentManager, BsBaiHat.TAG);
            }
        });

    }

    private void getListRandomBaiHat(BaiHat bh) {
        String header = Common.getHeader();
        int limit = 20;
        String id = bh.getId();

        ApiServiceV1.apiServiceV1.getListRandomBaiHat(limit, new String[]{id, "skfskdfjsdhjfh"}, header).enqueue(new Callback<GetListBaiHat>() {
            @Override
            public void onResponse(Call<GetListBaiHat> call, Response<GetListBaiHat> response) {
                GetListBaiHat res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        ArrayList<BaiHat> listBH;
                        if (res.getData() != null) listBH = res.getData();
                        else listBH = new ArrayList<>();
                        listBH.add(0, bh);
                        MediaCustom.danhSachPhats = listBH;
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Log.e("loi", res.getErrMessage());
                    }
                }


            }

            @Override
            public void onFailure(Call<GetListBaiHat> call, Throwable t) {
                Log.e("loi", "loi get list random bai hat");
                Log.e("loi", t.getMessage());
            }
        });


    }

    public class VHolder extends RecyclerView.ViewHolder {
        TextView stt, tenBaiHat, tenCasi;
        LinearLayout linearLayout;

        ImageView imgView, btnMore, iconDownLoad;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.txtStt);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
            btnMore = itemView.findViewById(R.id.btnMore);
            iconDownLoad = itemView.findViewById(R.id.iconDownLoad);
        }
    }
}
