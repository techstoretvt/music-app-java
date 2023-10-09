package com.example.musicapp.adapters;

import android.app.Activity;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.Bs_XemNS;
import com.example.musicapp.fragments.CT_ThuVien_NoiBatFragment;
import com.example.musicapp.fragments.ChiTietCaSiFragment;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.fragments.YeuThichFragment;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.example.musicapp.utils.Common;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class XemCaSiAdapter extends RecyclerView.Adapter<XemCaSiAdapter.VHolder> {

    ArrayList<Casi> data;
    Context context;
    Activity activity;

    public XemCaSiAdapter(ArrayList<Casi> data, Context context, Activity activity) {
        this.data = data;
        this.context = context;
        this.activity = activity;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_rv_xem_ns, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        setUI(holder, position);

        setEvent(holder);
    }

    private void setEvent(VHolder holder) {
        holder.wrap_casi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                ChiTietCaSiFragment.idCaSi = data.get(holder.getAdapterPosition()).getId();
                BaiHatAdapter.mdXemNS.dismiss();

                //set type back
                if (ChiTietThuVienFragment.isChiTietDS && !ChiTietNhacActivity.isChiTietNhac) {
                    ChiTietCaSiFragment.typeBack = 1;
                    ChiTietCaSiFragment.idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;

                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else if (ChiTietNhacActivity.isChiTietNhac) {
                    activity.finish();
                    Handler handler = new Handler();
                    handler.postDelayed(new Runnable() {
                        @Override
                        public void run() {
                            Common.replace_fragment(new ChiTietCaSiFragment());
                        }
                    }, 500);


                } else if (YeuThichFragment.isYeuThich) {
                    ChiTietCaSiFragment.typeBack = 4;
                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else if (CT_ThuVien_NoiBatFragment.isChiTietDSNoiBat) {
                    ChiTietCaSiFragment.typeBack = 5;
                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();

                } else {
                    ChiTietCaSiFragment.typeBack = 0;
                    Common.replace_fragment(new ChiTietCaSiFragment());
                    BaiHatAdapter.md.dismiss();
                }
            }
        });

    }

    private void setUI(VHolder holder, int position) {
        holder.tenCs.setText(data.get(holder.getAdapterPosition()).getTenCaSi());

        String urlAnh = data.get(holder.getAdapterPosition()).getAnh();
        Glide.with(context).load(urlAnh).into(holder.anhCaSi);
    }

    public class VHolder extends RecyclerView.ViewHolder {
        ImageView anhCaSi;
        TextView tenCs, slQuanTam;

        LinearLayout wrap_casi;


        public VHolder(@NonNull View itemView) {
            super(itemView);
            anhCaSi = itemView.findViewById(R.id.anhCaSi);
            tenCs = itemView.findViewById(R.id.tenCs);
            slQuanTam = itemView.findViewById(R.id.slQuanTam);
            wrap_casi = itemView.findViewById(R.id.wrap_casi);


        }
    }
}
