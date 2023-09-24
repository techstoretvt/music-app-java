package com.example.musicapp.activities;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.example.musicapp.R;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    RecyclerView recyclerView;
    LinearLayoutManager manager;

    BaiHatAdapter adapter;

    ArrayList<BaiHat> data;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        recyclerView = findViewById(R.id.rvBaiHat);
        manager = new LinearLayoutManager(this);

        data = new ArrayList<>();
        for (int i = 0; i < 10; i++) {
            BaiHat bh = new BaiHat("1", "ten bh", "Loi bai hat",
                    "https://res.cloudinary.com/dultkpqjp/image/upload/v1694525102/music/oahgh5rnoupovbjkhmok.jpg",
                    "sfsdf", new Casi("id", "ten ca si", "mota",
                    "https://res.cloudinary.com/dultkpqjp/image/upload/v1694525102/music/oahgh5rnoupovbjkhmok.jpg"));

            data.add(bh);
        }

        adapter = new BaiHatAdapter(data, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);

        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Di chuyển lên và xuống
                int swipeFlags = 0; // Không cho phép vuốt
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                adapter.notifyItemMoved(fromPosition, toPosition);

                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {

            }
        });

        itemTouchHelper.attachToRecyclerView(recyclerView);

    }
}