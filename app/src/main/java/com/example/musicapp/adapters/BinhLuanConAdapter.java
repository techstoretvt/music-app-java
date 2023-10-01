package com.example.musicapp.adapters;

import android.content.Context;
import android.graphics.Typeface;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.style.StyleSpan;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.modal.anhxajson.CommentBaiHat;
import com.example.musicapp.modal.anhxajson.CommentBaiHatCon;
import com.example.musicapp.modal.anhxajson.TaiKhoan;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;

public class BinhLuanConAdapter extends RecyclerView.Adapter<BinhLuanConAdapter.VHolder> {

    ArrayList<CommentBaiHatCon> data;
    Context context;

    public BinhLuanConAdapter(ArrayList<CommentBaiHatCon> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_binh_luan_con, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        CommentBaiHatCon cmt = data.get(holder.getAdapterPosition());
        TaiKhoan user = cmt.getUser();

        String avatar = "https://res.cloudinary.com/dultkpqjp/image/upload/v1683860764/avatar_user/no-user-image_axhl6d.png";
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
        Glide.with(holder.anhUser.getContext()).load(avatar)
                .into(holder.anhUser);
        holder.tenUser.setText(user.getFirstName() + " " + user.getLastName());

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

        String strNoiDung = "(" + cmt.getNameUserReply() + "): " + cmt.getNoiDung();


        holder.noiDung.setText(strNoiDung);

        if (cmt.getCountLike() == 0) {
            holder.slLike.setVisibility(View.GONE);
        } else {
            holder.slLike.setText(String.valueOf(cmt.getCountLike()));
        }

    }


    public class VHolder extends RecyclerView.ViewHolder {

        ImageView anhUser;
        TextView tenUser, tgComment, noiDung, slLike;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            anhUser = itemView.findViewById(R.id.anhUser);
            tenUser = itemView.findViewById(R.id.tenUser);
            tgComment = itemView.findViewById(R.id.tgComment);
            noiDung = itemView.findViewById(R.id.noiDung);
            slLike = itemView.findViewById(R.id.slLike);
        }
    }
}
