package com.jteran.crappykani.app.service;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.jteran.crappykani.R;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.MessageType;
import com.jteran.crappykani.wanikani.Scrapper;
import com.jteran.crappykani.wanikani.Wanikani;

import org.jsoup.nodes.Document;

import java.io.IOException;

import timber.log.Timber;

public class LogoutService extends SessionMessengerService {
    public LogoutService() {
        super("LogoutService");

        setIntentRedelivery(true);
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        try {
            String authToken = fetchAuthenticityTokenFromDashboard();
            logout(authToken);
        } catch (IOException e) {
            createMessageBundle(e.getLocalizedMessage(), MessageType.ERROR);
        }

        PrefManager.setLoginStatus(LoginStatus.LOGGED_OUT);
        Timber.d("user logged in: %s", PrefManager.isUserLoggedIn());

        sendMessage();
    }

    private String fetchAuthenticityTokenFromDashboard() throws IOException {
        Document dashboardPage = Wanikani.navigateTo("dashboard");

        return Scrapper.getAuthenticityTokenFrom(dashboardPage);
    }

    private void logout(String authToken) throws IOException {
        if (Wanikani.logout(authToken)) {
            createMessageBundle(
                    getString(R.string.signed_out_successfully),
                    MessageType.SUCCESS);
        } else {
            createMessageBundle(
                    getString(R.string.something_went_wrong),
                    MessageType.ERROR);
        }
    }
}
