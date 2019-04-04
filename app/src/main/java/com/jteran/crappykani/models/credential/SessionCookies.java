package com.jteran.crappykani.models.credential;

import android.support.annotation.NonNull;

import com.jteran.crappykani.helper.utils.Constants;

import java.util.HashMap;
import java.util.Map;

/**
 * Most basic WaniKani credentials
 * <p>
 * Contains the mininum properties necessary to interact with WaniKani
 */
public class SessionCookies {
    private String wanikaniSession;
    private String rememberUserToken;

    private Map<String, String> cookiesAsMap = new HashMap<>();

    public SessionCookies(@NonNull String wanikaniSession) {
        this.wanikaniSession = wanikaniSession;
        cookiesAsMap.put(Constants.COOKIE__WANIKANI_SESSION, wanikaniSession);
    }

    public SessionCookies(@NonNull String wanikaniSession, @NonNull String rememberUserToken) {
        this.wanikaniSession = wanikaniSession;
        this.rememberUserToken = rememberUserToken;
        cookiesAsMap.put(Constants.COOKIE__WANIKANI_SESSION, wanikaniSession);
        cookiesAsMap.put(Constants.COOKIE__REMEMBER_USER_TOKEN, rememberUserToken);
    }

    public String getWanikaniSession() {
        return wanikaniSession;
    }

    public String getRememberUserToken() {
        return rememberUserToken;
    }

    public Map<String, String> asMap() {
        return cookiesAsMap;
    }

    @Override
    public String toString() {
        return "SessionCookies{" +
                "wanikaniSession='" + wanikaniSession + '\'' +
                ", rememberUserToken='" + rememberUserToken + '\'' +
                '}';
    }
}
