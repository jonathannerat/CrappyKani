package com.jteran.crappykani.app.service;

import android.app.IntentService;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.content.LocalBroadcastManager;

import com.jteran.crappykani.helper.utils.Constants;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.credential.LoginCredentials;
import com.jteran.crappykani.wanikani.RestProvider;
import com.jteran.crappykani.wanikani.Scrapper;
import com.jteran.crappykani.wanikani.service.ScrapperService;
import com.jteran.crappykani.wanikani.service.SessionService;

import org.jsoup.nodes.Document;

import java.io.IOException;

import retrofit2.Response;
import timber.log.Timber;

public class LoginService extends IntentService {

    public static final String LOGIN_USERNAME_KEY = "user[login]";
    public static final String LOGIN_PASSWORD_KEY = "user[password]";

    /**
     * key to send login status in bundles
     */
    public static final String LOGIN_STATUS_KEY = "login_status";
    public static final String LOGIN_ERROR_MSG_KEY = "login_error_msg";

    /**
     * action used to send broadcast
     */
    public static final String LOGIN_STATUS_ACTION = "com.jteran.crappykani.service.login.STATUS_CHANGED";

    private LoginCredentials credentials;
    private String authToken;
    private SessionService session;
    private ScrapperService scrapper;

    public LoginService() {
        super("LoginService");

        setIntentRedelivery(true); // preserve original intent with credentials on service restart

        this.session = RestProvider.getSessionService();
        this.scrapper = RestProvider.getScrapperService();
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        Bundle extras = intent == null ? null : intent.getExtras();

        if (extras != null) {
            String username = extras.getString(LOGIN_USERNAME_KEY);
            String password = extras.getString(LOGIN_PASSWORD_KEY);

            Timber.d("username: %s", username);
            Timber.d("password: %s", password);

            if (password != null && username != null) {
                credentials = new LoginCredentials(username, password);
                Intent statusChangeIntent = new Intent(LOGIN_STATUS_ACTION);
                Bundle statusChangeExtras;

                try {
                    authToken = fetchAuthenticityTokenFromLogin();
                    Response loginResponse = login();
                    if (loginResponse.isSuccessful()) {
                        Bundle succcessExtras = new Bundle();
                        succcessExtras.putInt(LOGIN_STATUS_KEY, LoginStatus.LOGGED_IN);

                        PrefManager.setLoginStatus(LoginStatus.LOGGED_IN);

                        statusChangeExtras = succcessExtras;
                    } else {
                        Bundle errorExtras = new Bundle();
                        errorExtras.putString(
                                LOGIN_ERROR_MSG_KEY,
                                loginResponse.code() + ": " + loginResponse.message());
                        statusChangeExtras = errorExtras;
                    }
                } catch (IOException e) {
                    Bundle errorExtras = new Bundle();
                    errorExtras.putString(LOGIN_ERROR_MSG_KEY, e.getLocalizedMessage());
                    statusChangeExtras = errorExtras;
                }

                statusChangeIntent.putExtras(statusChangeExtras);

                // notify the login activity if active
                LocalBroadcastManager.getInstance(this).sendBroadcast(statusChangeIntent);
            }
        }
    }

    private String fetchAuthenticityTokenFromLogin() throws IOException {
        Document loginPage = scrapper.navigateTo("login").execute().body();

        if (loginPage != null) {
            return Scrapper.getLoginAuthenticityToken(loginPage);
        }

        return null;
    }

    private Response login() throws IOException {
        return session.login(
                credentials.getUsername(),
                credentials.getPassword(),
                credentials.getRememberMe(),
                Constants.UTF8_TICK,
                authToken).execute();
    }
}
