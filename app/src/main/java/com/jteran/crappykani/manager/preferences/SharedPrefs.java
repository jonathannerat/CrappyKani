package com.jteran.crappykani.manager.preferences;

import android.content.Context;
import android.content.SharedPreferences;
import android.support.annotation.NonNull;

/**
 * Additional abstraction layer for SharedPreferences
 */
final class SharedPrefs {
    /**
     * SharedPreferences file name
     */
    private static final String PREFERENCES_NAME = "com.jteran.crappycani.SHARED_PREFS";

    /**
     * SharedPreferences file mode
     */
    private static final int PREFERENCES_MODE = Context.MODE_PRIVATE;

    /**
     * App preferences
     */
    private SharedPreferences prefs;

    SharedPrefs(@NonNull Context context) {
        prefs = context.getApplicationContext().getSharedPreferences(PREFERENCES_NAME, PREFERENCES_MODE);
    }

    int get(@NonNull String key, int defaultValue) {
        return prefs.getInt(key, defaultValue);
    }

    boolean get(@NonNull String key, boolean defaultValue) {
        return prefs.getBoolean(key, defaultValue);
    }

    String get(@NonNull String key, String defaultValue) {
        return prefs.getString(key, defaultValue);
    }

    void put(@NonNull String key, int value) {
        prefs.edit().putInt(key, value).apply();
    }

    void put(@NonNull String key, boolean value) {
        prefs.edit().putBoolean(key, value).apply();
    }

    void put(@NonNull String key, String value) {
        prefs.edit().putString(key, value).apply();
    }

    void remove(@NonNull String key) {
        prefs.edit().remove(key).apply();
    }
}
