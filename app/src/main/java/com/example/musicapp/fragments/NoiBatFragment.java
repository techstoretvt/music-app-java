package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.adapters.DsNoiBatAdapter;
import com.example.musicapp.modal.anhxajson.DS_Noi_Bat;

import java.util.ArrayList;
import java.util.Random;

public class NoiBatFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RecyclerView rcvCoTheBanMuonNghe, rcvNhacHotGayBao, rcvRemixHotNhat, rcvChill,
            rcvNgheSiThinhHanh, rcvNgheLaYeuDoi, rcvNhacBuonTamTrang, rcvTop100, rcvAlbumHot;
    LinearLayoutManager manager;
    DsNoiBatAdapter adapter;

    ImageView img1, img2, img3;

    int numberRandom = 0;

    public NoiBatFragment() {
        // Required empty public constructor
    }

    public static NoiBatFragment newInstance(String param1, String param2) {
        NoiBatFragment fragment = new NoiBatFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view = inflater.inflate(R.layout.fragment_noi_bat, container, false);
        anhXa(view);

        //add data nghe gan day
        setAdapter();

        Glide.with(getContext()).load("https://res.cloudinary.com/dultkpqjp/image/upload/v1696394604/All_Image/Untitled_video_-_Made_with_Clipchamp_va5gko.gif")
                .into(img1);


        return view;
    }

    private void setAdapter() {
        ArrayList<DS_Noi_Bat> data;
        Random random = new Random();

        //rcvCoTheBanMuonNghe
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Hoài Lâm, Thùy Chi, Đức Phúc"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Duongg, Thịnh Suy, Haisam"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Nguyenn, Reddy, Duongg"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Quân A.P, Châu Khải Phong, Dương Edward"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Noo Phước Thịnh, ERIK, Trung Quân"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvCoTheBanMuonNghe.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvCoTheBanMuonNghe.setLayoutManager(manager);

        //rcvNhacHotGayBao
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Nhạc hoa lời việt đang được chia sẻ nhiều nhất"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Những màn kết hợp chấn động V-Pop"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Những ca khúc V-Pop mới cập nhật mà bạn nên nghe"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Những ca khúc V-Pop gây bão"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvNhacHotGayBao.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvNhacHotGayBao.setLayoutManager(manager);

        //rcvRemixHotNhat
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Sơn tùng M-TP, Hòa Minzy, Pháo"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Tăng Duy Tân, Hoàng Thùy Linh, Bích Phương"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Mina Young, Masew, Xesi"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvRemixHotNhat.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvRemixHotNhat.setLayoutManager(manager);

        //rcvChill
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Đắm mình trong giai điệu Lofi kết hợp với những âm thanh thiên nhiên thơ mộng"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Thả mình vào những giai điệu Lofi Chill nghe là nghiện"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Nhạc hoa lời việt nhẹ nhàng"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvChill.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvChill.setLayoutManager(manager);

        //rcvNgheSiThinhHanh
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Cùng thưởng thức những bài hát hay nhất"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Tất cả hoặc không là gì cả"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Cập nhật nhanh"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvNgheSiThinhHanh.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvNgheSiThinhHanh.setLayoutManager(manager);

        //rcvNgheLaYeuDoi
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Vì chúng ta sinh ra là để đi cùng nhau"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Ở đây có nhạc để edit ảnh giật giật cực choáy"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Nhạc gì mà nghe xong dính cứng ngác"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvNgheLaYeuDoi.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvNgheLaYeuDoi.setLayoutManager(manager);

        //rcvNhacBuonTamTrang
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Khi đôi ta hết duyên thì có níu kéo cũng vậy thôi"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Ngàn câu hứa chẳng được gì, cuối cùng thì mình phải chia ly"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Nếu nổi nhớ là một bông hoa th sẽ là đóa hoa..."));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvNhacBuonTamTrang.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvNhacBuonTamTrang.setLayoutManager(manager);

        //rcvTop100
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Top 100 V-Pop Hay Nhất"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Top 100 Nhạc Rap Việt Nam Hay Nhất"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Top 100 Nhac EDM Việt Hay Nhất"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvTop100.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvTop100.setLayoutManager(manager);

        //rcvAlbumHot
        data = new ArrayList<>();
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Cớ Sao Vân Thương Một Người Như Em"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Không Quan Trọng"));
        data.add(new DS_Noi_Bat("https://source.unsplash.com/random?sig=" + getNumberRandom(),
                "Những Ngày Mưa"));

        adapter = new DsNoiBatAdapter(data, getContext());
        rcvAlbumHot.setAdapter(adapter);
        manager = new LinearLayoutManager(getContext(), RecyclerView.HORIZONTAL, false);
        rcvAlbumHot.setLayoutManager(manager);
    }

    private int getNumberRandom() {
        int result = (numberRandom + 1) % 30;
        numberRandom = result;
        return result;

    }

    private void anhXa(View view) {
        rcvCoTheBanMuonNghe = view.findViewById(R.id.rcvCoTheBanMuonNghe);
        rcvNhacHotGayBao = view.findViewById(R.id.rcvNhacHotGayBao);
        rcvRemixHotNhat = view.findViewById(R.id.rcvRemixHotNhat);
        rcvChill = view.findViewById(R.id.rcvChill);
        rcvNgheSiThinhHanh = view.findViewById(R.id.rcvNgheSiThinhHanh);
        rcvNgheLaYeuDoi = view.findViewById(R.id.rcvNgheLaYeuDoi);
        rcvNhacBuonTamTrang = view.findViewById(R.id.rcvNhacBuonTamTrang);
        rcvNgheLaYeuDoi = view.findViewById(R.id.rcvNgheLaYeuDoi);
        rcvTop100 = view.findViewById(R.id.rcvTop100);
        rcvAlbumHot = view.findViewById(R.id.rcvAlbumHot);
        img1 = view.findViewById(R.id.img1);
        img2 = view.findViewById(R.id.img2);
        img3 = view.findViewById(R.id.img3);

    }


}