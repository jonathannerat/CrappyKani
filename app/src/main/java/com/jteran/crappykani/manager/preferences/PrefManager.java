package com.jteran.crappykani.manager.preferences;

import android.content.Context;

import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.credential.SessionCookies;

import java.util.regex.Pattern;

/**
 * Preference manager for the app.
 * Uses SharedPreferences for kind of preferences:
 * - prefs: app-wide preferences (login credentials, animations, themes, etc)
 */
public abstract class PrefManager {

    /**
     * App-wide preferences
     */
    private static SharedPrefs prefs;


    // Non-instantiable class
    private PrefManager() {
        throw new RuntimeException("PrefManager is a non-instantiable class");
    }

    /**
     * Initializes the manager.
     * Should be called on app or activity creation
     *
     * @param context Context of the app or activity (this is critical to load the same preferences)
     */
    public static void initialize(Context context) {
        prefs = new SharedPrefs(context);
    }

    public static void clearUserCredentials() {
        setUserLoggedIn(false);
    }

    public static void setUserLoggedIn(boolean value) {
        prefs.put(Keys.IS_USER_LOGGED_IN, value);
    }

    public static void setApiKeyV1(String value) {
        Pattern API_KEY_REGEX = Pattern.compile("^[0-9a-f]{32}$");
        String lowercaseApiKey = value.toLowerCase();
        if (API_KEY_REGEX.matcher(lowercaseApiKey).matches()) {
            prefs.put(Keys.API_KEY, lowercaseApiKey);
        } else {
            throw new IllegalArgumentException("API Key is bad formatted.");
        }
    }

    public static void setPAT(String value) {
        prefs.put(Keys.PERSONAL_ACCESS_TOKEN, value);
    }

    public static void setLoginStatus(@LoginStatus int value) {
        if (value == LoginStatus.LOGGED_OUT) {
            setUserLoggedIn(false);
        } else {
            setUserLoggedIn(true);
        }

        prefs.put(Keys.LOGIN_STATUS, value);
    }

    public static void saveSessionCookies(SessionCookies cookies) {
        if (cookies != null) {
            prefs.put(Keys.COOKIE__WANIKANI_SESSION, cookies.getWanikaniSession());
            prefs.put(Keys.COOKIE__REMEMBER_USER_TOKEN, cookies.getRememberUserToken());
        }
    }

    public static boolean isUserLoggedIn() {
        return prefs.get(Keys.IS_USER_LOGGED_IN, Defaults.IS_USER_LOGGED_IN);
    }

    public static String getApiKeyV1() {
        return prefs.get(Keys.API_KEY, Defaults.API_KEY);
    }

    public static String getLastUserLoggedIn() {
        return prefs.get(Keys.LAST_USER_LOGGED_IN, Defaults.LAST_USER_LOGGED_IN);
    }

    public static @LoginStatus
    int getLoginStatus() {
        return prefs.get(Keys.LOGIN_STATUS, Defaults.LOGIN_STATUS);
    }

    private static String getWanikaniSessionCoookie() {
        return prefs.get(Keys.COOKIE__WANIKANI_SESSION, Defaults.COOKIE__WANIKANI_SESSION);
    }

    private static String getRememberUserTokenCookie() {
        return prefs.get(Keys.COOKIE__REMEMBER_USER_TOKEN, Defaults.COOKIE__REMEMBER_USER_TOKEN);
    }

    public static SessionCookies getSessionCookies() {
        SessionCookies sessionCookies = null;
        String wanikaniSession = getWanikaniSessionCoookie();
        String rememberUserToken = getRememberUserTokenCookie();

        if (wanikaniSession != null) {
            if (rememberUserToken != null) {
                sessionCookies = new SessionCookies(wanikaniSession, rememberUserToken);
            } else {
                sessionCookies = new SessionCookies(wanikaniSession);
            }
        }

        return sessionCookies;
    }

    public static String getPAT() {
        return prefs.get(Keys.PERSONAL_ACCESS_TOKEN, Defaults.PERSONAL_ACCESS_TOKEN);
    }

    public static void setWasCheckLoginCalled(boolean value) {
        prefs.put(Keys.WAS_CHECK_LOGIN_CALLED, value);
    }

    public static boolean wasCheckLoginCalled() {
        return prefs.get(Keys.WAS_CHECK_LOGIN_CALLED, Defaults.WAS_CHECK_LOGIN_CALLED);
    }
}
