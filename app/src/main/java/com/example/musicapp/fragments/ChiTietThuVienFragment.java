package com.example.musicapp.fragments;

import android.app.AlertDialog;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.GradientDrawable;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.ItemTouchHelper;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.util.Log;
import android.util.TypedValue;
import android.view.ContextMenu;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.activities.ThemBHVaoDSActivity;
import com.example.musicapp.adapters.BaiHatAdapter;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.fragments.fragment_mini_nhac.NextMiniNhacFragment;
import com.example.musicapp.modal.anhxajson.BaiHat;
import com.example.musicapp.modal.anhxajson.ChiTietDanhSachPhat;
import com.example.musicapp.modal.anhxajson.DanhSachPhat;
import com.example.musicapp.modal.anhxajson.GetDSPhatById;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyXoaDSPhat;
import com.example.musicapp.utils.Common;
import com.example.musicapp.utils.MediaCustom;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;


public class ChiTietThuVienFragment extends Fragment {


    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    private String mParam1;
    private String mParam2;

    RelativeLayout layoutBtn;
    RecyclerView recyclerView;
    LinearLayoutManager manager;


    TextView chuaCoBaihat, tenDS;
    public static TextView slBaiHat;

    ImageView btnBack, btnMore, img1anh, anh1, anh2, anh3, anh4, btnEdit;

    Button btnSubmitEdit, btnPlayRanDom;
    LinearLayout layout4anh, btnThemBH;

    public static Boolean isChiTietDS = false;
    public static String idDanhSachPhat = null;
    public static String tenDanhSach = null;

    public static ArrayList<BaiHat> danhBaiHats = null;
    public static BaiHatAdapter adapter;


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

        anhXa(view);

        layDanhSachBaiHat();

        setItemTouchHelper();

        //set mau gradient
        GradientDrawable gradientDrawable = new GradientDrawable(
                GradientDrawable.Orientation.TOP_BOTTOM,
                new int[]{Color.parseColor("#332733"), Color.BLACK}
        );
        recyclerView.setBackground(gradientDrawable);

        //set event
        initEvent();


