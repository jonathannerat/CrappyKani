package com.jteran.crappykani.models;

import android.support.annotation.NonNull;

import com.jteran.crappykani.helper.utils.Constants;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.credential.SessionCookies;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class SessionCookieJar implements CookieJar {

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        String wanikaniSession = null;
        String rememberUserToken = null;
        SessionCookies sessionCookies = null;

        if (! PrefManager.isUserLoggedIn()) {
            for (Cookie cookie : cookies) {
                if (cookie.name().equals(Constants.COOKIE__WANIKANI_SESSION)) {
                    wanikaniSession = cookie.value();
                }

                if (cookie.name().equals(Constants.COOKIE__REMEMBER_USER_TOKEN)) {
                    rememberUserToken = cookie.value();
                }
            }

            if (wanikaniSession != null && rememberUserToken != null) {
                sessionCookies = new SessionCookies(wanikaniSession, rememberUserToken);
            } else if (wanikaniSession != null) {
                sessionCookies = new SessionCookies(wanikaniSession);
            }

            PrefManager.saveSessionCookies(sessionCookies);
        }
    }

    @Override
    @NonNull
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {
        List<Cookie> cookies = new ArrayList<>();
        SessionCookies sessionCookies = PrefManager.getSessionCookies();
        String wanikaniSession = sessionCookies != null ? sessionCookies.getWanikaniSession() : null;
        String rememberUserToken = sessionCookies != null ? sessionCookies.getRememberUserToken() : null;


        if (wanikaniSession != null) {
            Cookie wanikaniSessionCookie = new Cookie.Builder()
                    .name(Constants.COOKIE__WANIKANI_SESSION)
                    .value(wanikaniSession)
                    .domain(Constants.COOKIE_DOMAIN)
                    .build();

            cookies.add(wanikaniSessionCookie);
        }

        if (rememberUserToken != null) {
            Cookie rememberUserTokenCookie = new Cookie.Builder()
                    .name(Constants.COOKIE__REMEMBER_USER_TOKEN)
                    .value(rememberUserToken)
                    .domain(Constants.COOKIE_DOMAIN)
                    .build();

            cookies.add(rememberUserTokenCookie);
        }

        return cookies;
    }
}