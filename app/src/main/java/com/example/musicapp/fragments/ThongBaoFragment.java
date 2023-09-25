package com.example.musicapp.fragments;

import android.os.Bundle;

import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.ThongBaoAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.LayDsThongBao;
import com.example.musicapp.modal.anhxajson.ThongBao;
import com.example.musicapp.utils.Common;
import com.google.android.material.badge.BadgeDrawable;

import java.util.ArrayList;

import io.socket.emitter.Emitter;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ThongBaoFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ThongBaoFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    LinearLayoutManager manager;
    RecyclerView recyclerView;
    ThongBaoAdapter adapter;

    ArrayList<ThongBao> data = new ArrayList<>();

    public static int numberThongBao = 0;

    public ThongBaoFragment() {
        // Required empty public constructor
    }

    public static ThongBaoFragment newInstance(String param1, String param2) {
        ThongBaoFragment fragment = new ThongBaoFragment();
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
        View view = inflater.inflate(R.layout.fragment_thong_bao, container, false);
        manager = new LinearLayoutManager(getContext());
        recyclerView = view.findViewById(R.id.rvThongBao);
        numberThongBao = 0;

        layDanhSachThongBao();

        BadgeDrawable badge = MainActivity.bottomNavigationView.getOrCreateBadge(R.id.thongBao);
        badge.setVisible(false);
        badge.setNumber(numberThongBao);

//        btnTest.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View view) {
//                String dataStr = "{\"title\":\"Ca khúc mới có tên \\\"nhạc cười 4\\\"\",\"content\":\"\\\"nhạc cười 4\\\" - một bài hát mới của [tên nghệ sĩ\\/nhóm nhạc] đã chính thức ra mắt. Đây là một bài hát về tình yêu đơn phương, mang đến cho người nghe những cảm xúc lắng đọng và sâu sắc.\\n\\n                    Giai điệu của bài hát du dương, ngọt ngào, kết hợp với giọng hát đầy cảm xúc của [tên nghệ sĩ\\/nhóm nhạc], đã mang đến cho khán giả những cảm xúc lắng đọng.\\n                    \\n                    \\\"nhạc cười 4\\\" là một bài hát dễ nghe, dễ nhớ, và dễ đi vào lòng người. Bài hát chắc chắn sẽ là một bản hit trong thời gian tới.\",\"timeCreate\":1695607515696,\"urlImage\":\"https:\\/\\/source.unsplash.com\\/random?sig=11\"}";
//
//
//                Gson gson = new Gson();
//                ThongBao newTB = gson.fromJson(dataStr, ThongBao.class);
//
//                Log.e("title", newTB.getTitle());
////                Log.e("content", newTB.getContent());
////                Log.e("url image", newTB.getUrlImage());
//
//                data.add(0, newTB);
//                adapter.notifyDataSetChanged();
//            }
//        });

        MainActivity.webSocketClient.socket.on("new_thong_bao", new Emitter.Listener() {
            @Override
            public void call(final Object... args) {
                // Xử lý dữ liệu từ máy chủ ở đây
//                String dataStr = args[0].toString();
////                Log.e("data", dataStr);
//
//                Gson gson = new Gson();
//                ThongBao newTB = gson.fromJson(dataStr, ThongBao.class);
//
//                data.add(0, newTB);
//                adapter.notifyDataSetChanged();
                layDanhSachThongBao();

            }
        });

        return view;
    }

    private void layDanhSachThongBao() {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.layDanhSachTB(header).enqueue(new Callback<LayDsThongBao>() {
            @Override
            public void onResponse(Call<LayDsThongBao> call, Response<LayDsThongBao> response) {
                LayDsThongBao res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        data = res.getData();
                        adapter = new ThongBaoAdapter(data, getContext());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);


                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
            }

            @Override
            public void onFailure(Call<LayDsThongBao> call, Throwable t) {
                Toast.makeText(getContext(), "Loi lay ds thong bao", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }
}