package com.example.musicapp.adapters;

import android.content.Context;
import android.graphics.Color;
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
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.activities.TestActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.BsBaiHat;
import com.example.musicapp.fragments.ChiTietThuVienFragment;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class TestAdapter extends RecyclerView.Adapter<TestAdapter.VHolder> {


    ArrayList<String> data;
    Context context;

    public TestAdapter(ArrayList<String> data, Context context) {
        this.data = data;
        this.context = context;
    }

    @NonNull
    @Override
    public VHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        LayoutInflater inflater = LayoutInflater.from(context);
        View view = inflater.inflate(R.layout.item_test_adapter, parent, false);
        return new VHolder(view);
    }

    @Override
    public int getItemCount() {
        return data.size();
    }

    @Override
    public void onBindViewHolder(@NonNull VHolder holder, int position) {
        setUI(holder, position);
        holder.tenDS.setText(data.get(holder.getAdapterPosition()));
        holder.tenDS.append(" dong chu cua lơi bai hat");


        if (TestActivity.positon == position) {
            holder.tenDS.setTextColor(Color.YELLOW);
        }


        setEvent(holder);
    }

    private void setEvent(VHolder holder) {

    }

    private void setUI(VHolder holder, int position) {

    }

    public class VHolder extends RecyclerView.ViewHolder {

        TextView tenDS;

        public VHolder(@NonNull View itemView) {
            super(itemView);
            tenDS = itemView.findViewById(R.id.tenDS);


        }
    }
}
