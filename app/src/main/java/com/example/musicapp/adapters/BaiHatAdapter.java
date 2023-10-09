package com.example.musicapp.adapters;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.database.Cursor;
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
import com.example.musicapp.fragments.DaTaiFragment;
import com.example.musicapp.fragments.TimKiemFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;
import com.example.musicapp.fragments.Bs_XemNS;
import com.example.musicapp.fragments.fragment_mini_nhac.CurrentMiniNhacFragment;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;
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
    public static Bs_XemNS mdXemNS = new Bs_XemNS();

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

    @SuppressLint("RecyclerView")
    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {

        setUI(holder, position);

        setEvent(holder, position);


    }

    private void setEvent(VHolder holder, int position) {
        holder.linearLayout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {


                MainActivity.progess_phatNhac.setProgress(100);
                BaiHat currentBH = data.get(holder.getAdapterPosition());

                String strTenCaSi = "";
                for (int i = 0; i < currentBH.getBaiHat_caSis().size(); i++) {
                    if (i == 0)
                        strTenCaSi = currentBH.getBaiHat_caSis().
                                get(i).getCasi().getTenCaSi();
                    else
                        strTenCaSi += ", " + currentBH.getBaiHat_caSis().
                                get(i).getCasi().getTenCaSi();
                }

                if (ChiTietNhacActivity.isChiTietNhac) {

                    int indexBH = MediaCustom.danhSachPhats.indexOf(currentBH);
                    if (indexBH != -1) {
                        MediaCustom.position = indexBH;

                        String anhBia = currentBH.getAnhBia();
                        //update ui


                        ChiTietNhacActivity.btnPrev.setImageResource(R.drawable.baseline_skip_previous_white);

                        if (MainActivity.dungNhac != null) {
                            //current
                            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBia)
                                    .into(CurrentMiniNhacFragment.imgNhac);
                            CurrentMiniNhacFragment.tenBaiHat.setText(currentBH.getTenBaiHat());


                            CurrentMiniNhacFragment.tenCaSi.setText(strTenCaSi);

                            MainActivity.dungNhac.setImageResource(R.drawable.baseline_pause_24);

                            //next
                            int nextPosition = (indexBH + 1) % MediaCustom.danhSachPhats.size();
                            String anhBiaNext = MediaCustom.danhSachPhats.get(nextPosition)
                                    .getAnhBia();
                            Glide.with(MainActivity.layoutAudio.getContext()).load(anhBiaNext)
                                    .into(NextMiniNhacFragment.imgNhac);
                            NextMiniNhacFragment.tenBaiHat.setText(MediaCustom.danhSachPhats.get(nextPosition)
                                    .getTenBaiHat());
                            NextMiniNhacFragment.tenCaSi.setText(strTenCaSi);

                        }
                        if (BaiHatFragment.imgNhac != null) {
                            Glide.with(BaiHatFragment.imgNhac.getContext()).load(anhBia)
                                    .into(BaiHatFragment.imgNhac);
                            BaiHatFragment.txtTenBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                                    getTenBaiHat());
                            BaiHatFragment.txtTenCS.setText(strTenCaSi);
                            BaiHatFragment.txtLoiBH.setText(MediaCustom.danhSachPhats.get(MediaCustom.position).
                                    getLoiBaiHat());

                            ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_pause_white);

                            BaiHatFragment.checkLike();

                        }

                        BinhLuanFragment.getListComment(MediaCustom.danhSachPhats.get(MediaCustom.position)
                                .getId());
                    }

                } else if (ChiTietThuVienFragment.isChiTietDS) {
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
                } else if (DaTaiFragment.isFragmentDaTai) {
                    MediaCustom.position = holder.getAdapterPosition();
                    MediaCustom.danhSachPhats = data;
                    MediaCustom.typeDanhSachPhat = 2;
                    MediaCustom.tenLoai = "Đã tải";
                } else {
                    //kham pha
                    MediaCustom.position = 0;
                    MediaCustom.danhSachPhats = new ArrayList<>();
                    MediaCustom.danhSachPhats.add(data.get(holder.getAdapterPosition()));
                    MediaCustom.typeDanhSachPhat = 1;
                    MediaCustom.tenLoai = "Khám phá";
                    getListRandomBaiHat(data.get(holder.getAdapterPosition()));
                }

                //
                MainActivity.phatNhacMini(data.get(holder.getAdapterPosition()).getAnhBia(),
                        data.get(holder.getAdapterPosition()).getTenBaiHat(),
                        strTenCaSi);

                if (MediaCustom.phatNhac(data.get(holder.getAdapterPosition()).getLinkBaiHat())) {
                    if (ChiTietNhacActivity.isChiTietNhac) {
                        ThongTinBaiHatFragment.getData();

                        ChiTietNhacActivity.tgHienTai.setText(MediaCustom.getStrCurrentTime());
                        ChiTietNhacActivity.totalTime.setText(MediaCustom.strTotalTime);
                        ChiTietNhacActivity.sliderProgress.setValueTo(MediaCustom.totalTime);
                    }
                }

                if (MediaCustom.position == 0) {
                    MainActivity.btnPrev.setVisibility(View.GONE);
                } else {
                    MainActivity.btnPrev.setVisibility(View.VISIBLE);
                }

                MediaCustom.tangView(data.get(position).getId());

                if (!ChiTietNhacActivity.isChiTietNhac) {
                    Intent intent = new Intent(view.getContext(), ChiTietNhacActivity.class);
                    view.getContext().startActivity(intent);
                }

            }
        });

        if (!ChiTietNhacActivity.isChiTietNhac && !ChiTietThuVienFragment.isChiTietDS) {
            holder.linearLayout.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View view) {
                    holder.btnMore.callOnClick();
                    return true;
                }
            });
        }
        holder.btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //
                idBaiHat = data.get(holder.getAdapterPosition()).getId();
