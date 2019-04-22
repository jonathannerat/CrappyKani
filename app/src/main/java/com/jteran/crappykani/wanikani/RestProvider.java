package com.jteran.crappykani.wanikani;

import android.support.annotation.NonNull;

import com.jteran.crappykani.BuildConfig;
import com.jteran.crappykani.helper.rx.converter.JsoupConverterFactory;
import com.jteran.crappykani.helper.utils.Constants;
import com.jteran.crappykani.models.SessionCookieJar;
import com.jteran.crappykani.wanikani.service.ScrapperService;
import com.jteran.crappykani.wanikani.service.SessionService;
import com.jteran.crappykani.wanikani.service.SettingsService;

import okhttp3.OkHttpClient;
import okhttp3.logging.HttpLoggingInterceptor;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.moshi.MoshiConverterFactory;

public class RestProvider {

    private static OkHttpClient okHttpClient = null;
    private static ScrapperService scrapperService = null;
    private static SessionService sessionService = null;
    private static SettingsService settingsService = null;


    public static OkHttpClient provideOkHttpClient() {
        if (okHttpClient == null) {
            OkHttpClient.Builder clientBuilder = new OkHttpClient.Builder()
                    .cookieJar(new SessionCookieJar())
                    .followRedirects(false);

            if (BuildConfig.DEBUG) {
                clientBuilder.addInterceptor(
                        new HttpLoggingInterceptor().setLevel(HttpLoggingInterceptor.Level.BASIC)
                );
            }

            okHttpClient = clientBuilder.build();
        }

        return okHttpClient;
    }

    /**
     * Provides special client with Authorization header for API calls
     *
     * @param apiToken Token used by the client
     * @return okHttpClient with Authorization header
     */
    private static OkHttpClient provideOkHttpClient(String apiToken) {

        OkHttpClient.Builder client = provideOkHttpClient().newBuilder();

        // TODO: Add interceptor with Authorization header and apiToken
        // client.addInterceptor()

        return client.build();

    }

    private static Retrofit provideRetrofit(@NonNull String baseUrl, @NonNull OkHttpClient client) {
        return new Retrofit.Builder()
                .baseUrl(baseUrl)
                .client(client)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JsoupConverterFactory.create(baseUrl))
                .addConverterFactory(MoshiConverterFactory.create())
                .build();

    }

    public static SessionService getSessionService() {
        if (sessionService == null) {
            sessionService = provideRetrofit(Constants.URL__HOME, provideOkHttpClient())
                    .create(SessionService.class);
        }

        return sessionService;
    }

    public static SettingsService getSettingsService() {
        if (settingsService == null) {
            settingsService = provideRetrofit(Constants.URL__SETTINGS_BASE, provideOkHttpClient())
                    .create(SettingsService.class);
        }

        return settingsService;
    }

    public static ScrapperService getScrapperService() {
        if (scrapperService == null) {
            scrapperService = provideRetrofit(Constants.URL__HOME, provideOkHttpClient())
                    .create(ScrapperService.class);
        }

        return scrapperService;
    }
}
