package com.jteran.crappykani.manager.preferences;

/**
 * Utility class. Holds the keys names for each preference.
 */
final class Keys {
    static final String WAS_CHECK_LOGIN_CALLED = "check_login_was_called";
    static final String LOGIN_STATUS = "login_status";
    static final String COOKIE__WANIKANI_SESSION = "session_cookie";
    static final String COOKIE__REMEMBER_USER_TOKEN = "remember_user_token";
    static final String IS_USER_LOGGED_IN = "is_user_logged_in";
    static final String LAST_USER_LOGGED_IN = "last_user_logged_in";
    static final String API_KEY = "api_key";
    static final String PERSONAL_ACCESS_TOKEN = "api_key_v2";
    static final String CSRF_PARAM = "csrf_param";
    static final String CSRF_TOKEN = "csrf_token";

    private Keys() {
        throw new IllegalStateException("Non instantiable class");
    }
}
