package com.example.musicapp.adapters;

import android.content.Context;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.BinhLuanFragment;
import com.example.musicapp.modal.anhxajson.CommentBaiHat;
import com.example.musicapp.modal.anhxajson.CommentBaiHatCon;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.modal.body.BodyToggleLikeComment;
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

    private void setEvent(@NonNull VHolder holder) {
        holder.btnReply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String idCommentCha = data.get(holder.getAdapterPosition()).getId();
                String nameUserReply = data.get(holder.getAdapterPosition()).getUser().getFirstName() +
                        " " + data.get(holder.getAdapterPosition()).getUser().getLastName();

                BinhLuanFragment.isReply = true;
                BinhLuanFragment.layoutReply.setVisibility(View.VISIBLE);
                BinhLuanFragment.nameReply.setText(nameUserReply);
                BinhLuanFragment.idCommentCha = idCommentCha;

                BinhLuanFragment.valueCmt.requestFocus();
                InputMethodManager inputMethodManager = (InputMethodManager) context.getSystemService(Context.INPUT_METHOD_SERVICE);
                inputMethodManager.showSoftInput(BinhLuanFragment.valueCmt, 0);
            }
        });

        holder.layoutMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                holder.layoutMore.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.VISIBLE);
                BinhLuanFragment.isOpenMoreCmt = true;
            }
        });

        holder.btnToggleLike.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String header = Common.getHeader();
                String idComment = data.get(holder.getAdapterPosition()).getId();
                String type = "parent";
                String idBaiHat = MediaCustom.danhSachPhats.get(MediaCustom.position).getId();
                BodyToggleLikeComment body = new BodyToggleLikeComment(idComment, type, idBaiHat);

                ApiServiceV1.apiServiceV1.toggleLikeComment(body, header).enqueue(new Callback<ResponseDefault>() {
                    @Override
                    public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                        ResponseDefault res = response.body();
                        if (res != null) {
                            if (res.getErrCode() == 0) {
                                if (res.getErrMessage().equals("yes")) {
                                    holder.slLike.setVisibility(View.VISIBLE);
                                    double coundLike = data.get(holder.getAdapterPosition()).getCountLike();
                                    coundLike++;
                                    data.get(holder.getAdapterPosition()).setCountLike(coundLike);
                                    holder.slLike.setText(String.valueOf((int) coundLike));

                                    holder.btnToggleLike.setImageResource(R.drawable.heart_contained);

                                } else {
                                    double coundLike = data.get(holder.getAdapterPosition()).getCountLike();
                                    coundLike--;
                                    data.get(holder.getAdapterPosition()).setCountLike(coundLike);
                                    holder.slLike.setText(String.valueOf((int) coundLike));

                                    if (coundLike == 0) {
                                        holder.slLike.setVisibility(View.GONE);
                                    }
                                    holder.btnToggleLike.setImageResource(R.drawable.heart_border);

                                }

                            } else {
                                if (res.getStatus() == 401) {
                                    System.exit(0);
                                }
                                Log.e("Loi", res.getErrMessage());
                            }
                        }
                    }

                    @Override
                    public void onFailure(Call<ResponseDefault> call, Throwable t) {
                        Log.e("Loi", "Loi toggle like comment");
                    }
                });

            }
        });
    }

    private void setUI(@NonNull VHolder holder) {
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
            holder.slLike.setText(String.valueOf((int) cmt.getCountLike()));
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

        if (BinhLuanFragment.listIdLike != null && BinhLuanFragment.listIdLike.contains(cmt.getId())) {
            holder.btnToggleLike.setImageResource(R.drawable.heart_contained);
        } else {
        }


        //set adapter
        ArrayList<CommentBaiHatCon> CmtCon = new ArrayList<>();
        if (cmt.getCommentBHCons() != null) {
            CmtCon = cmt.getCommentBHCons();
        }

        holder.adapter = new BinhLuanConAdapter(CmtCon, holder.recyclerView.getContext());
        holder.recyclerView.setAdapter(holder.adapter);
        holder.recyclerView.setLayoutManager(holder.manager);

        holder.txtSlCmt.setText("Xem phản hồi (" + String.valueOf(CmtCon.size()) + ")");

        if (CmtCon.size() == 0) {
            holder.layoutMore.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.GONE);
        } else if (CmtCon.size() == 1) {
            holder.layoutMore.setVisibility(View.GONE);
            holder.recyclerView.setVisibility(View.VISIBLE);
        } else if (CmtCon.size() > 1) {
            if (!BinhLuanFragment.isOpenMoreCmt) {
                holder.layoutMore.setVisibility(View.VISIBLE);
                holder.recyclerView.setVisibility(View.GONE);
            } else {
                holder.layoutMore.setVisibility(View.GONE);
                holder.recyclerView.setVisibility(View.VISIBLE);
            }

        }
    }


    public class VHolder extends RecyclerView.ViewHolder {

        RecyclerView recyclerView;
        LinearLayoutManager manager;
        BinhLuanConAdapter adapter;

        ImageView anhUser, btnToggleLike;

        TextView tenUser, noiDung, slLike, tgComment, btnReply, txtSlCmt;

        LinearLayout layoutMore;

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
            layoutMore = itemView.findViewById(R.id.layoutMore);
            txtSlCmt = itemView.findViewById(R.id.txtSlCmt);
            btnToggleLike = itemView.findViewById(R.id.btnToggleLike);

        }
    }
}
