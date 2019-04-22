package com.jteran.crappykani.app.service;

import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;

import com.jteran.crappykani.R;
import com.jteran.crappykani.exceptions.MissingCredentialsException;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.MessageType;
import com.jteran.crappykani.models.credential.LoginCredentials;
import com.jteran.crappykani.wanikani.Scrapper;
import com.jteran.crappykani.wanikani.Wanikani;

import org.jsoup.nodes.Document;

import java.io.IOException;

public class LoginService extends SessionMessengerService {

    public static final String LOGIN_USERNAME_KEY = "user[login]";
    public static final String LOGIN_PASSWORD_KEY = "user[password]";

    public LoginService() {
        super("LoginService");

        setIntentRedelivery(true); // preserve original intent with credentials on service restart
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            LoginCredentials credentials = parseCredentials(intent);
            String authToken = fetchAuthenticityTokenFromLogin();
            login(credentials, authToken);
        } catch (Exception e) {
            createMessageBundle(
                    e.getLocalizedMessage(),
                    MessageType.ERROR);
        }

        sendMessage();
    }

    private void login(@NonNull LoginCredentials credentials, @NonNull String authToken) throws IOException {

        if (Wanikani.login(credentials, authToken)) {
            PrefManager.setLoginStatus(LoginStatus.LOGGED_IN);
            createMessageBundle(
                    getString(R.string.logged_in_successfully),
                    MessageType.SUCCESS);
        } else {
            createMessageBundle(
                    getString(R.string.something_went_wrong),
                    MessageType.ERROR);
        }
    }

    private LoginCredentials parseCredentials(@Nullable Intent intent) {
        Bundle credentialsExtras = intent == null ? null : intent.getExtras();

        if (credentialsExtras != null) {
            String username = credentialsExtras.getString(LOGIN_USERNAME_KEY);
            String password = credentialsExtras.getString(LOGIN_PASSWORD_KEY);

            if (password != null && username != null) {
                return new LoginCredentials(username, password);
            }

            throw new MissingCredentialsException("No credentials in extras");
        }

        throw new MissingCredentialsException("No credentials extra");
    }

    private String fetchAuthenticityTokenFromLogin() throws IOException {
        Document loginPage = Wanikani.navigateTo("login");

        return Scrapper.getLoginAuthenticityToken(loginPage);
    }

}
