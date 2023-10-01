package com.example.musicapp.api;

import com.example.musicapp.modal.anhxajson.AddCommentParent;
import com.example.musicapp.modal.anhxajson.DanhSachCaSi;
import com.example.musicapp.modal.anhxajson.GetCaSiByID;
import com.example.musicapp.modal.anhxajson.GetDSPhatById;
import com.example.musicapp.modal.anhxajson.GetListBHYeuThich;
import com.example.musicapp.modal.anhxajson.GetListBaiHat;
import com.example.musicapp.modal.anhxajson.GetListCSQuanTam;
import com.example.musicapp.modal.anhxajson.GetListCommentById;
import com.example.musicapp.modal.anhxajson.GetListPlaylist;
import com.example.musicapp.modal.anhxajson.GetTaiKhoan;
import com.example.musicapp.modal.anhxajson.Keyword;
import com.example.musicapp.modal.anhxajson.LayDsThongBao;
import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.anhxajson.ThemBHVaoDS;
import com.example.musicapp.modal.anhxajson.ThemDSPhat;
import com.example.musicapp.modal.anhxajson.TimKiemBaiHat;
import com.example.musicapp.modal.anhxajson.TimKiemCaSi;
import com.example.musicapp.modal.body.BodyAddCommentChild;
import com.example.musicapp.modal.body.BodyAddCommentParent;
import com.example.musicapp.modal.body.BodyChangePass;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.modal.body.BodyLoginGoogle;
import com.example.musicapp.modal.body.BodySignUp;
import com.example.musicapp.modal.body.BodyThemBHVaoDS;
import com.example.musicapp.modal.body.BodyThemDSPhat;
import com.example.musicapp.modal.body.BodyXacNhan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import java.util.ArrayList;
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
    Call<ThemBHVaoDS> themBaiHatVaoDS(@Body BodyThemBHVaoDS body, @Header("authorization") String authorization);

    @DELETE("/api/v2/xoa-danh-sach-phat")
    Call<ResponseDefault> xoaDanhSachPhatById(@Query("idDanhSachPhat") String idDanhSachPhat,
                                              @Header("authorization") String authorization);

    @DELETE("/api/v2/xoa-bai-hat-khoi-danh-sach")
    Call<ResponseDefault> xoaBaiHatKhoiDS(@Query("idDanhSachPhat") String idDanhSachPhat,
                                          @Query("idBaiHat") String idBaiHat,
                                          @Header("authorization") String authorization);

    @GET("/api/v2/lay-bai-hat-cua-ca-si")
    Call<GetListBaiHat> layBaiHatCuaCaSi(@Query("idCaSi") String idCaSi
            , @Header("authorization") String authorization);

    @GET("/api/v2/goi-y-ca-si")
    Call<DanhSachCaSi> layDSGoiYCaSi(@Query("idCaSi") String idCaSi
            , @Header("authorization") String authorization);

    @GET("/api/v2/lay-ca-si-by-id")
    Call<GetCaSiByID> layCaSiById(@Query("idCaSi") String idCaSi
            , @Header("authorization") String authorization);

    @GET("/api/v2/tim-kiem-bai-hat")
    Call<TimKiemBaiHat> timKiemBaiHat(@Query("tenBaiHat") String tenBH,
                                      @Query("offset") String offset,
                                      @Query("limit") String limit,
                                      @Header("authorization") String authorization);

    @GET("/api/v2/tim-kiem-ca-si")
    Call<TimKiemCaSi> timKiemCaSi(@Query("tenCaSi") String tenCS,
                                  @Query("offset") String offset,
                                  @Query("limit") String limit,
                                  @Header("authorization") String authorization);

    @PUT("/api/v2/doi-ten-danh-sach")
    Call<ResponseDefault> doiTenDanhSach(@Query("idDanhSach") String idDS,
                                         @Query("tenDanhSach") String tenDS,
                                         @Header("authorization") String authorization
    );

    @PUT("/api/v2/doi-vi-tri-trong-ds")
    Call<ResponseDefault> doiViTriBaiHatTrongDS(@Query("idFrom") String idFrom,
                                                @Query("idTo") String idTo,
                                                @Query("idDanhSach") String idDanhSach,
                                                @Header("authorization") String authorization
    );

    @GET("/api/v2/get-list-keyword-search-mobile")
    Call<Keyword> getGoiYTuKhoa(@Query("value") String value);

    @GET("/api/v2/lay-danh-sach-thong-bao")
    Call<LayDsThongBao> layDanhSachTB(
            @Header("authorization") String authorization);

    @GET("/api/get-user-login")
    Call<GetTaiKhoan> getTaiKhoan(@Header("authorization") String authorization);

    @POST("/api/login-google")
    Call<Login> loginGoogle(@Body BodyLoginGoogle body);

    @POST("/api/v2/toggle-yeu-thich-bai-hat")
    Call<ResponseDefault> toggleLikeBaiHat(@Query("idBaiHat") String idBaiHat,
                                           @Header("authorization") String authorization);

    @GET("/api/v2/kiem-tra-yeu-thich-bai-hat")
    Call<ResponseDefault> kiemTraYeuThichBaiHat(@Query("idBaiHat") String idBaiHat,
                                                @Header("authorization") String authorization);

    @GET("/api/v2/lay-danh-sach-bai-hat-yeu-thich")
    Call<GetListBHYeuThich> getListBHYeuThich(@Query("order_by") String order_by,
                                              @Query("order_type") String order_type,
                                              @Header("authorization") String authorization);

    @POST("/api/v2/toggle-quan-tam-ca-si")
    Call<ResponseDefault> toggleQuanTamCasi(@Query("idCaSi") String idCaSi,
                                            @Header("authorization") String authorization);

    @GET("/api/v2/lay-ds-ca-si-quan-tam")
    Call<GetListCSQuanTam> getListCaSiQuanTam(
            @Header("authorization") String authorization);

    @GET("/api/v2/kiem-tra-quan-tam-ca-si")
    Call<ResponseDefault> kiemTraQuanTamCaSi(@Query("idCaSi") String idCaSi,
                                             @Header("authorization") String authorization);

    @GET("/api/v2/get-list-random-bai-hat")
    Call<GetListBaiHat> getListRandomBaiHat(@Query("limit") int limit,
                                            @Query("minusId") String[] minusId,
                                            @Header("authorization") String authorization);

    @GET("/api/v2/get-list-comment-by-id-bai-hat")
    Call<GetListCommentById> getListCommentByIdBaiHat(@Query("idBaiHat") String idBaiHat,
                                                      @Header("authorization") String authorization);

    @POST("/api/v2/add-comment-parent")
    Call<AddCommentParent> addCommentParent(@Body BodyAddCommentParent body,
                                            @Header("authorization") String authorization);

//    @POST("/api/v2/add-comment-child")
//    Call<AddCommentParent> addCommentChild(@Body BodyAddCommentChild body,
//                                           @Header("authorization") String authorization);


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
