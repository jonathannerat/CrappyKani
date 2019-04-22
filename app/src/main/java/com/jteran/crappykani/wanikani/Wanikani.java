package com.jteran.crappykani.wanikani;

import android.support.annotation.NonNull;
import android.support.annotation.WorkerThread;

import com.jteran.crappykani.helper.utils.Constants;
import com.jteran.crappykani.models.credential.LoginCredentials;
import com.jteran.crappykani.wanikani.service.ScrapperService;
import com.jteran.crappykani.wanikani.service.SessionService;

import org.jsoup.nodes.Document;

import java.io.IOException;

import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;

public class Wanikani {

    private static final OkHttpClient client = RestProvider.provideOkHttpClient();
    private static final SessionService session = RestProvider.getSessionService();
    private static final ScrapperService scrapper = RestProvider.getScrapperService();

    @WorkerThread
    public static boolean isUserLoggedIn() throws IOException {
        Request dashboardRequest = new Request.Builder().url(Constants.URL__DASHBOARD).get().build();
        Response response = client.newCall(dashboardRequest).execute();

        // If request was successful with current cookies (in CookieJar), then the user is logged in
        boolean isLoggedIn = response.code() == 200;
        response.close();

        return isLoggedIn;
    }

    @WorkerThread
    public static Document navigateTo(@NonNull String url) throws IOException {
        return scrapper.navigateTo(url).execute().body();
    }

    @WorkerThread
    public static boolean login(LoginCredentials credentials, String authToken) throws IOException {
        retrofit2.Response loginResponse = session.login(
                credentials.getUsername(),
                credentials.getPassword(),
                credentials.getRememberMe(),
                Constants.UTF8_TICK,
                authToken).execute();

        String redirectUrl = loginResponse.headers().get("Location");

        return loginResponse.code() == 302 && // is redirecting?
                Constants.URL__DASHBOARD.equals(redirectUrl); // to https://www.wanikani.com/dashboard ?
    }

    public static boolean logout(String authToken) throws IOException {
        retrofit2.Response logoutResponse = session.logout("delete", authToken).execute();

        String redirectUrl = logoutResponse.headers().get("Location");


        return logoutResponse.code() == 302 && // is redirecting?
                Constants.URL__HOME.equals(redirectUrl); // to https://www.wanikani.com/ ?
    }
}
