package com.jteran.crappykani.wanikani.service;

import org.jsoup.nodes.Document;

import io.reactivex.Single;
import retrofit2.http.GET;
import retrofit2.http.Path;

public interface ScrapperService {

    @GET("{url}")
    Single<Document> navigateTo(@Path(value = "url", encoded = true) String relativePath);

}
