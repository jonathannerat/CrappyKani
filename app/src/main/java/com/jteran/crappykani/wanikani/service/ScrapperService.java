package com.jteran.crappykani.wanikani.service;

import org.jsoup.nodes.Document;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScrapperService {

    @GET("{url}")
    Call<Document> navigateTo(@Path(value = "url", encoded = true) String relativePath);

}
