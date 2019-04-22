package com.jteran.crappykani.app.service;

import android.content.Intent;
import android.support.annotation.Nullable;

import com.jteran.crappykani.R;
import com.jteran.crappykani.manager.preferences.PrefManager;
import com.jteran.crappykani.models.LoginStatus;
import com.jteran.crappykani.models.MessageType;
import com.jteran.crappykani.wanikani.Wanikani;

import java.io.IOException;

public class CheckLoginService extends SessionMessengerService {

    public CheckLoginService() {
        super("CheckLoginService");
    }

    @Override
    protected void onHandleIntent(@Nullable Intent intent) {
        PrefManager.setWasCheckLoginCalled(true);
        try {
            checkLogin();
        } catch (IOException e) {
            createMessageBundle(e.getLocalizedMessage(), MessageType.ERROR);
        }

        sendMessage();
    }

    private void checkLogin() throws IOException {
        if (!Wanikani.isUserLoggedIn()) {
            PrefManager.setLoginStatus(LoginStatus.LOGGED_OUT);

            createMessageBundle(
                    getString(R.string.sign_in_or_sign_up_before_continuing),
                    MessageType.ERROR);
        }
    }
}