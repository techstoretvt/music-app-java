package com.example.musicapp.activities;


import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;

import android.os.Bundle;
import android.widget.TextView;

import com.example.musicapp.R;
import com.example.musicapp.databinding.ActivityMainBinding;
import com.example.musicapp.fragments.CaNhanFragment;
import com.example.musicapp.fragments.KhamPhaFragment;
import com.example.musicapp.fragments.ThongBaoFragment;
import com.example.musicapp.fragments.ThuVienFragment;

import org.w3c.dom.Text;

public class MainActivity extends AppCompatActivity {

    ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = ActivityMainBinding.inflate(getLayoutInflater());
        setContentView(binding.getRoot());
        replace_fragment(new KhamPhaFragment());

//        TextView tx = findViewById(R.id.textView11);
//        tx.setText("hello 11");

        //binding bottom tabs
        binding.bottomNavigationView.setOnItemSelectedListener(item -> {
            if (item.getItemId() == R.id.khamPha) {
                replace_fragment(new KhamPhaFragment());
            } else if (item.getItemId() == R.id.thuVien) {
                replace_fragment(new ThuVienFragment());
            } else if (item.getItemId() == R.id.thongBao) {
                replace_fragment(new ThongBaoFragment());
            } else if (item.getItemId() == R.id.caNhan) {
                replace_fragment(new CaNhanFragment());
            }

            return true;
        });

    }

    private void replace_fragment(Fragment fragment) {
        FragmentManager fragmentManager = getSupportFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout, fragment);
        fragmentTransaction.commit();
    }


//    private void callApi() {
//        ApiService.apiService.productConvert().enqueue(new Callback<Currency>() {
//            @Override
//            public void onResponse(Call<Currency> call, Response<Currency> response) {
//                Toast.makeText(MainActivity.this, "Call api success", Toast.LENGTH_SHORT).show();
//
//                Currency currency = response.body();
//                if (currency != null) {
//                    idProduct.setText(String.valueOf(currency.getData().get(0).getId()));
//                }
//            }
//
//            @Override
//            public void onFailure(Call<Currency> call, Throwable t) {
//                Toast.makeText(MainActivity.this, "Call api error", Toast.LENGTH_SHORT).show();
//            }
//        });
//    }


}


