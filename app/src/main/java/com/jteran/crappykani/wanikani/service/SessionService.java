package com.jteran.crappykani.wanikani.service;

import io.reactivex.Completable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SessionService {

    @FormUrlEncoded
    @POST("login")
    Completable login(
            @Field("user[login]") String username,
            @Field("user[password]") String password,
            @Field("user[remember_me]") String rememberMe,
            @Field("utf8") String utf8,
            @Field("authenticity_token") String authenticityToken);

    @FormUrlEncoded
    @POST("logout")
    Completable logout(
            @Field("_method") String method,
            @Field("authenticity_token") String authenticityToken);

}