//                idCaSi = data.get(holder.getAdapterPosition()).g.getCasi().getId();
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

    private void setUI(VHolder holder, int position) {
        holder.stt.setText(String.valueOf(position + 1));
        holder.tenBaiHat.setText(data.get(position).getTenBaiHat());


        String strTenCaSi = "";
        for (int i = 0; i < data.get(position).getBaiHat_caSis().size(); i++) {
            if (i == 0)
                strTenCaSi = data.get(position).getBaiHat_caSis().
                        get(i).getCasi().getTenCaSi();
            else
                strTenCaSi += ", " + data.get(position).getBaiHat_caSis().
                        get(i).getCasi().getTenCaSi();
        }
        holder.tenCasi.setText(strTenCaSi);


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
        if (ChiTietNhacActivity.isChiTietNhac) {
            holder.btnMore.setVisibility(View.GONE);
            holder.ic_wrap.setVisibility(View.VISIBLE);
        }
    }

    public static void getListRandomBaiHat(@NonNull BaiHat bh) {
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
                        if (res.getData() != null) {
                            listBH = res.getData();

                            if (NextMiniNhacFragment.tenBaiHat != null) {
                                BaiHat bh0 = listBH.get(0);
                                NextMiniNhacFragment.tenBaiHat.setText(bh0.getTenBaiHat());


                                String strTenCaSi = "";
                                for (int i = 0; i < bh0.getBaiHat_caSis().size(); i++) {
                                    if (i == 0)
                                        strTenCaSi = bh0.getBaiHat_caSis().
                                                get(i).getCasi().getTenCaSi();
                                    else
                                        strTenCaSi += ", " + bh0.getBaiHat_caSis().
                                                get(i).getCasi().getTenCaSi();
                                }

                                NextMiniNhacFragment.tenCaSi.setText(strTenCaSi);


                                Glide.with(MainActivity.layoutAudio.getContext()).load(bh0.getAnhBia())
                                        .into(NextMiniNhacFragment.imgNhac);
                            }


                        } else listBH = new ArrayList<>();
                        listBH.add(0, bh);
                        MediaCustom.danhSachPhats = listBH;
                        MediaCustom.position = 0;

                        if (MainActivity.btnPrev != null) {
                            MainActivity.btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                        }
                        if (ChiTietNhacActivity.isChiTietNhac) {
                            ChiTietNhacActivity.btnPrev.setImageResource(R.drawable.baseline_skip_previous_disable);
                        }

                        if (ThongTinBaiHatFragment.tenBaiHat != null && ChiTietNhacActivity.isChiTietNhac) {
                            ThongTinBaiHatFragment.getData();
                        }


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

        ImageView imgView, btnMore, iconDownLoad, ic_wrap;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            stt = itemView.findViewById(R.id.txtStt);
            tenCasi = itemView.findViewById(R.id.tenCaSi);
            tenBaiHat = itemView.findViewById(R.id.tenBaiHat);
            linearLayout = itemView.findViewById(R.id.layoutBaiHat);
            imgView = itemView.findViewById(R.id.imgView);
            btnMore = itemView.findViewById(R.id.btnMore);
            iconDownLoad = itemView.findViewById(R.id.iconDownLoad);
            ic_wrap = itemView.findViewById(R.id.ic_wrap);
        }
    }
}
