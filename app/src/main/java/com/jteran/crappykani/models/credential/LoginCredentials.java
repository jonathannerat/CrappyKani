package com.jteran.crappykani.models.credential;

import android.support.annotation.NonNull;

public class LoginCredentials {

    /**
     * Taken from wanikani's login page
     */
    private static final int MAX_USERNAME_LENGTH = 255;

    /**
     * Taken from wanikani's login page
     */
    private static final int MAX_PASSWORD_LENGTH = 128;

    private String username;
    private String password;
    private String rememberMe = "1";

    public LoginCredentials(@NonNull String username, @NonNull String password) {
        if (username.length() == 0)
            throw new IllegalArgumentException("Username can't be empty.");

        if (password.length() == 0)
            throw new IllegalArgumentException("Password can't be empty.");

        if (username.length() > MAX_USERNAME_LENGTH)
            throw new IllegalArgumentException("Username can't have more than " + MAX_USERNAME_LENGTH + " characters.");

        if (password.length() > MAX_PASSWORD_LENGTH)
            throw new IllegalArgumentException("Password can't have more than " + MAX_PASSWORD_LENGTH + " characters.");

        this.username = username;
        this.password = password;
    }

    public String getUsername() {
        return username;
    }

    public String getPassword() {
        return password;
    }

    public String getRememberMe() {
        return rememberMe;
    }

    @Override
    public String toString() {
        return "LoginCredentials{" +
                "username='" + username + '\'' +
                ", password='********'" + // hide sensible data
                ", rememberMe=" + rememberMe +
                '}';
    }
}
