package com.example.musicapp.api;

import com.example.musicapp.modal.anhxajson.GetDSPhatById;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.ThemDSPhat;
import com.example.musicapp.modal.body.BodyChangePass;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.modal.body.BodySignUp;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemDSPhat;
import com.example.musicapp.modal.body.BodyXacNhan;
import com.example.musicapp.modal.body.BodyXoaDSPhat;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.Map;
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

public interface ApiServiceV1 {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    OkHttpClient okHttpClient = new OkHttpClient.Builder()
            .connectTimeout(300, TimeUnit.SECONDS) // Thời gian tối đa cho việc kết nối
            .readTimeout(300, TimeUnit.SECONDS)    // Thời gian tối đa cho việc đọc dữ liệu
            .writeTimeout(30, TimeUnit.SECONDS)   // Thời gian tối đa cho việc ghi dữ liệu
            .build();

    ApiServiceV1 apiServiceV1 = new Retrofit.Builder()
            .baseUrl("https://techstoretvtserver2.onrender.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
            .client(okHttpClient)
            .build()
            .create(ApiServiceV1.class);

    @GET("/api/v1/check-start-server")
    Call<ResponseDefault> checkStartServer();

    @POST("/api/user-login")
    Call<Login> login(@Body BodyLogin value);

    @POST("/api/v2/create-new-user-mobile")
    Call<ResponseDefault> signUp(@Body BodySignUp value);

    @PUT("/api/v2/verify-code-for-create-user-mobile")
    Call<ResponseDefault> xacNhanEmail(@Body BodyXacNhan value);

    @PUT("/apt/v1/get-code-verify-forget-pass")
    Call<ResponseDefault> getCodeQuenMK(@Body BodyLogin value);

    @PUT("/api/v1/change-pass-forget")
    Call<ResponseDefault> doiMK(@Body BodyChangePass value);

    @GET("/api/v2/lay-ds-bai-hat-public")
    Call<GetListBaiHat> getListBaiHatHome(@QueryMap Map<String, String> options);

    @GET("/api/v2/lay-danh-sach-phat")
    Call<GetListPlaylist> getDanhSachPhat(@Header("authorization") String authorization);

    @POST("/api/v2/them-danh-sach-phat")
    Call<ThemDSPhat> themDanhSachPhat(@Body BodyThemDSPhat name, @Header("authorization") String authorization);

    @GET("/api/v2/lay-bai-hat-trong-danh-sach")
    Call<GetDSPhatById> getDanhSachPhatById(@Query("idDanhSachPhat") String idDanhSachPhat
            , @Header("authorization") String authorization);

    @POST("/api/v2/them-bai-hat-vao-danh-sach")
    Call<ResponseDefault> themBaiHatVaoDS(@Body BodyThemBHVaoDS body, @Header("authorization") String authorization);

    @DELETE("/api/v2/xoa-danh-sach-phat")
    Call<ResponseDefault> xoaDanhSachPhatById(@Query("idDanhSachPhat") String idDanhSachPhat, @Header("authorization") String authorization);

    /* Get
    1. Truyền tham số ?access_key=access_key
    @GET("api/v2/get-suggest-product-mobile")
    Call<Currency> productConvert(@Query("access_key") String access_key,
                                  @Query("currency") String currency
                                  );
     2. Truyền trực tiếp
     @GET("api/v2/get-suggest-product-mobile?access_key=access_key")
     Call<Currency> productConvert();

     3. Truyền params
     @GET("api/v2/product/{id}")
     Call<Currency> productConvert(@Path("id") int id);

     4. Truyền nhiều query
     @GET("api/v2/product")
     Call<Currency> productConvert(@QueryMap Map<String, String> options);
     --cách gọi, khởi tạo Map và thêm nhiều key value vào và truyền vào tham số
     Map<String, String> options = new HashMap<>();
     options.put("access_key","access_key");
     */

    /*
     * 1.
     * @Post("/api/product")
     * Call<Currency> productConvert(@Body Post post);
     * --Cách gọi: tạo 1 đối tượng Post và truyền nó vào
     * */
}
