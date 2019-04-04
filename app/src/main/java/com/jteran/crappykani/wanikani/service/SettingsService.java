package com.jteran.crappykani.wanikani.service;

import io.reactivex.Completable;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface SettingsService {

    @FormUrlEncoded
    @POST("personal_access_tokens")
    Completable createPersonalAccessToken(
            @Field("personal_access_token[description]") String description,
            @Field("personal_access_token[permissions][assignments][start]") String permAssignmentsStart,
            @Field("personal_access_token[permissions][reviews][create]") String permReviewsCreate,
            @Field("personal_access_token[permissions][study_materials][create]") String permStudiMaterialsCreate,
            @Field("personal_access_token[permissions][study_materials][update]") String permStudiMaterialsUpdate,
            @Field("personal_access_token[permissions][user][update]") String permUserUpdate,
            @Field("utf8") String utf8,
            @Field("authenticity_token") String authenticityToken
    );
}
