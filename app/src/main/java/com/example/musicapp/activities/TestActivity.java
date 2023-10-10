package com.example.musicapp.activities;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.adapters.TestAdapter;

import java.util.ArrayList;

public class TestActivity extends AppCompatActivity {

    Button btnTest;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    TestAdapter adapter;

    EditText edText;

    public static int positon = 0;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_test);
        btnTest = findViewById(R.id.btnTest);
        recyclerView = findViewById(R.id.recycleView);
        edText = findViewById(R.id.edText);
        manager = new LinearLayoutManager(this);

        ArrayList<String> listData = new ArrayList<>();
        int count = 20;
        for (int i = 0; i < count; i++) {
            listData.add(String.valueOf(i));
        }

        adapter = new TestAdapter(listData, this);
        recyclerView.setAdapter(adapter);
        recyclerView.setLayoutManager(manager);


        btnTest.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                int dong = Integer.parseInt(edText.getText().toString());
                dong = (dong + 1) % count;
                edText.setText(String.valueOf(dong));

//                recyclerView.scrollToPosition(dong + 4);

                RecyclerView.ViewHolder viewHolder = recyclerView.findViewHolderForLayoutPosition(dong);
                if (viewHolder != null && viewHolder.isRecyclable()) {
                    // Phần tử đó đã được cuộn đến
                    Log.e("Test", "True");
                    recyclerView.smoothScrollToPosition(dong + 3);
                } else {
                    // Phần tử đó chưa được cuộn đến
                    Log.e("Test", "False");
                    recyclerView.smoothScrollToPosition(dong);
                }

                positon = dong;

                adapter.notifyItemChanged(dong);
                adapter.notifyItemChanged(dong - 1);

            }
        });


    }

}