package com.jteran.crappykani.manager.preferences;

import com.jteran.crappykani.models.LoginStatus;

/**
 * Utility class. Holds the default values for each preference.
 */
final class Defaults {
    static final int LOGIN_STATUS = LoginStatus.LOGGED_OUT;
    static final String COOKIE__WANIKANI_SESSION = null;
    static final String COOKIE__REMEMBER_USER_TOKEN = null;
    static final String LAST_USER_LOGGED_IN = null;
    static final String API_KEY = null;
    static final String PERSONAL_ACCESS_TOKEN = null;
    static final boolean IS_USER_LOGGED_IN = false;

    private Defaults() {
        throw new IllegalStateException("Non instantiable class.");
    }
}
