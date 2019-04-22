package com.jteran.crappykani.wanikani.service;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface SessionService {

    @FormUrlEncoded
    @POST("login")
    Call<Void> login(
            @Field("user[login]") String username,
            @Field("user[password]") String password,
            @Field("user[remember_me]") String rememberMe,
            @Field("utf8") String utf8,
            @Field("authenticity_token") String authenticityToken);

    @GET("dashboard")
    Call<Void> checkLogin();

    @FormUrlEncoded
    @POST("logout")
    Call<Void> logout(
            @Field("_method") String method,
            @Field("authenticity_token") String authenticityToken);

}
