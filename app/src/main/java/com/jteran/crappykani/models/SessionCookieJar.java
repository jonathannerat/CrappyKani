package com.jteran.crappykani.models;

import android.support.annotation.NonNull;

import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.credential.SessionCookies;

import java.util.ArrayList;
import java.util.List;

import okhttp3.Cookie;
import okhttp3.CookieJar;
import okhttp3.HttpUrl;

public class SessionCookieJar implements CookieJar {
    private static final String WANIKANI_SESSION_COOKIE_NAME = "_wanikani_session";
    private static final String REMEMBER_USER_TOKEN_COOKIE_NAME = "remember_user_token";
    private static final String COOKIE_DOMAIN = "www.wanikani.com";

    private SessionCookies sessionCookies = PrefManager.getSessionCookies();
    private Cookie wanikaniSessionCookie;
    private Cookie rememberUserTokenCookie;
    private List<Cookie> savedCookies = new ArrayList<>();

    public SessionCookieJar() {
        String wanikaniSession;
        String rememberUserToken;

        if (sessionCookies != null) {
            wanikaniSession = sessionCookies.getWanikaniSession();
            rememberUserToken = sessionCookies.getRememberUserToken();

            if (wanikaniSession != null) {
                wanikaniSessionCookie = new Cookie.Builder()
                        .name(WANIKANI_SESSION_COOKIE_NAME)
                        .value(wanikaniSession)
                        .domain(COOKIE_DOMAIN)
                        .build();

                savedCookies.add(wanikaniSessionCookie);
            }

            if (rememberUserToken != null) {
                rememberUserTokenCookie = new Cookie.Builder()
                        .name(REMEMBER_USER_TOKEN_COOKIE_NAME)
                        .value(rememberUserToken)
                        .domain(COOKIE_DOMAIN)
                        .build();

                savedCookies.add(rememberUserTokenCookie);
            }
        }
    }

    @Override
    public void saveFromResponse(@NonNull HttpUrl url, @NonNull List<Cookie> cookies) {
        if (!PrefManager.isUserLoggedIn()) {
            for (Cookie cookie : cookies) {
                if (cookie.name().equals(WANIKANI_SESSION_COOKIE_NAME)) {
                    wanikaniSessionCookie = cookie;
                }

                if (cookie.name().equals(REMEMBER_USER_TOKEN_COOKIE_NAME)) {
                    rememberUserTokenCookie = cookie;
                }
            }

            if (wanikaniSessionCookie != null) {
                savedCookies.clear();
                savedCookies.add(wanikaniSessionCookie);
                if (rememberUserTokenCookie != null) {
                    savedCookies.add(rememberUserTokenCookie);
                    sessionCookies = new SessionCookies(wanikaniSessionCookie.value(), rememberUserTokenCookie.value());
                } else {
                    sessionCookies = new SessionCookies(wanikaniSessionCookie.value());
                }

                PrefManager.saveSessionCookies(sessionCookies);
            }
        }

    }

    @Override
    @NonNull
    public List<Cookie> loadForRequest(@NonNull HttpUrl url) {

        return savedCookies;
    }
}