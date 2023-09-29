package com.example.musicapp.fragments;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;

import androidx.annotation.NonNull;
import androidx.appcompat.widget.Toolbar;
import androidx.fragment.app.Fragment;

import android.speech.RecognizerIntent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.example.musicapp.R;
import com.example.musicapp.activities.LoginActivity;
import com.example.musicapp.activities.MainActivity;
import com.example.musicapp.activities.SettingActivity;
import com.example.musicapp.api.ApiServiceV1;
import com.example.musicapp.modal.anhxajson.GetTaiKhoan;
import com.example.musicapp.modal.anhxajson.TaiKhoan;
import com.example.musicapp.utils.Common;
import com.google.android.gms.auth.api.signin.GoogleSignIn;
import com.google.android.gms.auth.api.signin.GoogleSignInClient;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.android.material.appbar.MaterialToolbar;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class CaNhanFragment extends Fragment {

    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";


    private String mParam1;
    private String mParam2;

    MaterialToolbar topAppBar;

    ImageView anhUser;
    TextView tenUser, typeUser;

    LinearLayout xemDsQuanTam;


    public CaNhanFragment() {
        // Required empty public constructor
    }


    public static CaNhanFragment newInstance(String param1, String param2) {
        CaNhanFragment fragment = new CaNhanFragment();
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
        View view = inflater.inflate(R.layout.fragment_ca_nhan, container, false);
        topAppBar = view.findViewById(R.id.topAppBar);
        anhUser = view.findViewById(R.id.anhUser);
        tenUser = view.findViewById(R.id.tenUser);
        typeUser = view.findViewById(R.id.typeUser);
        xemDsQuanTam = view.findViewById(R.id.xemDsQuanTam);

        topAppBar.setOnMenuItemClickListener(new Toolbar.OnMenuItemClickListener() {
            @Override
            public boolean onMenuItemClick(MenuItem item) {
                Log.e(String.valueOf(item), "");
                if (String.valueOf(item).equals("search")) {
                    TimKiemFragment.typeBack = 2;
                    Common.replace_fragment(new TimKiemFragment());
                } else if (String.valueOf(item).equals("micro")) {
                    Intent intent = new Intent(RecognizerIntent.ACTION_RECOGNIZE_SPEECH);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE_MODEL, RecognizerIntent.LANGUAGE_MODEL_FREE_FORM);
                    intent.putExtra(RecognizerIntent.EXTRA_LANGUAGE, Locale.getDefault().toString());
                    startActivityForResult(intent, 1);

                } else if (String.valueOf(item).equals("setting")) {
                    Intent intent = new Intent(getContext(), SettingActivity.class);
                    startActivity(intent);

                }
                return false;
            }
        });

        xemDsQuanTam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                MainActivity.bottomNavigationView.setSelectedItemId(R.id.thuVien);
                Common.replace_fragment(new NgheSiQuanTamFragment());
            }
        });

        getTaiKhoan();
        return view;
    }

    private void getTaiKhoan() {
        String header = Common.getHeader();

        ApiServiceV1.apiServiceV1.getTaiKhoan(header).enqueue(new Callback<GetTaiKhoan>() {
            @Override
            public void onResponse(Call<GetTaiKhoan> call, Response<GetTaiKhoan> response) {
                GetTaiKhoan res = response.body();

                if (res != null) {
                    if (res.getErrCode() == 0) {
                        TaiKhoan tk = res.getData();

                        tenUser.setText(tk.getFirstName() + " " + tk.getLastName());
                        if (!tk.getIdTypeUser().equals("3")) {
                            typeUser.setText("Quản trị viên");
                        } else {
                            typeUser.setText("Người dùng mới");
                        }

                        //set avatar

                        if (tk.getTypeAccount().equals("web")) {
                            if (tk.getAvatarUpdate() != null) {
                                Glide.with(getContext()).load(tk.getAvatarUpdate())
                                        .into(anhUser);
                            } else {
                                Glide.with(getContext()).load("https://res.cloudinary.com/dultkpqjp/image/upload/v1683860764/avatar_user/no-user-image_axhl6d.png")
                                        .into(anhUser);
                            }
                        } else if (tk.getTypeAccount().equals("google")) {
                            if (tk.getAvatarUpdate() != null) {
                                Glide.with(getContext()).load(tk.getAvatarUpdate())
                                        .into(anhUser);
                            } else {
                                Glide.with(getContext()).load(tk.getAvatarGoogle())
                                        .into(anhUser);
                            }
                        } else if (tk.getTypeAccount().equals("facebook")) {
                            if (tk.getAvatarUpdate() != null) {
                                Glide.with(getContext()).load(tk.getAvatarUpdate())
                                        .into(anhUser);
                            } else {
                                Glide.with(getContext()).load(tk.getAvatarFacebook())
                                        .into(anhUser);
                            }
                        }


                    } else {
                        if (res.getStatus() == 401) {
                            System.exit(0);
                        }
                        if (res.getErrMessage().equals("User was blocked!")) {
                            //reset data
                            ThuVienFragment.danhSachPhats = null;

                            Intent intent = new Intent(getActivity(), LoginActivity.class);
                            startActivity(intent);


                            SharedPreferences sharedPreferences = getActivity().getSharedPreferences("DataLocal", Context.MODE_PRIVATE);
                            SharedPreferences.Editor editor = sharedPreferences.edit();

                            editor.remove("accessToken");
                            editor.remove("refreshToken");
                            editor.apply();

                            //dang xuat google
                            GoogleSignInClient googleSignInClient = GoogleSignIn.getClient(getActivity(),
                                    GoogleSignInOptions.DEFAULT_SIGN_IN);
                            googleSignInClient.signOut().addOnCompleteListener(new OnCompleteListener<Void>() {
                                @Override
                                public void onComplete(@NonNull Task<Void> task) {
//                        Toast.makeText(SettingActivity.this,
//                                        "Dang xuat", Toast.LENGTH_SHORT)
//                                .show();
                                }
                            });
                        }

                        Toast.makeText(getContext(), res.getErrMessage(), Toast.LENGTH_SHORT)
                                .show();

                    }
                }
            }

            @Override
            public void onFailure(Call<GetTaiKhoan> call, Throwable t) {
                Toast.makeText(getContext(), "Loi lay tai khoan", Toast.LENGTH_SHORT)
                        .show();
            }
        });

    }


}