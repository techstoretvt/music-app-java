package com.example.musicapp.api;

import com.example.musicapp.modal.anhxajson.Login;
import com.example.musicapp.modal.anhxajson.ResponseDefault;
import com.example.musicapp.modal.body.BodyLogin;
import com.example.musicapp.modal.body.BodySignUp;
import com.example.musicapp.modal.body.BodyXacNhan;
import com.google.gson.Gson;
import com.google.gson.GsonBuilder;

import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.PUT;

public interface ApiServiceV1 {
    Gson gson = new GsonBuilder()
            .setDateFormat("yyyy-MM-dd HH:mm:ss")
            .create();

    ApiServiceV1 apiServiceV1 = new Retrofit.Builder()
            .baseUrl("https://techstoretvtserver2.onrender.com")
            .addConverterFactory(GsonConverterFactory.create(gson))
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
