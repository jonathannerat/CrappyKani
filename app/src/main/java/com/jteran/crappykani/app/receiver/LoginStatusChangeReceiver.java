package com.jteran.crappykani.app.receiver;

import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;

import com.jteran.crappykani.app.service.LoginService;
import com.jteran.crappykani.models.LoginStatus;

import io.reactivex.annotations.Nullable;


public class LoginStatusChangeReceiver extends BroadcastReceiver {

    private LoginStatusChangeListener listener;

    public LoginStatusChangeReceiver() {
    }

    @Override
    public void onReceive(Context context, Intent intent) {
        Bundle extras = intent.getExtras();

        if (extras != null) {
            int loginStatus = extras.getInt(LoginService.LOGIN_STATUS_KEY);
            String errorMessage = extras.getString(LoginService.LOGIN_ERROR_MSG_KEY);

            listener.onLoginStatusChange(loginStatus, errorMessage);
        }
    }

    public void attachListener(LoginStatusChangeListener listener) {
        this.listener = listener;
    }

    public void releaseListener() {
        this.listener = null;
    }

    public interface LoginStatusChangeListener {
        void onLoginStatusChange(@LoginStatus int status, @Nullable String errorMsg);
    }
}
