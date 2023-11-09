package com.example.musicapp.api;

import com.example.musicapp.modal.anhxajson.AddCommentCon;
import com.example.musicapp.modal.anhxajson.AddCommentParent;
import com.example.musicapp.modal.anhxajson.DanhSachCaSi;
import com.example.musicapp.modal.anhxajson.GetBaiHatById;
import com.example.musicapp.modal.anhxajson.GetCaSiByID;
import com.example.musicapp.modal.anhxajson.GetDSPhatById;
import com.example.musicapp.modal.anhxajson.GetListBHYeuThich;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.GetListCSQuanTam;
import com.example.musicapp.modal.anhxajson.GetListCommentById;
import com.example.musicapp.modal.anhxajson.GetListIdLikeComment;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.example.musicapp.modal.anhxajson.GetLoiBaiHat;
import com.example.musicapp.modal.anhxajson.GetTaiKhoan;
import com.example.musicapp.modal.anhxajson.Keyword;
import com.example.musicapp.modal.anhxajson.KeywordGoogle;
import com.example.musicapp.modal.anhxajson.LayDsThongBao;
import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.anhxajson.ThemDSPhat;
import com.example.musicapp.modal.anhxajson.TimKiemBaiHat;
import com.example.musicapp.modal.anhxajson.TimKiemCaSi;
import com.example.musicapp.modal.anhxajson.TimKiemMV;
import com.example.musicapp.modal.body.BodyAddCommentCon;
import com.example.musicapp.modal.body.BodyAddCommentParent;
import com.example.musicapp.modal.body.BodyChangePass;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.modal.body.BodyLoginGoogle;
import com.example.musicapp.modal.body.BodySignUp;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemDSPhat;
import com.example.musicapp.modal.body.BodyToggleLikeComment;
import com.example.musicapp.modal.body.BodyXacNhan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.concurrent.TimeUnit;

import okhttp3.OkHttpClient;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.DELETE;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.POST;
import retrofit2.http.PUT;
import retrofit2.http.Query;
import retrofit2.http.QueryMap;

public interface ApiServiceV2 {
    Gson gson = new GsonBuilder()
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(30, TimeUnit.SECONDS) // Thời gian tối đa cho việc kết nối
            .readTimeout(300, TimeUnit.SECONDS)    // Thời gian tối đa cho việc đọc dữ liệu
            .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian tối đa cho việc ghi dữ liệu
            .build();

    ApiServiceV2 apiServiceV2 = new Retrofit.Builder()
            .baseUrl("http://suggestqueries.google.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiServiceV2.class);

    @GET("/complete/search?client=firefox&hl=vi")
    Call<ArrayList<KeywordGoogle>> getListGoiYGoogle(@Query("q") String q);

}