        return view;
    }

    private void anhXa(View view) {
        recyclerView = (RecyclerView) view.findViewById(R.id.recycleView);
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
        btnEdit = view.findViewById(R.id.btnEdit);
        btnSubmitEdit = view.findViewById(R.id.btnSubmitEdit);
        btnThemBH = view.findViewById(R.id.btnThemBH);
        btnPlayRanDom = view.findViewById(R.id.btnPlayRandom);

        isChiTietDS = true;


        danhBaiHats = new ArrayList<>();
    }

    private void initEvent() {
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


                                String idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;
                                String header = "bearer " + MainActivity.accessToken;
                                BodyXoaDSPhat body = new BodyXoaDSPhat(idDanhSachPhat);

                                ApiServiceV1.apiServiceV1.xoaDanhSachPhatById(idDanhSachPhat, header).enqueue(new Callback<ResponseDefault>() {
                                    @Override
                                    public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                                        ResponseDefault res = response.body();
                                        if (res != null) {
                                            if (res.getErrCode() == 0) {
                                                ArrayList<DanhSachPhat> arr = ThuVienFragment.danhSachPhats;
                                                for (DanhSachPhat i : arr) {
                                                    if (i.getId().equals(idDanhSachPhat)) {
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

        btnEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                tenDS.setEnabled(true);
                tenDS.setBackground(new ColorDrawable(Color.WHITE));
                tenDS.setTextColor(Color.BLACK);
                tenDS.setPadding(20, 10, 20, 10);
                btnEdit.setVisibility(View.GONE);
                btnSubmitEdit.setVisibility(View.VISIBLE);
            }
        });

        btnSubmitEdit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String newName = String.valueOf(tenDS.getText());
                Boolean check = true;
                for (DanhSachPhat ds : ThuVienFragment.danhSachPhats) {
                    if (ds.getId() == idDanhSachPhat && ds.getTenDanhSach().equals(newName))
                        check = false;
                }
                if (check)
                    submitEditTenDanhSach();


            }
        });

        tenDS.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView textView, int i, KeyEvent keyEvent) {
                String newName = String.valueOf(tenDS.getText());
                Boolean check = true;
                for (DanhSachPhat ds : ThuVienFragment.danhSachPhats) {
                    if (ds.getId() == idDanhSachPhat && ds.getTenDanhSach().equals(newName))
                        check = false;
                }
                if (check)
                    submitEditTenDanhSach();
                return false;
            }
        });

        btnThemBH.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(getContext(), ThemBHVaoDSActivity.class);
                ThemBHVaoDSActivity.idDanhSach = idDanhSachPhat;
                startActivity(intent);
            }
        });

        btnPlayRanDom.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {

                MediaCustom.position = 0;
                MediaCustom.danhSachPhats = danhBaiHats;
                MediaCustom.typeDanhSachPhat = 2;
                MediaCustom.tenLoai = tenDanhSach;

                MainActivity.phatNhacMini(danhBaiHats.get(0).getAnhBia(),
                        danhBaiHats.get(0).getTenBaiHat(),
                        danhBaiHats.get(0).getCasi().getTenCaSi());

                MediaCustom.phatNhac(danhBaiHats.get(0).getLinkBaiHat());
                MediaCustom.isRandom = true;

                BaiHat bh2 = danhBaiHats.get(1);
                if (bh2 != null) {
                    NextMiniNhacFragment.tenBaiHat.setText(bh2.getTenBaiHat());
                    NextMiniNhacFragment.tenCaSi.setText(bh2.getCasi().getTenCaSi());
                    Glide.with(getContext()).load(bh2.getAnhBia()).into(NextMiniNhacFragment.imgNhac);
                }
            }
        });
    }

    private void setItemTouchHelper() {
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(new ItemTouchHelper.Callback() {
            @Override
            public int getMovementFlags(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder) {
                int dragFlags = ItemTouchHelper.UP | ItemTouchHelper.DOWN; // Di chuyển lên và xuống
                int swipeFlags = ItemTouchHelper.LEFT | ItemTouchHelper.RIGHT; // Không cho phép vuốt
                return makeMovementFlags(dragFlags, swipeFlags);
            }

            @Override
            public boolean onMove(@NonNull RecyclerView recyclerView, @NonNull RecyclerView.ViewHolder viewHolder, @NonNull RecyclerView.ViewHolder target) {
                int fromPosition = viewHolder.getAdapterPosition();
                int toPosition = target.getAdapterPosition();

                adapter.notifyItemMoved(fromPosition, toPosition);

                Log.e("from", String.valueOf(fromPosition));
                Log.e("to", String.valueOf(toPosition));
                Log.e("ten bh", danhBaiHats.get(fromPosition).getTenBaiHat());
                Log.e("ten bh", danhBaiHats.get(toPosition).getTenBaiHat());


                BaiHat a = danhBaiHats.get(fromPosition);
                BaiHat b = danhBaiHats.get(toPosition);

                danhBaiHats.remove(fromPosition);
                danhBaiHats.add(fromPosition, b);

                danhBaiHats.remove(toPosition);
                danhBaiHats.add(toPosition, a);

                String idFrom = danhBaiHats.get(fromPosition).getId();
                String idTo = danhBaiHats.get(toPosition).getId();

                doiViTriBaiHat(idFrom, idTo);


                return true;
            }

            @Override
            public void onSwiped(@NonNull RecyclerView.ViewHolder viewHolder, int direction) {
                Log.e("vao", "");
                int position = viewHolder.getAdapterPosition();
                deleteBaiHatKhoiDS(danhBaiHats.get(position).getId());

                danhBaiHats.remove(position);
                adapter.notifyDataSetChanged();
            }

        });

        itemTouchHelper.attachToRecyclerView(recyclerView);
    }

    private void deleteBaiHatKhoiDS(String idBH) {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.xoaBaiHatKhoiDS(idDanhSachPhat, idBH, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null && res.getErrCode() != 0) {
                    Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                            .show();
                }
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Toast.makeText(getContext(), "Loi server", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    private void doiViTriBaiHat(String idFrom, String idTo) {
        String header = Common.getHeader();

//        ProgressDialog progressDialog = new ProgressDialog(getContext());
//        progressDialog.setTitle("Đang xử lý...");
//        progressDialog.show();
        ApiServiceV1.apiServiceV1.doiViTriBaiHatTrongDS(idFrom, idTo, idDanhSachPhat, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
//                        layDanhSachBaiHat();
                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();
                    }
                }
//                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Toast.makeText(getContext(), "Loi server", Toast.LENGTH_SHORT)
                        .show();
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        layDanhSachBaiHat();
    }

    private void submitEditTenDanhSach() {
        String tenMoi = String.valueOf(tenDS.getText());
        String idDS = idDanhSachPhat;
        String header = Common.getHeader();

        ProgressDialog progressDialog = new ProgressDialog(getContext());
        progressDialog.setTitle("Đang xử lý...");
        progressDialog.show();
        ApiServiceV1.apiServiceV1.doiTenDanhSach(idDS, tenMoi, header).enqueue(new Callback<ResponseDefault>() {
            @Override
            public void onResponse(Call<ResponseDefault> call, Response<ResponseDefault> response) {
                ResponseDefault res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        tenDS.setEnabled(false);
                        tenDS.setBackground(new ColorDrawable(Color.BLACK));
                        tenDS.setTextColor(Color.WHITE);
                        tenDS.setPadding(10, 0, 10, 0);
                        btnEdit.setVisibility(View.VISIBLE);
                        btnSubmitEdit.setVisibility(View.GONE);

                        for (DanhSachPhat i : ThuVienFragment.danhSachPhats) {
                            if (i.getId() == idDanhSachPhat) {
                                i.setTenDanhSach(tenMoi);
                            }
                        }

                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();


                    }
                }
                progressDialog.dismiss();
            }

            @Override
            public void onFailure(Call<ResponseDefault> call, Throwable t) {
                Log.e("Loi doi ten ds", "");
            }
        });
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        isChiTietDS = false;
        idDanhSachPhat = null;
        danhBaiHats = null;
    }

    private void layDanhSachBaiHat() {
        String idDanhSachPhat = ChiTietThuVienFragment.idDanhSachPhat;
        String header = "bearer " + MainActivity.accessToken;

        ApiServiceV1.apiServiceV1.getDanhSachPhatById(idDanhSachPhat, header).enqueue(new Callback<GetDSPhatById>() {
            @Override
            public void onResponse(Call<GetDSPhatById> call, Response<GetDSPhatById> response) {
                GetDSPhatById res = response.body();
                if (res != null) {
                    if (res.getErrCode() == 0) {
                        tenDanhSach = res.getData().getTenDanhSach();


                        layoutBtn.setVisibility(View.VISIBLE);
                        int slBh = res.getData().getChiTietDanhSachPhats() != null ?
                                res.getData().getChiTietDanhSachPhats().size() : 0;
                        tenDS.setText(res.getData().getTenDanhSach());
                        slBaiHat.setText(String.valueOf(slBh) + " bài hát");

                        if (res.getData().getChiTietDanhSachPhats().size() == 0) {

                            chuaCoBaihat.setVisibility(View.VISIBLE);
                            return;
                        }
                        btnPlayRanDom.setEnabled(true);

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


                        danhBaiHats = new ArrayList<>();

                        for (ChiTietDanhSachPhat i : res.getData().getChiTietDanhSachPhats()) {
                            danhBaiHats.add(i.getBaihat());
                            tgDanhSach += (long) i.getBaihat().getThoiGian();
                        }

                        Log.e("thoi gian", String.valueOf(tgDanhSach));

                        long seconds = tgDanhSach / 1000;
                        long minutes = seconds / 60;
                        long hours = minutes / 60;
                        minutes %= 60;
                        seconds %= 60;

                        slBaiHat.append(" - " + hours + " giờ " + minutes + " phút " + seconds + " giây");

                        adapter = new BaiHatAdapter(danhBaiHats, getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setLayoutManager(manager);

                        int desiredHeightInDp = danhBaiHats.size() * 84;
                        int desiredHeightInPixels = (int) TypedValue.applyDimension(
                                TypedValue.COMPLEX_UNIT_DIP, desiredHeightInDp, getResources().getDisplayMetrics()
                        );
                        ViewGroup.LayoutParams layoutParams = recyclerView.getLayoutParams();
                        layoutParams.height = desiredHeightInPixels;
                        recyclerView.setLayoutParams(layoutParams);
                        recyclerView.setNestedScrollingEnabled(false);


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