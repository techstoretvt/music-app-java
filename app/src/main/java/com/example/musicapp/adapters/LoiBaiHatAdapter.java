package com.example.musicapp.adapters;

import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.musicapp.R;
import com.example.musicapp.activities.ChiTietNhacActivity;
import com.example.musicapp.activities.TestActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_chi_tiet_bh.LoiBaiHatFragment;
import com.example.musicapp.modal.anhxajson.GetTaiKhoan;
import com.example.musicapp.modal.anhxajson.LoiBaiHat;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.modal.body.BodySuaTGBaiHat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LoiBaiHatAdapter extends RecyclerView.Adapter<LoiBaiHatAdapter.VHolder> {


    ArrayList<LoiBaiHat> data;
    Context context;

    public static int currentPosition = -1;

    public LoiBaiHatAdapter(ArrayList<LoiBaiHat> data, Context context) {
        this.data = data;
        this.context = context;
    }

    TaiKhoan tkUser = null;

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_loi_bai_hat, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        setUI(holder, position);

        LoiBaiHat currentData = data.get(holder.getAdapterPosition());

        holder.loiBH.setText(currentData.getLoiBaiHat());


        if (currentPosition != -1 &&
                currentPosition == holder.getAdapterPosition() && currentPosition == position) {
            holder.loiBH.setTextColor(Color.YELLOW);
        } else {
            holder.loiBH.setTextColor(Color.WHITE);
        }


        setEvent(holder);
    }

    private void setEvent(VHolder holder) {

        holder.layoutLoiBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                LoiBaiHat currentData = data.get(holder.getAdapterPosition());

                double currentTime = currentData.getThoiGian();
                MediaCustom.setCurrentTimeDouble(currentTime);
                MediaCustom.play();
                holder.loiBH.setTextColor(Color.YELLOW);


                LoiBaiHatFragment.isScrolling = false;
                if (ChiTietNhacActivity.isChiTietNhac) {
                    if (ChiTietNhacActivity.btnPausePlay != null) {
                        ChiTietNhacActivity.btnPausePlay.setImageResource(R.drawable.baseline_pause_white);
                    }
                }

            }
        });

        holder.layoutLoiBH.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                double offsetTG = 0.2;
                //check admin
                if (tkUser == null) {
                    String header = Common.getHeader();
                    ApiServiceV1.apiServiceV1.getTaiKhoan(header).enqueue(new Callback<GetTaiKhoan>() {
                        @Override
                        public void onResponse(Call<GetTaiKhoan> call, Response<GetTaiKhoan> response) {
                            GetTaiKhoan res = response.body();
                            if (res != null) {
                                if (res.getErrCode() == 0) {
                                    tkUser = res.getData();

                                    if (tkUser == null || tkUser.getIdTypeUser().equals('3')) {

                                    } else {
                                        AlertDialog.Builder builder = new AlertDialog.Builder(context);
                                        builder.setTitle("Chỉnh thời gian");
                                        builder.setMessage("Tăng hoặc giảm thời gian đi " + offsetTG + "s");

                                        builder.setPositiveButton("Tăng", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                handleSuaTGBaiHat(data.get(holder.getAdapterPosition()).getId(),
                                                        data.get(holder.getAdapterPosition()).getThoiGian() + offsetTG,
                                                        holder.getAdapterPosition()
                                                );
                                            }
                                        });

                                        builder.setNegativeButton("Giảm", new DialogInterface.OnClickListener() {
                                            @Override
                                            public void onClick(DialogInterface dialogInterface, int i) {
                                                handleSuaTGBaiHat(data.get(holder.getAdapterPosition()).getId(),
                                                        data.get(holder.getAdapterPosition()).getThoiGian() - offsetTG,
                                                        holder.getAdapterPosition()
                                                );
                                            }
                                        });

                                        builder.show();
                                    }

                                } else {

                                }
                            }
                        }

                        @Override
                        public void onFailure(Call<GetTaiKhoan> call, Throwable t) {
                            Toast.makeText(context, "Loi lay tai khoan", Toast.LENGTH_SHORT)
                                    .show();
                        }
                    });
                } else if (!tkUser.getIdTypeUser().equals('3')) {
                    AlertDialog.Builder builder = new AlertDialog.Builder(context);
                    builder.setTitle("Chỉnh thời gian");
                    builder.setMessage("Tăng hoặc giảm thời gian đi " + offsetTG + "s");

                    builder.setPositiveButton("Tăng", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            handleSuaTGBaiHat(data.get(holder.getAdapterPosition()).getId(),
                                    data.get(holder.getAdapterPosition()).getThoiGian() + offsetTG,
                                    holder.getAdapterPosition()
                            );
                        }
                    });

                    builder.setNegativeButton("Giảm", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialogInterface, int i) {
                            handleSuaTGBaiHat(data.get(holder.getAdapterPosition()).getId(),
                                    data.get(holder.getAdapterPosition()).getThoiGian() - offsetTG,
                                    holder.getAdapterPosition()
                            );
                        }
                    });

                    builder.show();
                }


                return true;
            }
        });
    }

    private void handleSuaTGBaiHat(String idBaiHat, double thoiGian, int index) {
        String header = Common.getHeader();
        BodySuaTGBaiHat body = new BodySuaTGBaiHat(idBaiHat, thoiGian);

        ApiServiceV1.apiServiceV1.suaThoiGianLoiBHAdmin(body, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        Toast.makeText(context, "Success", Toast.LENGTH_SHORT)
                                .show();

//                        data.get(index).setThoiGian(thoiGian);
                        LoiBaiHatFragment.listLoiBH.get(index).setThoiGian(thoiGian);
                        LoiBaiHatFragment.adapter.notifyItemChanged(index);

                    } else {
                        Toast.makeText(context, res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                } else {
                    Toast.makeText(context, "Fail", Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Toast.makeText(context, "Loi lay tai khoan", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void setUI(VHolder holder, int position) {

    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView loiBH;
        LinearLayout layoutLoiBH;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            loiBH = itemView.findViewById(R.id.loiBH);
            layoutLoiBH = itemView.findViewById(R.id.layoutLoiBH);


        }
    }
}
