package com.example.musicapp.fragments;

import android.app.AlertDialog;
import android.content.DialogInterface;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.adapters.AdapterKhamPha;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.Casi;
import com.example.musicapp.modal.anhxajson.ChiTietDanhSachPhat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetDSPhatById;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyXoaDSPhat;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ChiTietThuVienFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ChiTietThuVienFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    RelativeLayout layoutBtn;
    RecyclerView recyclerView;
    LinearLayoutManager manager;
    AdapterKhamPha adapter;

    ArrayList<BaiHat> list;

    TextView chuaCoBaihat, tenDS, slBaiHat;

    ImageView btnBack, btnMore, img1anh, anh1, anh2, anh3, anh4;
    LinearLayout layout4anh;

    public ChiTietThuVienFragment() {
        // Required empty public constructor
    }

    public static ChiTietThuVienFragment newInstance(String param1, String param2) {
        ChiTietThuVienFragment fragment = new ChiTietThuVienFragment();
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
        View view = inflater.inflate(R.layout.fragment_chi_tiet_thu_vien, container, false);

        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
//        manager = new LinearLayoutManager(getActivity(), LinearLayoutManager.VERTICAL, false);
        manager = new GridLayoutManager(getActivity(), 1);
        layoutBtn = view.findViewById(R.id.layoutBtn);
        chuaCoBaihat = view.findViewById(R.id.chuaCoBaihat);
        btnBack = view.findViewById(R.id.btnBack);
        btnMore = view.findViewById(R.id.btnMore);
        tenDS = view.findViewById(R.id.tenDS);
        slBaiHat = view.findViewById(R.id.slBaiHat);
        img1anh = view.findViewById(R.id.img1anh);
        layout4anh = view.findViewById(R.id.layout4anh);
        anh1 = view.findViewById(R.id.anh1);
        anh2 = view.findViewById(R.id.anh2);
        anh3 = view.findViewById(R.id.anh3);
        anh4 = view.findViewById(R.id.anh4);


        list = new ArrayList<>();

        layDanhSachBaiHat();


        //set event
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.fragmentManager2.beginTransaction()
                        .replace(R.id.frame_layout, new ThuVienFragment())
                        .commit();
            }
        });

        btnMore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                btnMore.showContextMenu(0, 50);
            }
        });

        btnMore.setOnCreateContextMenuListener(new View.OnCreateContextMenuListener() {
            @Override
            public void onCreateContextMenu(ContextMenu contextMenu, View view, ContextMenu.ContextMenuInfo contextMenuInfo) {
                contextMenu.add(0, 1, 0, "Xóa danh sách");

                contextMenu.findItem(1).setOnMenuItemClickListener(new MenuItem.OnMenuItemClickListener() {
                    @Override
                    public boolean onMenuItemClick(@NonNull MenuItem menuItem) {
                        int itemId = menuItem.getItemId();
                        Log.e("vao", String.valueOf(itemId));

                        AlertDialog.Builder builder = new AlertDialog.Builder(getContext());
                        builder.setTitle("Thông báo");
                        builder.setMessage("Bạn có chắc chắn muốn xóa danh sách.");
                        builder.setPositiveButton("OK", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi người dùng nhấn vào nút OK


                                String idDanhSachPhat = MainActivity.idDanhSachPhat;
                                String header = "bearer " + MainActivity.accessToken;
                                BodyXoaDSPhat body = new BodyXoaDSPhat(idDanhSachPhat);

                                ApiServiceV1.apiServiceV1.xoaDanhSachPhatById(idDanhSachPhat, header).enqueue(new Callback<ResponseDefault>() {
                                    @Override
                                    public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                                        ResponseDefault res = response.body();
                                        if (res != null) {
                                            if (res.getErrCode() == 0) {
                                                for (DanhSachPhat i : ThuVienFragment.danhSachPhats) {
                                                    if (i.getId() == idDanhSachPhat) {
                                                        ThuVienFragment.danhSachPhats.remove(i);
                                                    }
                                                }
                                                ThuVienFragment.adapter.notifyDataSetChanged();
                                                MainActivity.fragmentManager2.beginTransaction()
                                                        .replace(R.id.frame_layout, new ThuVienFragment())
                                                        .commit();


                                            } else {
                                                if (res.getStatus() == 401) {
                                                    System.exit(0);
                                                }
                                                Log.e("Xoa ds that bai", res.getErrMessage());
                                            }
                                        }
                                    }

                                    @Override
                                    public void onFailure(Call<ResponseDefault> call, Throwable t) {
                                        Log.e("Xoa ds that bai", "sfdsdf");
                                    }
                                });
                            }
                        });
                        builder.setNegativeButton("Hủy", new DialogInterface.OnClickListener() {
                            @Override
                            public void onClick(DialogInterface dialog, int which) {
                                // Xử lý khi người dùng nhấn vào nút Hủy
                            }
                        });
                        builder.show();

                        return false;
                    }
                });
            }

        });

        return view;
    }

    private void layDanhSachBaiHat() {
        String idDanhSachPhat = MainActivity.idDanhSachPhat;
        String header = "bearer " + MainActivity.accessToken;

        ApiServiceV1.apiServiceV1.getDanhSachPhatById(idDanhSachPhat, header).enqueue(new Callback<GetDSPhatById>() {
            @Override
            public void onResponse(Call<GetDSPhatById> call, Response<GetDSPhatById> response) {
                GetDSPhatById res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {

                        if (res.getData().getChiTietDanhSachPhats().size() == 0) {

                            chuaCoBaihat.setVisibility(View.VISIBLE);
                            return;
                        }
                        layoutBtn.setVisibility(View.VISIBLE);
                        int slBh = res.getData().getChiTietDanhSachPhats() != null ?
                                res.getData().getChiTietDanhSachPhats().size() : 0;
                        tenDS.setText(res.getData().getTenDanhSach());
                        slBaiHat.setText(String.valueOf(slBh) + " bài hát");

                        if (slBh > 0 && slBh < 4) {
                            Glide.with(getContext())
                                    .load(res.getData().
                                            getChiTietDanhSachPhats().get(0).getBaihat().getAnhBia())
                                    .into(img1anh);
                        } else if (slBh >= 4) {
                            layout4anh.setVisibility(View.VISIBLE);
                            img1anh.setVisibility(View.GONE);
                            Glide.with(getContext())
                                    .load(res.getData().
                                            getChiTietDanhSachPhats().get(0).getBaihat().getAnhBia())
                                    .into(anh1);
                            Glide.with(getContext())
                                    .load(res.getData().
                                            getChiTietDanhSachPhats().get(1).getBaihat().getAnhBia())
                                    .into(anh2);
                            Glide.with(getContext())
                                    .load(res.getData().
                                            getChiTietDanhSachPhats().get(2).getBaihat().getAnhBia())
                                    .into(anh3);
                            Glide.with(getContext())
                                    .load(res.getData().
                                            getChiTietDanhSachPhats().get(3).getBaihat().getAnhBia())
                                    .into(anh4);
                        }

                        long tgDanhSach = 0;


                        ArrayList<BaiHat> listbh = new ArrayList<>();

                        for (ChiTietDanhSachPhat i : res.getData().getChiTietDanhSachPhats()) {
                            listbh.add(i.getBaihat());
                            tgDanhSach += (long) i.getBaihat().getThoiGian();
                        }

                        Log.e("thoi gian", String.valueOf(tgDanhSach));

                        long seconds = tgDanhSach / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        minutes %= 60;
                        seconds %= 60;

                        slBaiHat.append(" - " + hours + " giờ " + minutes + " phút " + seconds + " giây");

                        list = listbh;

                        adapter = new AdapterKhamPha(list, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = list.size() * 84;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recyclerView.setLayoutParams(layoutParams);

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
            public void onFailure(Call<GetDSPhatById> call, Throwable t) {
                Toast.makeText(getContext(), "Get danh sach fail", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

}