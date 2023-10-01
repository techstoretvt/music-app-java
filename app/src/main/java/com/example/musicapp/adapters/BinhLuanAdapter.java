package com.example.musicapp.adapters;

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
import androidx.recyclerview.widget.LinearLayoutManager;
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
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BaiHatFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.ThongTinBaiHatFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.CommentBaiHat;
import com.example.musicapp.modal.anhxajson.CommentBaiHatCon;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class BinhLuanAdapter extends RecyclerView.Adapter<BinhLuanAdapter.VHolder> {

    ArrayList<CommentBaiHat> data;
    Context context;

    public BinhLuanAdapter(ArrayList<CommentBaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_binh_luan, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {

        setUI(holder);

        setEvent(holder);


    }

    private void setEvent(VHolder holder) {
        holder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idCommentCha = data.get(holder.getAdapterPosition()).getId();
                String nameUserReply = data.get(holder.getAdapterPosition()).getUser().getFirstName() +
                        " " + data.get(holder.getAdapterPosition()).getUser().getLastName();

                BinhLuanFragment.isReply = true;
                BinhLuanFragment.layoutReply.setVisibility(View.VISIBLE);
                BinhLuanFragment.nameReply.setText(nameUserReply);

                BinhLuanFragment.valueCmt.callOnClick();
                BinhLuanFragment.idCommentCha = idCommentCha;
            }
        });
    }

    private void setUI(VHolder holder) {
        String avatar = "https://res.cloudinary.com/dultkpqjp/image/upload/v1683860764/avatar_user/no-user-image_axhl6d.png";
        TaiKhoan user = data.get(holder.getAdapterPosition()).getUser();
        CommentBaiHat cmt = data.get(holder.getAdapterPosition());
        if (user.getTypeAccount().equals("web")) {
            if (user.getAvatarUpdate().isEmpty())
                avatar = "https://res.cloudinary.com/dultkpqjp/image/upload/v1683860764/avatar_user/no-user-image_axhl6d.png";
            else
                avatar = user.getAvatarUpdate();
        } else if (user.getTypeAccount().equals("google")) {
            if (!user.getAvatarGoogle().isEmpty())
                avatar = user.getAvatarGoogle();
            else if (!user.getAvatarUpdate().isEmpty())
                avatar = user.getAvatarUpdate();
            else
                avatar = "https://res.cloudinary.com/dultkpqjp/image/upload/v1683860764/avatar_user/no-user-image_axhl6d.png";
        }
        Glide.with(holder.recyclerView.getContext()).load(avatar)
                .into(holder.anhUser);
        holder.tenUser.setText(user.getFirstName() + " " + user.getLastName());
        holder.noiDung.setText(cmt.getNoiDung());

        if (cmt.getCountLike() == 0) {
            holder.slLike.setVisibility(View.GONE);
        } else {
            holder.slLike.setText(String.valueOf(cmt.getCountLike()));
        }

        //86.400.000
        double timestamp = System.currentTimeMillis();
        double tgCmt = cmt.getThoiGian();
        double kcTG = timestamp - tgCmt;

        int a = (int) kcTG / 86400000;
        Log.e("thoi gian", String.valueOf(a));

        if ((int) kcTG / 86400000 == 0) {

            if (kcTG < 3600000) {
                int phut = (int) (kcTG / 60000);
                holder.tgComment.setText(String.valueOf(phut) + " phút trước");
            } else {
                int gio = (int) (kcTG / 3600000);
                holder.tgComment.setText(String.valueOf(gio) + " giờ trước");
            }

        } else if (kcTG / 86400000 == 1) {
            holder.tgComment.setText("1 ngày trước");
        } else if (kcTG / 86400000 == 2) {
            holder.tgComment.setText("2 ngày trước");
        } else {
            LocalDateTime localDateTime = LocalDateTime.ofInstant(Instant.ofEpochMilli((long) tgCmt),
                    ZoneOffset.ofHours(-7));

            // Định dạng LocalDateTime thành chuỗi
            DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy");
            String dateString = formatter.format(localDateTime);

            holder.tgComment.setText(dateString);

        }


        //set adapter
        ArrayList<CommentBaiHatCon> CmtCon = new ArrayList<>();
        if (cmt.getCommentBHCons() != null) {
            CmtCon = cmt.getCommentBHCons();
        }

        holder.adapter = new BinhLuanConAdapter(CmtCon, holder.recyclerView.getContext());
        holder.recyclerView.setAdapter(holder.adapter);
        holder.recyclerView.setLayoutManager(holder.manager);
    }


    public class VHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        LinearLayoutManager manager;
        BinhLuanConAdapter adapter;

        ImageView anhUser;

        TextView tenUser, noiDung, slLike, tgComment, btnReply;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            recyclerView = itemView.findViewById(R.id.recycleView);
            manager = new LinearLayoutManager(itemView.getContext());
            anhUser = itemView.findViewById(R.id.anhUser);
            tenUser = itemView.findViewById(R.id.tenUser);
            noiDung = itemView.findViewById(R.id.noiDung);
            slLike = itemView.findViewById(R.id.slLike);
            tgComment = itemView.findViewById(R.id.tgComment);
            btnReply = itemView.findViewById(R.id.btnReply);

        }
    }
}
