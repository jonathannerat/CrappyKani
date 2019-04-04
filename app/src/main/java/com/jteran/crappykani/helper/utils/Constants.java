package com.jteran.crappykani.helper.utils;

/**
 * Wanikani related constants
 */
public final class Constants {

    // Common WaniKani URLs

    public static final String URL__HOME = "https://www.wanikani.com/";
    public static final String URL__LOGIN = "https://www.wanikani.com/login";
    public static final String URL__LOGOUT = "https://www.wanikani.com/logout";
    public static final String URL__DASHBOARD = "https://www.wanikani.com/dashboard";
    public static final String URL__API = "https://api.wanikani.com/v2/";
    public static final String URL__LESSONS = "https://www.wanikani.com/lesson";
    public static final String URL__REVIEWS = "https://www.wanikani.com/review";
    public static final String URL__LEVELS = "https://www.wanikani.com/dashboard";
    public static final String URL__RADICALS = "https://www.wanikani.com/dashboard";
    public static final String URL__KANJI = "https://www.wanikani.com/dashboard";
    public static final String URL__VOCAB = "https://www.wanikani.com/dashboard";
    public static final String URL__SETTINGS_BASE = "https://www.wanikani.com/settings/";
    public static final String URL__SETTINGS_APP = "https://www.wanikani.com/settings/app";
    public static final String URL__SETTINGS_ACCOUNT = "https://www.wanikani.com/settings/account";
    public static final String URL__SETTINGS_PROFILE = "https://www.wanikani.com/settings/profile";
    public static final String URL__SETTINGS_DANGER_ZONE = "https://www.wanikani.com/settings/danger_zone";
    public static final String URL__SETTINGS_API_TOKENS = "https://www.wanikani.com/settings/personal_access_tokens";


    // Login field names (<input name="..."/>)

    public static final String LOGIN_FN__USERNAME = "user[login]";
    public static final String LOGIN_FN__PASSWORD = "user[password]";
    public static final String LOGIN_FN__REMEMBER_ME = "user[remember_me]";
    public static final String LOGIN_FN__UTF8 = "utf8";
    public static final String UTF8_TICK = "✓";
    public static final String LOGIN_FN__AUTHENTICITY_TOKEN = "authenticity_token";


    // Logout field names (<input name="..."/>)

    public static final String LOGOUT_FN__METHOD = "_method";
    public static final String LOGOUT_FN__AUTHENTICITY_TOKEN = "authenticity_token";


    // CSFR meta-tags names (<meta name="..."/>

    public static final String META__CSRF_PARAM = "csrf-param";
    public static final String META__CSRF_TOKEN = "csrf-token";


    // Session Cookies

    public static final String COOKIE__WANIKANI_SESSION = "_wanikani_session";
    public static final String COOKIE__REMEMBER_USER_TOKEN = "remember_user_token";
    public static final String COOKIE_DOMAIN = "www.wanikani.com";

    // Api Tokens

    public static final String CRAPPYKANI_PAT_NAME = "_crappykani_token";
    public static final String API_KEY_ELEMENT_ID = "user_api_key";

}
